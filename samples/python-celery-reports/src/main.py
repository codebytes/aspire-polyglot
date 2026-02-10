"""FastAPI application for background report generation."""
import os
from typing import List, Optional
from fastapi import FastAPI, HTTPException
from fastapi.responses import HTMLResponse
from pydantic import BaseModel
from celery.result import AsyncResult
from tasks import celery_app, generate_report

app = FastAPI(title="Report Generator API")

# Store submitted reports metadata in memory
# In production, this would be in a database
reports_db = {}


class ReportRequest(BaseModel):
    """Request model for creating a report."""
    title: str
    type: str  # sales, inventory, users


class ReportStatus(BaseModel):
    """Response model for report status."""
    task_id: str
    title: str
    type: str
    status: str
    result: Optional[dict] = None
    error: Optional[str] = None


class ReportCreated(BaseModel):
    """Response model for report creation."""
    task_id: str
    status: str
    message: str


@app.get("/")
async def index():
    """Serve simple HTML page for interacting with the API."""
    html_content = """
    <!DOCTYPE html>
    <html>
    <head>
        <title>Background Report Generator</title>
        <style>
            body { font-family: Arial, sans-serif; max-width: 1000px; margin: 50px auto; padding: 20px; }
            h1 { color: #333; }
            .form-group { margin: 15px 0; }
            label { display: inline-block; width: 100px; }
            input, select { padding: 8px; width: 300px; }
            button { padding: 10px 20px; background: #007bff; color: white; border: none; cursor: pointer; margin: 10px 5px; }
            button:hover { background: #0056b3; }
            .reports { margin-top: 30px; }
            .report { border: 1px solid #ddd; padding: 15px; margin: 10px 0; border-radius: 5px; }
            .pending { background: #fff3cd; }
            .running { background: #cfe2ff; }
            .completed { background: #d1e7dd; }
            .failed { background: #f8d7da; }
            .status { font-weight: bold; }
            .result { margin-top: 10px; font-family: monospace; background: #f5f5f5; padding: 10px; border-radius: 3px; }
        </style>
    </head>
    <body>
        <h1>📊 Background Report Generator</h1>
        <p>Submit reports for background processing using Celery.</p>
        
        <div class="form-group">
            <label for="title">Title:</label>
            <input type="text" id="title" placeholder="Q4 2024 Report" />
        </div>
        <div class="form-group">
            <label for="type">Type:</label>
            <select id="type">
                <option value="sales">Sales</option>
                <option value="inventory">Inventory</option>
                <option value="users">Users</option>
            </select>
        </div>
        <button onclick="submitReport()">Generate Report</button>
        <button onclick="refreshReports()">Refresh Status</button>
        
        <div class="reports">
            <h2>Submitted Reports</h2>
            <div id="reportsList"></div>
        </div>
        
        <script>
            async function submitReport() {
                const title = document.getElementById('title').value;
                const type = document.getElementById('type').value;
                
                if (!title) {
                    alert('Please enter a title');
                    return;
                }
                
                try {
                    const response = await fetch('/api/reports', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ title, type })
                    });
                    const data = await response.json();
                    alert('Report submitted! Task ID: ' + data.task_id);
                    document.getElementById('title').value = '';
                    refreshReports();
                } catch (error) {
                    alert('Error: ' + error.message);
                }
            }
            
            async function refreshReports() {
                try {
                    const response = await fetch('/api/reports');
                    const reports = await response.json();
                    
                    const listDiv = document.getElementById('reportsList');
                    if (reports.length === 0) {
                        listDiv.innerHTML = '<p>No reports submitted yet.</p>';
                        return;
                    }
                    
                    listDiv.innerHTML = reports.map(report => `
                        <div class="report ${report.status.toLowerCase()}">
                            <h3>${report.title}</h3>
                            <p><strong>Type:</strong> ${report.type}</p>
                            <p><strong>Task ID:</strong> ${report.task_id}</p>
                            <p class="status">Status: ${report.status}</p>
                            ${report.result ? `<div class="result">${JSON.stringify(report.result, null, 2)}</div>` : ''}
                            ${report.error ? `<div class="result error">Error: ${report.error}</div>` : ''}
                        </div>
                    `).join('');
                } catch (error) {
                    console.error('Error refreshing reports:', error);
                }
            }
            
            // Auto-refresh every 3 seconds
            setInterval(refreshReports, 3000);
            
            // Initial load
            refreshReports();
        </script>
    </body>
    </html>
    """
    return HTMLResponse(content=html_content)


@app.get("/health")
async def health():
    """Health check endpoint."""
    return {"status": "healthy", "service": "report-api"}


@app.post("/api/reports", response_model=ReportCreated)
async def create_report(request: ReportRequest):
    """
    Submit a new report for background generation.
    
    Args:
        request: Report request with title and type
        
    Returns:
        Task ID and status
    """
    valid_types = ["sales", "inventory", "users"]
    if request.type not in valid_types:
        raise HTTPException(
            status_code=400,
            detail=f"Invalid report type. Must be one of: {valid_types}"
        )
    
    # Enqueue the Celery task
    task = generate_report.delay(request.title, request.type)
    
    # Store metadata
    reports_db[task.id] = {
        "title": request.title,
        "type": request.type,
        "task_id": task.id
    }
    
    return ReportCreated(
        task_id=task.id,
        status="pending",
        message="Report generation started"
    )


@app.get("/api/reports/{task_id}", response_model=ReportStatus)
async def get_report_status(task_id: str):
    """
    Get the status and result of a specific report task.
    
    Args:
        task_id: Celery task ID
        
    Returns:
        Task status and result if completed
    """
    if task_id not in reports_db:
        raise HTTPException(status_code=404, detail="Task not found")
    
    task_result = AsyncResult(task_id, app=celery_app)
    metadata = reports_db[task_id]
    
    status_map = {
        "PENDING": "pending",
        "STARTED": "running",
        "RUNNING": "running",
        "SUCCESS": "completed",
        "FAILURE": "failed"
    }
    
    status = status_map.get(task_result.state, task_result.state.lower())
    
    response = ReportStatus(
        task_id=task_id,
        title=metadata["title"],
        type=metadata["type"],
        status=status
    )
    
    if task_result.successful():
        response.result = task_result.result
    elif task_result.failed():
        response.error = str(task_result.info)
    
    return response


@app.get("/api/reports", response_model=List[ReportStatus])
async def list_reports():
    """
    List all submitted reports with their current status.
    
    Returns:
        List of all reports with status information
    """
    reports = []
    for task_id, metadata in reports_db.items():
        task_result = AsyncResult(task_id, app=celery_app)
        
        status_map = {
            "PENDING": "pending",
            "STARTED": "running",
            "RUNNING": "running",
            "SUCCESS": "completed",
            "FAILURE": "failed"
        }
        
        status = status_map.get(task_result.state, task_result.state.lower())
        
        report = ReportStatus(
            task_id=task_id,
            title=metadata["title"],
            type=metadata["type"],
            status=status
        )
        
        if task_result.successful():
            report.result = task_result.result
        elif task_result.failed():
            report.error = str(task_result.info)
        
        reports.append(report)
    
    # Sort by most recent first
    reports.reverse()
    return reports


if __name__ == "__main__":
    import uvicorn
    port = int(os.getenv("PORT", 8000))
    uvicorn.run(app, host="0.0.0.0", port=port)
