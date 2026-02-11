# Spring Boot + PostgreSQL

A Java Spring Boot REST API with PostgreSQL, orchestrated by Aspire via Dockerfile.

## Tech Stack
- **Backend**: Java 21 + Spring Boot 3.4
- **Database**: PostgreSQL + pgAdmin
- **AppHost**: Standalone `apphost.cs` with `AddDockerfile`

## Running

```bash
cd samples/spring-boot-postgres
aspire run
```

## What It Demonstrates
- `AddDockerfile` for Java applications
- `AddPostgres` with pgAdmin
- Connection string injection via environment variables
- Multi-stage Docker build for Spring Boot

## API Endpoints

Once running, the API is available at the displayed endpoint:

- `GET /api/notes` - List all notes
- `POST /api/notes` - Create a note (JSON body: `{"title": "...", "content": "..."}`)
- `GET /api/notes/{id}` - Get a specific note
- `DELETE /api/notes/{id}` - Delete a note

## Testing

Example using curl:

```bash
# Create a note
curl -X POST http://localhost:8080/api/notes \
  -H "Content-Type: application/json" \
  -d '{"title":"Hello","content":"My first note"}'

# List all notes
curl http://localhost:8080/api/notes

# Get a specific note
curl http://localhost:8080/api/notes/1

# Delete a note
curl -X DELETE http://localhost:8080/api/notes/1
```

## Database Access

pgAdmin is automatically configured and accessible through the Aspire dashboard. Use the connection details provided in the dashboard to connect to the PostgreSQL instance.
