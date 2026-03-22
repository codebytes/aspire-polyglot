# TypeScript Starter — Express + React with Aspire

A minimal starter sample scaffolded from the **official Aspire TypeScript template** (`aspire-ts-starter`). The AppHost itself is TypeScript — not C#, not Python — making this a first-class polyglot citizen in Aspire 13.2+.

## What It Demonstrates

- **TypeScript AppHost** (`apphost.ts`) — the orchestrator is written in TypeScript
- **Express API** — weather forecast endpoint with OpenTelemetry instrumentation
- **React + Vite frontend** — single-page app consuming the API
- **Aspire service discovery** — `withReference()` wires the frontend to the API automatically
- **Aspire dashboard** — distributed traces, logs, and metrics out of the box

## Architecture

```
┌─────────────────────────────────────────────────┐
│              Aspire AppHost (TypeScript)         │
│                  apphost.ts                      │
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

## Project Structure

```
ts-starter/
├── apphost.ts          # TypeScript AppHost — orchestrates all services
├── aspire.config.json  # Aspire configuration
├── package.json        # Root dependencies (TypeScript, tsx, eslint)
├── tsconfig.json       # TypeScript config
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

### `apphost.ts`

```typescript
const app = await builder
    .addNodeApp("app", "./api", "src/index.ts")
    .withHttpEndpoint({ env: "PORT" })
    .withExternalHttpEndpoints();

const frontend = await builder
    .addViteApp("frontend", "./frontend")
    .withReference(app)
    .waitFor(app);
```

- `addNodeApp` — runs a Node.js app with `tsx`
- `addViteApp` — runs a Vite dev server
- `withReference` — injects the API URL into the frontend
- `waitFor` — ensures the API starts before the frontend

## Technologies

- **AppHost:** TypeScript + Aspire SDK
- **API:** Express, OpenTelemetry
- **Frontend:** React 19, Vite, TypeScript
- **Tooling:** tsx, ESLint, TypeScript 5.9

## Learn More

- [Aspire documentation](https://learn.microsoft.com/dotnet/aspire/)
- [TypeScript AppHost support](https://learn.microsoft.com/dotnet/aspire/get-started/build-aspire-apps-with-nodejs)
- [Express.js](https://expressjs.com/)
- [Vite](https://vitejs.dev/)
