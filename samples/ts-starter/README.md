# TypeScript Starter — Express + React with Aspire

A minimal starter sample scaffolded from the **official Aspire TypeScript template** (`aspire-ts-starter`). The AppHost itself is TypeScript — not C#, not Python — and uses the generally available Aspire 13.6 AppHost layout.

## What It Demonstrates

- **TypeScript AppHost** (`apphost.mts`) — the orchestrator is written in TypeScript
- **Express API** — weather forecast endpoint with OpenTelemetry instrumentation
- **React + Vite frontend** — single-page app consuming the API
- **Aspire service discovery** — `withReference()` wires the frontend to the API automatically
- **Aspire dashboard** — distributed traces, logs, and metrics out of the box

## Architecture

```
┌─────────────────────────────────────────────────┐
│              Aspire AppHost (TypeScript)         │
│                  apphost.mts                     │
├─────────────────────┬───────────────────────────┤
│                     │                           │
│   ┌─────────────┐   │   ┌───────────────────┐   │
│   │  Express API│   │   │  React + Vite     │   │
│   │  (./api)    │◄──┼───│  (./frontend)     │   │
│   │  Port: auto │   │   │  Port: auto       │   │
│   └─────────────┘   │   └───────────────────┘   │
│                     │                           │
└─────────────────────┴───────────────────────────┘
```

## Prerequisites

- [.NET Aspire CLI](https://learn.microsoft.com/dotnet/aspire/) (`aspire` command)
- .NET 10 SDK
- [Node.js](https://nodejs.org/) 20.19+ or 22.13+ or 24+
- npm

## Getting Started

```bash
# Install dependencies
npm install
cd api && npm install && cd ..
cd frontend && npm install && cd ..

# Restore Aspire SDK code
aspire restore

# Start the app
aspire run
```

The Aspire dashboard URL will appear in the terminal. Open it to see the API and frontend resources, their logs, and distributed traces.

For a worktree, use `aspire start --apphost apphost.mts --isolated`.
Open `frontend`, refresh the five-day forecast, and switch between Fahrenheit
and Celsius. Stop the sample with `aspire stop --apphost apphost.mts`.

The API and browser tracing use the dynamically allocated OTLP/HTTP listener.
The API preloads its instrumentation before Express starts. HTTP/protobuf also
avoids the Node 25 gRPC TLS restriction on IP-address collector endpoints.

## Build checks

After installing dependencies in all three directories:

```bash
npm run build
npm run typecheck:api
npm --prefix frontend run build
npm --prefix frontend run lint
```

The API executes TypeScript through the Node/tsx runtime, so its type-check
configuration allows `.ts` imports without emitting JavaScript. The initial
weather request is canceled on component cleanup; only the refresh button
starts a synchronous loading-state transition.

## Project Structure

```
ts-starter/
├── apphost.mts         # TypeScript AppHost — orchestrates all services
├── .aspire/modules/    # Generated Aspire TypeScript SDK
├── aspire.config.json  # Aspire configuration
├── package.json        # Root dependencies (TypeScript, tsx, eslint)
├── tsconfig.apphost.json # AppHost TypeScript config
├── api/                # Express API service
│   ├── src/
│   │   ├── index.ts            # Express server + weather endpoint
│   │   └── instrumentation.ts  # OpenTelemetry setup
│   └── package.json
└── frontend/           # React + Vite frontend
    ├── src/
    │   ├── App.tsx     # Main React component
    │   └── main.tsx    # Entry point
    └── package.json
```

## Key Files

### `apphost.mts`

```typescript
const app = await builder
    .addNodeApp("app", "./api", "src/index.ts")
    .withRunScript("start")
    .withOtlpExporter({ protocol: OtlpProtocol.HttpProtobuf })
    .publishAsPackageScript({ scriptName: "start" })
    .withHttpEndpoint({ env: "PORT" })
    .withExternalHttpEndpoints();

const frontend = await builder
    .addViteApp("frontend", "./frontend")
    .withReference(app)
    .waitFor(app);
```

- `addNodeApp` + `withRunScript("start")` — runs the API's npm start script through `tsx`, including on Node 20
- `publishAsPackageScript` — uses the same start script in the published container, with `tsx` installed as a runtime dependency
- `addViteApp` — runs a Vite dev server
- `withReference` — injects the API URL into the frontend
- `waitFor` — ensures the API starts before the frontend

## Technologies

- **AppHost:** TypeScript + Aspire SDK
- **API:** Express, OpenTelemetry
- **Frontend:** React 19, Vite, TypeScript
- **Tooling:** tsx, ESLint, TypeScript 6

## Learn More

- [Aspire documentation](https://learn.microsoft.com/dotnet/aspire/)
- [TypeScript AppHost support](https://learn.microsoft.com/dotnet/aspire/get-started/build-aspire-apps-with-nodejs)
- [Express.js](https://expressjs.com/)
- [Vite](https://vitejs.dev/)
