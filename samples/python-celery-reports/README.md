# Python Celery Reports - Background Report Generator

A demonstration of **background task processing** using FastAPI, Celery, and Redis, orchestrated by .NET Aspire.

## Architecture

This sample showcases the **background worker pattern** for long-running tasks:

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   FastAPI   │────▶│    Redis    │◀────│   Celery    │
│     API     │     │   Broker    │     │   Worker    │
└─────────────┘     └─────────────┘     └─────────────┘
```

### Components

1. **FastAPI API Server** (`api`) - Web interface and REST API
   - Accepts report generation requests
   - Enqueues tasks to Celery via Redis
   - Polls task status and retrieves results
   - Serves interactive HTML UI

2. **Celery Worker** (`worker`) - Background task processor
   - Consumes tasks from Redis queue
   - Simulates report generation (5-15 seconds)
   - Returns results with fake statistics
   - Runs independently from the API

3. **Redis** (`broker`) - Message broker and result backend
   - Queues tasks between API and worker
   - Stores task results and status
   - Managed by Aspire

## Features

- **Asynchronous Processing**: Submit reports and check status later
- **Multiple Report Types**: Sales, Inventory, and User reports
- **Real-time Status**: Track pending, running, completed, and failed tasks
- **Interactive UI**: Web interface to submit and monitor reports
- **Aspire Orchestration**: Both API and worker visible in Aspire dashboard
- **Service Discovery**: Worker and API connect to Redis via Aspire connection strings

## Running with Aspire

```bash
aspire run
```

The Aspire dashboard will show:
- **broker** - Redis instance (port 6379)
- **api** - FastAPI application with external HTTP endpoint
- **worker** - Celery worker process (background)

Open the API's external endpoint to access the web UI.

## API Endpoints

### Submit Report
```bash
POST /api/reports
Content-Type: application/json

{
  "title": "Q4 2024 Sales Report",
  "type": "sales"  # sales, inventory, or users
}

Response:
{
  "task_id": "abc-123-xyz",
  "status": "pending",
  "message": "Report generation started"
}
```

### Check Status
```bash
GET /api/reports/{task_id}

Response:
{
  "task_id": "abc-123-xyz",
  "title": "Q4 2024 Sales Report",
  "type": "sales",
  "status": "completed",
  "result": {
    "title": "Q4 2024 Sales Report",
    "type": "sales",
    "total_sales": 35420,
    "orders": 287,
    "average_order_value": 123.45,
    "top_product": "Product-42",
    "processing_time": 8
  }
}
```

### List All Reports
```bash
GET /api/reports

Response: Array of report statuses
```

## How It Works

### 1. Task Submission (FastAPI)
When you submit a report via `POST /api/reports`:
- FastAPI creates a Celery task
- Task is enqueued in Redis
- Task ID is returned immediately
- API doesn't wait for completion

### 2. Task Processing (Celery Worker)
The Celery worker:
- Polls Redis for pending tasks
- Picks up the report generation task
- Updates task state to "RUNNING"
- Simulates processing (5-15 seconds)
- Generates fake statistics
- Stores result in Redis

### 3. Status Retrieval (FastAPI)
When you check status via `GET /api/reports/{task_id}`:
- FastAPI queries Celery result backend (Redis)
- Returns current status (pending/running/completed/failed)
- If completed, includes the generated report data

## Aspire Integration

### Connection String Injection
Both the API and worker receive the Redis connection string via environment variable:
```csharp
var redis = builder.AddRedis("broker");
var api = builder.AddPythonApp("api", "../src", "main.py")
    .WithReference(redis);  // Injects ConnectionStrings__broker
```

### Python Code Retrieval
```python
# src/tasks.py
redis_conn = os.getenv("ConnectionStrings__broker", "redis://localhost:6379")
celery_app = Celery("reports", broker=redis_conn, backend=redis_conn)
```

### Dashboard Visibility
All three services appear in the Aspire dashboard:
- Monitor API logs and HTTP traffic
- View worker logs and task processing
- See Redis connection metrics

## Development

### Run API Locally
```bash
cd src
pip install -r requirements.txt
export ConnectionStrings__broker="redis://localhost:6379"
python main.py
```

### Run Worker Locally
```bash
cd src
export ConnectionStrings__broker="redis://localhost:6379"
celery -A worker worker --loglevel=info
```

## Use Cases

This pattern is ideal for:
- **Report Generation**: Long-running data aggregation and PDF generation
- **Email Sending**: Batch email processing without blocking the API
- **Image Processing**: Thumbnail generation, resizing, filtering
- **Data Import/Export**: CSV parsing, data transformation
- **Scheduled Jobs**: Periodic cleanup, backups, notifications
- **Machine Learning**: Model training, batch predictions

## Key Takeaways

1. **Decoupled Processing**: API and worker are independent processes
2. **Scalability**: Add more workers to handle increased load
3. **Resilience**: If worker crashes, tasks remain in queue
4. **User Experience**: Users don't wait for long-running operations
5. **Observability**: Aspire dashboard shows all components and their health

## Related Patterns

- **python-etl-pipeline**: Scheduled background jobs with APScheduler
- **multi-lang-health-monitor**: Service-to-service communication
- **express-url-shortener**: Simple API with Redis caching
