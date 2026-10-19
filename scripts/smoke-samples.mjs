import assert from "node:assert/strict";
import { execFile } from "node:child_process";
import { randomUUID } from "node:crypto";
import { mkdtemp, readFile, rm, writeFile } from "node:fs/promises";
import { request as httpsRequest } from "node:https";
import { tmpdir } from "node:os";
import { dirname, join, resolve } from "node:path";
import { setTimeout as delay } from "node:timers/promises";
import { fileURLToPath } from "node:url";
import { promisify } from "node:util";

const execute = promisify(execFile);
const root = resolve(dirname(fileURLToPath(import.meta.url)), "..");
const flags = ["--non-interactive", "--nologo"];
const marker = `smoke-${randomUUID()}`;
let httpsCertificate;

async function command(executable, args, cwd = root) {
  try {
    const { stdout } = await execute(executable, args, {
      cwd, timeout: 600_000, maxBuffer: 8 * 1024 * 1024, windowsHide: true,
    });
    return stdout;
  } catch (error) {
    const output = `${error.stdout ?? ""}\n${error.stderr ?? ""}`.trim();
    throw new Error(
      `${executable} ${args.join(" ")} failed (${error.code ?? error.message}).\n` +
      output.split(/\r?\n/).slice(-60).join("\n"),
      { cause: error },
    );
  }
}

function cliJson(output) {
  const start = output.search(/^[\[{]/m);
  assert.notEqual(start, -1, `No JSON in CLI output: ${output}`);
  return JSON.parse(output.slice(start));
}

async function request(url, options = {}, status = 200) {
  let response;
  try {
    response = url.startsWith("https:") ? await requestHttps(url, options) :
      await fetch(url, { redirect: "manual", signal: AbortSignal.timeout(30_000), ...options });
  } catch (error) {
    throw new Error(`${options.method ?? "GET"} ${url}: ${error.cause?.message ?? error.message}`, { cause: error });
  }
  if (response.status !== status) {
    assert.fail(`${options.method ?? "GET"} ${url}: expected ${status}, got ` +
      `${response.status}\n${(await response.text()).slice(0, 1500)}`);
  }
  return response;
}

function requestHttps(url, options) {
  return new Promise((resolve, reject) => {
    const outgoing = httpsRequest(url, {
      method: options.method, headers: options.headers, ca: httpsCertificate,
      signal: AbortSignal.timeout(30_000),
    }, incoming => {
      const chunks = [];
      incoming.on("data", chunk => chunks.push(chunk));
      incoming.on("error", reject);
      incoming.on("end", () => {
        const headers = new Headers();
        for (let i = 0; i < incoming.rawHeaders.length; i += 2) {
          headers.append(incoming.rawHeaders[i], incoming.rawHeaders[i + 1]);
        }
        const body = [204, 205, 304].includes(incoming.statusCode) || options.method === "HEAD" ?
          null : Buffer.concat(chunks);
        resolve(new Response(body, { status: incoming.statusCode, headers }));
      });
    });
    outgoing.on("error", reject);
    outgoing.end(options.body);
  });
}

async function trustDevelopmentCertificate() {
  const directory = await mkdtemp(join(tmpdir(), "aspire-smoke-cert-"));
  try {
    await command("dotnet", ["dev-certs", "https", "--check", "--quiet"]);
    const path = join(directory, "localhost.pem");
    // Export only the public certificate, never --password or --no-password.
    await command("dotnet", ["dev-certs", "https", "--export-path", path, "--format", "Pem", "--quiet"]);
    const certificate = await readFile(path, "utf8");
    assert(!certificate.includes("PRIVATE KEY"), "Unexpected private key in certificate export");
    httpsCertificate = certificate;
  } finally {
    await rm(directory, { recursive: true, force: true });
  }
}

async function json(base, path, data, status = 200, method = data ? "POST" : "GET") {
  const response = await request(`${base}${path}`, {
    method,
    headers: data ? { "Content-Type": "application/json" } : undefined,
    body: data ? JSON.stringify(data) : undefined,
  }, status);
  return status === 204 ? undefined : response.json();
}

async function psql(container, resource, database, sql) {
  return container(resource, "sh", "-c",
    'PGPASSWORD="$POSTGRES_PASSWORD" exec psql -w -X -U "$POSTGRES_USER" -d "$1" -v ON_ERROR_STOP=1 -tAc "$2"',
    "smoke-psql", database, sql);
}

async function eventually(check, description, timeout = 120_000) {
  const deadline = Date.now() + timeout;
  do {
    const result = await check();
    if (result) return result;
    await delay(1000);
  } while (Date.now() < deadline);
  assert.fail(`Timed out waiting for ${description}`);
}

const samples = [
  ["terminal-demo", "apphost.cs", ["shell", "node-repl"], terminals],
  ["python-fastapi-docker", "apphost.cs", ["api"], analyzer],
  ["ts-starter", "apphost.mts", ["app", "frontend"], weather],
  ["postgres-adminer", "apphost.cs", ["db", "adminer"], adminer],
  ["go-redis-compose", "apphost.cs", ["cache", "api"], hits],
  ["java-javalin-redis", "apphost.cs", ["cache", "api"], leaderboard],
  ["hono-redis-urls", "apphost.mts", ["cache", "api", "web"], shortener],
  ["vite-react-api", "apphost.mts", ["cache", "api", "web"], todos],
  ["dotnet-react-postgres", "apphost.cs", ["postgres", "api", "web"], quotes],
  ["dotnet-angular-cosmos", join("AppHost", "AppHost.csproj"),
    ["cosmos", "recipesdb", "recipes", "api", "frontend"], recipes],
  ["polyglot-event-stream", join("AppHost", "AppHost.csproj"),
    ["messaging", "producer", "consumer", "dashboard", "kafka-ui"], events],
  ["preview/flask-markdown-wiki", "apphost.py", ["cache", "wiki"], wiki],
  ["preview/django-htmx-polls", "apphost.py", ["pg", "pollsdb", "polls"], polls],
  ["preview/svelte-go-bookmarks", "apphost.go", ["pg", "api", "frontend"], bookmarks],
  ["preview/spring-boot-postgres", "AppHost.java", ["pg", "api"], notes],
  ["standalone-dashboard", null, [], standalone],
];

async function appHostSample([name, entryPoint, required, check]) {
  const cwd = join(root, "samples", ...name.split("/"));
  const apphost = join(cwd, entryPoint);
  const aspire = (...args) => command("aspire", [...args, "--apphost", apphost, ...flags], cwd);
  const running = cliJson(await command("aspire", ["ps", "--format", "Json", ...flags]));
  assert(!running.some(app => resolve(app.appHostPath).toLowerCase() === apphost.toLowerCase()),
    `${name} is already running; stop it explicitly before testing. It was not changed.`);

  let failure;
  try {
    await aspire("start", ...(process.env.ASPIRE_SMOKE_PRIVATE_DAEMON === "1" ? [] : ["--isolated"]),
      "--format", "Json");
    for (const resource of required) {
      await aspire("wait", resource, "--status", "healthy", "--timeout", "420");
    }
    const resources = cliJson(await aspire("describe", "--format", "Json")).resources;
    const resource = name => {
      const found = resources.find(item => item.displayName === name);
      assert(found, `Missing resource ${name}`);
      assert.equal(found.state, "Running", `${name} is not running`);
      assert.notEqual(found.healthStatus, "Unhealthy", `${name} is unhealthy`);
      return found;
    };
    const endpoint = name => {
      const urls = resource(name).urls;
      const found = urls.find(item => item.name === "http") ??
        urls.find(item => /^https?:/.test(item.url));
      assert(found, `No HTTP endpoint for ${name}`);
      return found.url.replace(/\/$/, "");
    };
    const container = (name, ...args) => command("docker", ["exec", resource(name).name, ...args]);
    await check({ aspire, endpoint, container });
  } catch (error) {
    failure = error;
    try {
      console.error(await aspire("logs", "--tail", "30"));
    } catch (diagnosticError) {
      console.error(`Could not read ${name} logs: ${diagnosticError.message}`);
    }
  }
  try {
    await aspire("stop");
  } catch (error) {
    throw new Error(`${failure ? `${failure.message}\n` : ""}Cleanup failed: ${error.message}`);
  }
  if (failure) throw failure;
}

async function terminals({ aspire }) {
  const directory = await mkdtemp(join(tmpdir(), "aspire-smoke-"));
  try {
    for (const [resource, input, expected] of [
      ["shell", "echo ASPIRE_SMOKE_OK", /^ASPIRE_SMOKE_OK\s*$/m],
      ["node-repl", "JSON.stringify({answer: 6 * 7, isTTY: process.stdin.isTTY})",
        /"answer":42,"isTTY":true/],
    ]) {
      const tape = join(directory, `${resource}.tape`);
      await writeFile(tape, `Type ${JSON.stringify(input)}\nEnter\nSleep 1s\n`);
      const screen = await aspire("terminal", "tape", "play", resource, "--tape-file", tape);
      assert.match(screen, expected, `${resource} did not execute its terminal input`);
    }
  } finally {
    await rm(directory, { recursive: true, force: true });
  }
}

async function analyzer({ endpoint }) {
  const base = endpoint("api");
  assert.equal((await json(base, "/api/info")).service, "text-analyzer");
  const result = await json(base, "/api/analyze", { text: "Aspire makes polyglot apps easy. Easy!" });
  assert.equal(result.characters, 38);
  assert.equal(result.words, 6);
  assert.equal(result.sentences, 2);
  assert.equal(result.unique_words, 5);
  assert.equal(result.reading_time_seconds, 1.8);
  assert.deepEqual(result.top_words[0], { word: "easy", count: 2 });
  assert.match(await (await request(base)).text(), /Aspire Python Text Analyzer/);
}

async function weather({ endpoint }) {
  const base = endpoint("frontend");
  assert.match(await (await request(base)).text(), /id="root"/);
  const forecast = await json(base, "/api/weatherforecast");
  assert.equal(forecast.length, 5);
  for (const day of forecast) {
    assert(Number.isFinite(Date.parse(day.date)), "Invalid forecast date");
    assert(Number.isFinite(day.temperatureC), "Missing Celsius temperature");
    assert.equal(day.temperatureF, 32 + Math.trunc(day.temperatureC / 0.5556));
    assert.equal(typeof day.summary, "string");
  }
}

async function adminer({ endpoint, container }) {
  assert.match(await (await request(endpoint("adminer"))).text(), /Adminer/);
  assert.equal((await psql(container, "db", "appdb", "SELECT 42")).trim(), "42");
}

async function hits({ endpoint }) {
  const base = endpoint("api");
  const first = await json(base, "/api/hits");
  const second = await json(base, "/api/hits");
  assert.equal(second.hits, first.hits + 1);
}

async function leaderboard({ endpoint }) {
  const base = endpoint("api");
  await json(base, "/api/scores", { player: `${marker}-low`, score: 100 }, 201);
  await json(base, "/api/scores", { player: `${marker}-high`, score: 300 }, 201);
  const { leaderboard } = await json(base, "/api/leaderboard");
  assert(leaderboard.find(item => item.player === `${marker}-high` && item.score === 300));
  assert(leaderboard.find(item => item.player === `${marker}-low` && item.score === 100));
  assert(leaderboard.every((item, index) => index === 0 || item.score <= leaderboard[index - 1].score));
}

async function shortener({ endpoint }) {
  const base = endpoint("web");
  const url = `https://example.com/${marker}`;
  const created = await json(base, "/api/shorten", { url }, 201);
  assert.match(created.code, /^[a-z0-9]{7}$/);
  const redirect = await request(`${base}/r/${created.code}`, {}, 302);
  assert.equal(redirect.headers.get("location"), url);
  const { urls } = await json(base, "/api/urls");
  assert(urls.find(item => item.code === created.code && item.clicks === 1 && item.url === url));
  await json(base, "/api/shorten", { url: "not-a-url" }, 400);
}

async function todos({ endpoint }) {
  const base = endpoint("web");
  const id = Date.now();
  try {
    await json(base, "/api/todos", { id, text: marker, completed: false });
    assert((await json(base, "/api/todos")).todos.some(item => item.id === id && item.text === marker));
  } finally {
    await json(base, `/api/todos/${id}`, undefined, 200, "DELETE");
  }
  assert(!(await json(base, "/api/todos")).todos.some(item => item.id === id));
}

async function quotes({ endpoint, container }) {
  const base = endpoint("web");
  const before = await json(base, "/api/quotes");
  assert(before.length >= 2, "Missing seeded quotes");
  const imported = await json(base, "/api/quotes/import", [{ text: marker, author: "Smoke suite" }]);
  assert.equal(imported.added, 1);
  assert.equal(imported.ids.length, 1);
  assert(Number.isSafeInteger(imported.ids[0]));
  try {
    assert((await json(base, "/api/quotes")).some(item => item.id === imported.ids[0] && item.text === marker));
    await json(base, "/api/quotes/import", [{ text: "Missing author" }], 400);
    assert.equal((await json(base, "/api/quotes")).length, before.length + 1);
  } finally {
    // This demo has no DELETE endpoint; remove only the row created by this run.
    await psql(container, "postgres", "quotesdb", `DELETE FROM quotes WHERE id = ${imported.ids[0]}`);
  }
}

async function recipes({ endpoint }) {
  const base = endpoint("frontend");
  assert((await json(base, "/api/recipes")).length >= 3, "Missing seeded recipes");
  const data = {
    title: marker, description: "Smoke suite recipe", ingredients: ["Smoke test milk"],
    instructions: "Mix.", cookTimeMinutes: 1, category: "Test",
  };
  const created = await json(base, "/api/recipes", data, 201);
  const path = `/api/recipes/${created.id}`;
  try {
    assert.equal((await json(base, path)).title, marker);
    assert((await json(base, `/api/recipes/search?q=${marker.toUpperCase()}`)).some(item => item.id === created.id));
    assert((await json(base, "/api/recipes/search?q=smoke%20test%20milk")).some(item => item.id === created.id));
    await json(base, path, { ...data, title: `${marker}-updated` }, 200, "PUT");
    assert.equal((await json(base, path)).title, `${marker}-updated`);
  } finally {
    await json(base, path, undefined, 204, "DELETE");
  }
  await request(`${base}${path}`, {}, 404);
}

async function events({ endpoint }) {
  const producer = endpoint("producer");
  const consumer = endpoint("consumer");
  const dashboard = endpoint("dashboard");
  assert.equal((await json(producer, "/api/sensors")).length, 5);
  await eventually(async () => (await json(dashboard, "/api/readings")).length > 0,
    "Kafka dashboard subscriptions and initial sensor readings");
  const published = await json(producer, "/api/events", {
    sensorId: marker, temperature: 42.5, humidity: 95,
    location: "Smoke suite", timestamp: new Date().toISOString(),
  });
  assert.equal(published.status, "published");
  await eventually(async () => (await json(consumer, "/api/aggregates"))[marker]?.readingCount > 0,
    "the Python consumer to aggregate the test event");
  await eventually(async () => (await json(dashboard, "/api/readings")).some(item => item.sensorId === marker),
    "the Node dashboard to receive the test reading");
  await eventually(async () => (await json(dashboard, "/api/alerts")).some(item =>
    item.sensorId === marker && item.reason.includes("High temperature") && item.reason.includes("High humidity")),
  "the Python anomaly alert to reach the Node dashboard");
}

async function wiki({ endpoint }) {
  const base = endpoint("wiki");
  const response = await request(`${base}/new`, {
    method: "POST", body: new URLSearchParams({ title: marker, content: "**Original smoke content**" }),
  }, 302);
  const path = response.headers.get("location");
  assert(path?.startsWith("/page/"));
  assert.match(await (await request(`${base}${path}`)).text(), /<strong>Original smoke content<\/strong>/);
  await request(`${base}${path}`, {
    method: "POST", body: new URLSearchParams({ content: "**Updated smoke content**" }),
  }, 302);
  const updated = await (await request(`${base}${path}`)).text();
  assert.match(updated, /<strong>Updated smoke content<\/strong>/);
  assert(!updated.includes("<strong>Original smoke content</strong>"), "Wiki served stale Redis content");
}

async function polls({ endpoint, container }) {
  const base = endpoint("polls");
  const form = await request(`${base}/create/`);
  const html = await form.text();
  const csrf = html.match(/name="csrfmiddlewaretoken" value="([^"]+)"/)?.[1];
  assert(csrf, "Missing Django CSRF token");
  const headers = {
    Cookie: form.headers.getSetCookie().map(cookie => cookie.split(";")[0]).join("; "),
    Referer: `${base}/create/`,
  };
  const created = await request(`${base}/create/`, {
    method: "POST", headers,
    body: new URLSearchParams({
      csrfmiddlewaretoken: csrf, question: marker, choice_1: "First", choice_2: "Second",
    }),
  }, 302);
  const path = created.headers.get("location");
  assert.match(path ?? "", /^\/poll\/\d+\/$/);
  const detail = await (await request(`${base}${path}`)).text();
  const vote = detail.match(/hx-post="([^"]+\/vote\/[^"]+)"/)?.[1];
  assert(vote, "Missing HTMX vote action");
  const result = await request(`${base}${vote}`, {
    method: "POST", headers,
    body: new URLSearchParams({ csrfmiddlewaretoken: csrf }),
  });
  assert.match(await result.text(), /1 votes?/);
  assert.match(await (await request(`${base}${path}`)).text(), /1 votes?/);
  const vendor = await container("polls", "python", "manage.py", "shell", "-c",
    "from django.db import connection; print(connection.vendor)");
  assert.match(vendor, /postgresql/, "Django silently used SQLite");
  await container("polls", "python", "manage.py", "test", "polls", "--noinput");
}

async function bookmarks({ endpoint, container }) {
  const base = endpoint("frontend");
  assert((await json(base, "/api/bookmarks")).length >= 3, "Missing seeded bookmarks");
  const created = await json(base, "/api/bookmarks", {
    url: `https://example.com/${marker}`, title: marker, tags: "smoke,validation",
  }, 201);
  assert(Number.isSafeInteger(created.id));
  try {
    assert((await json(base, `/api/bookmarks/search?q=${marker}`)).some(item => item.id === created.id));
    const stored = await psql(container, "pg", "bookmarksdb",
      `SELECT title FROM bookmarks WHERE id = ${created.id}`);
    assert.equal(stored.trim(), marker, "Go silently used in-memory storage");
  } finally {
    await json(base, `/api/bookmarks/${created.id}`, undefined, 204, "DELETE");
  }
  assert(!(await json(base, "/api/bookmarks")).some(item => item.id === created.id));
}

async function notes({ endpoint }) {
  const base = endpoint("api");
  const created = await json(base, "/api/notes", { title: marker, content: "Smoke suite" });
  const path = `/api/notes/${created.id}`;
  try {
    assert.equal((await json(base, path)).title, marker);
    assert((await json(base, "/api/notes")).some(item => item.id === created.id));
  } finally {
    await request(`${base}${path}`, { method: "DELETE" });
  }
  await request(`${base}${path}`, {}, 404);
}

async function standalone() {
  const cwd = join(root, "samples", "standalone-dashboard");
  const existing = await command("docker", ["container", "ls", "--all", "--format", "{{.Names}}",
    "--filter", "name=^/aspire-dashboard$"]);
  assert(!existing.trim(), "The standalone aspire-dashboard container already exists; it was not changed.");
  const compose = (...args) => command("docker", [
    "compose", "--project-name", `aspire-smoke-${process.pid}`, ...args,
  ], cwd);
  let failure;
  try {
    await compose("up", "--build", "--detach", "--wait", "--wait-timeout", "300");
    assert.match(await (await request("http://localhost:18888", { redirect: "follow" })).text(), /Aspire/);
    for (const worker of ["node-worker", "python-worker", "go-worker"]) {
      await eventually(async () => /job \d+/i.test(await compose("logs", "--no-color", "--tail", "30", worker)),
        `${worker} to process jobs`, 60_000);
    }
    const output = (await compose("ps", "--all", "--format", "json")).trim();
    const services = output.startsWith("[") ? JSON.parse(output) : output.split(/\r?\n/).map(line => JSON.parse(line));
    assert.equal(services.length, 4);
    for (const service of services) assert.equal(service.State, "running", `${service.Service} exited`);
  } catch (error) {
    failure = error;
    try {
      console.error(await compose("logs", "--no-color", "--tail", "20"));
    } catch (diagnosticError) {
      console.error(`Could not read Compose logs: ${diagnosticError.message}`);
    }
  }
  try {
    await compose("down");
  } catch (error) {
    throw new Error(`${failure ? `${failure.message}\n` : ""}Cleanup failed: ${error.message}`);
  }
  if (failure) throw failure;
}

const names = process.argv.slice(2);
if (names.includes("--list") || names.includes("--help")) {
  if (names.includes("--help")) {
    console.log("Usage: node --use-system-ca scripts/smoke-samples.mjs [sample ...]\n");
  }
  console.log(samples.map(([name]) => name).join("\n"));
} else {
  for (const name of names) assert(samples.some(sample => sample[0] === name), `Unknown sample: ${name}`);
  const selected = names.length ? samples.filter(sample => names.includes(sample[0])) : samples;
  if (selected.some(([name]) => name === "python-fastapi-docker")) await trustDevelopmentCertificate();
  const failed = [];
  for (const sample of selected) {
    const start = Date.now();
    console.log(`[RUN] ${sample[0]}`);
    try {
      if (sample[1]) await appHostSample(sample);
      else await sample[3]();
      console.log(`[PASS] ${sample[0]} (${Math.round((Date.now() - start) / 1000)}s)`);
    } catch (error) {
      failed.push(sample[0]);
      console.error(`[FAIL] ${sample[0]}: ${error.message}`);
    }
  }
  console.log(`\n${selected.length - failed.length}/${selected.length} samples passed.`);
  if (failed.length) {
    console.error(`Failed: ${failed.join(", ")}`);
    process.exitCode = 1;
  }
}
