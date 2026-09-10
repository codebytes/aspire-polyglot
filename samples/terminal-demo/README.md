# Interactive terminals (local shell + Node.js REPL)

A single-file C# AppHost running two local, interactive processes with
`AddExecutable(...).WithTerminal()`. Type into either process directly in the
Aspire dashboard or attach from the Aspire CLI. No Docker, application projects,
or npm packages are needed.

Inspired by the upstream [Aspire Terminals playground](https://github.com/microsoft/aspire/tree/main/playground/Terminals)
and [TerminalsJs playground](https://github.com/microsoft/aspire/tree/main/playground/TerminalsJs).

> **Experimental:** `WithTerminal()` is experimental in Aspire 13.5.3. The AppHost
> explicitly acknowledges `ASPIRETERMINAL001`; the API may change in future releases.

## Prerequisites

- [Aspire CLI](https://aspire.dev/get-started/install-cli/) 13.5.3 and .NET 10 SDK.
- [Node.js](https://nodejs.org/) 24 or later, available as `node` on `PATH`.
- `/bin/bash` on macOS/Linux, or `cmd.exe` on Windows.

## Running

```bash
cd samples/terminal-demo
aspire run
```

Open the dashboard URL printed by Aspire. The launch profiles use dynamically
allocated loopback ports, so this sample does not reserve another sample's ports.

| Resource | Process | Try in its terminal |
|----------|---------|---------------------|
| `shell` | Bash on macOS/Linux; Command Prompt on Windows | `echo Hello from Aspire` |
| `node-repl` | Node.js's built-in interactive JavaScript REPL | `6 * 7`, `process.version`, `process.stdin.isTTY` |

Open **Console logs** for either resource. A running terminal-enabled resource
defaults to the **Terminal** view; use the toolbar's options menu to switch
between **Terminal** and **Console logs**. Terminal input goes to the running
process, unlike the read-only console log stream.

For a short demo, run the shell's `echo` command, then evaluate `6 * 7` in
`node-repl`. `process.stdin.isTTY` returns `true`, showing that Node is attached to
a real pseudo-terminal rather than redirected input. `.help` lists Node's REPL
commands.

## Attach from another terminal

In a second terminal, from the same sample directory:

```bash
aspire terminal ps
aspire terminal attach node-repl
```

Press **Ctrl+B**, then **D** to detach without stopping the process. To attach to
the shell instead:

```bash
aspire terminal attach shell
```

The sample's `aspire.config.json` enables `features.terminalCommandsEnabled`
locally; no global feature-flag change is needed. The dashboard and CLI can
attach to the same session at once and see the same input and output.

`exit` in the shell or `.exit` in Node ends that resource, not the AppHost. Use
the resource's **Start** action in the dashboard to begin a new session. Stop the
demo with **Ctrl+C** in the terminal running `aspire run`, or run `aspire stop`
from this sample directory.

## What it demonstrates

- `AddExecutable()` orchestrates existing interactive CLIs without a language
  hosting package, entry-point script, or container.
- `WithTerminal()` gives each resource a pseudo-terminal and an Aspire-managed
  terminal host, enabling interactive stdin, line editing, and terminal output.
- A C# AppHost can orchestrate both a JavaScript REPL and a native shell.
- Shell startup profiles are skipped for a repeatable demo. Both processes start
  in this sample directory.

**Local development only:** these are real processes running with your user
permissions, not sandboxed shells. Keep the dashboard on loopback, retain its
authentication, and only enter commands you trust.

## Project layout

```text
apphost.cs            single-file C# AppHost with two terminal-enabled resources
aspire.config.json    AppHost path and local terminal CLI feature flag
apphost.run.json      HTTPS/HTTP launch profiles with dynamic loopback ports
nuget.config          package source, matching the other C# samples
```

See [Aspire's WithTerminal guide](https://aspire.dev/app-host/with-terminal/)
for terminal options, replicas, and VS Code integration.
