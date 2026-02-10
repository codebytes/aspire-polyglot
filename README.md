# Polyglot Aspire

Polyglot samples for .NET Aspire 13 — demonstrating service orchestration across TypeScript, Python, C#, and mixed-language applications.

**Quick Start:** `cd samples/<sample> && aspire run`

**Prerequisites:** [Aspire CLI](https://aspire.dev/get-started/install-cli/), [Docker](https://docs.docker.com/get-docker/)

## Samples

### TypeScript

| Sample | Description |
|--------|-------------|
| [node-express-redis](./samples/node-express-redis) | Express + Redis + Vite frontend — visit counter with real-time updates |
| [vite-react-fastapi-ts](./samples/vite-react-fastapi-ts) | React + API fullstack with TypeScript frontend |

### Python

| Sample | Description |
|--------|-------------|
| [python-fastapi-postgres](./samples/python-fastapi-postgres) | FastAPI + PostgreSQL + pgAdmin — async CRUD API |
| [python-script](./samples/python-script) | Minimal Python script — simplest possible Aspire app |
| [python-openai-agent](./samples/python-openai-agent) | OpenAI chat agent with streaming responses |

### C# / .NET

| Sample | Description |
|--------|-------------|
| [vite-csharp-postgres](./samples/vite-csharp-postgres) | Minimal API + PostgreSQL + Vite React frontend |

### Mixed / Polyglot

| Sample | Description |
|--------|-------------|
| [polyglot-task-queue](./samples/polyglot-task-queue) | Multi-language task queue — Python, C#, Node.js workers with RabbitMQ |
| [rag-document-qa-svelte](./samples/rag-document-qa-svelte) | RAG document Q&A — Svelte frontend, Python backend, Qdrant vectors |

## Slides

Presentation slides are in the [slides/](./slides/) directory, built with [Marp](https://marp.app/).

## Learn More

- [Aspire Documentation](https://aspire.dev/docs/)
- [Aspire VS Code Extension](https://marketplace.visualstudio.com/items?itemName=microsoft-aspire.aspire-vscode)
- [Aspire GitHub](https://github.com/dotnet/aspire)
- [David Fowler's Aspire 13 Samples](https://github.com/davidfowl/aspire-13-samples)
