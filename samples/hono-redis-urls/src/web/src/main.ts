import './style.css';

interface ShortUrl {
  code: string;
  url: string;
  clicks: number;
  createdAt: string;
}

const app = document.querySelector<HTMLDivElement>('#app')!;

app.innerHTML = `
  <main class="wrap">
    <h1>🔗 Aspire URL Shortener</h1>
    <p class="sub">Hono API · Redis · Vite — orchestrated by a single-file TypeScript AppHost.</p>
    <form id="form">
      <input id="url" type="url" placeholder="https://example.com/very/long/link" required />
      <button type="submit">Shorten</button>
    </form>
    <p id="error" class="error"></p>
    <ul id="list" class="list"></ul>
  </main>
`;

const form = app.querySelector<HTMLFormElement>('#form')!;
const input = app.querySelector<HTMLInputElement>('#url')!;
const errorEl = app.querySelector<HTMLParagraphElement>('#error')!;
const list = app.querySelector<HTMLUListElement>('#list')!;

function shortLink(code: string): string {
  return `${window.location.origin}/r/${code}`;
}

function render(urls: ShortUrl[]): void {
  if (urls.length === 0) {
    list.innerHTML = '<li class="empty">No links yet — shorten one above.</li>';
    return;
  }
  list.innerHTML = urls
    .map(
      (u) => `
      <li>
        <a class="code" href="/r/${u.code}" target="_blank" rel="noreferrer">${shortLink(u.code)}</a>
        <span class="target" title="${u.url}">${u.url}</span>
        <span class="clicks">${u.clicks} clicks</span>
      </li>`
    )
    .join('');
}

async function load(): Promise<void> {
  const res = await fetch('/api/urls');
  const data = (await res.json()) as { urls: ShortUrl[] };
  render(data.urls);
}

form.addEventListener('submit', async (e) => {
  e.preventDefault();
  errorEl.textContent = '';
  const res = await fetch('/api/shorten', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ url: input.value }),
  });
  if (!res.ok) {
    const data = (await res.json().catch(() => ({}))) as { error?: string };
    errorEl.textContent = data.error ?? 'Something went wrong.';
    return;
  }
  input.value = '';
  await load();
});

load();
