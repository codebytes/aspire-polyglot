"""Shared Celery configuration and task definitions."""
import os
import time
import random
from celery import Celery

# Get Redis connection from Aspire
redis_conn = os.getenv("ConnectionStrings__broker", "redis://localhost:6379")

# Create Celery app
celery_app = Celery(
    "reports",
    broker=redis_conn,
    backend=redis_conn
)

# Configure Celery
celery_app.conf.update(
    task_serializer="json",
    accept_content=["json"],
    result_serializer="json",
    timezone="UTC",
    enable_utc=True,
    task_track_started=True,
)


@celery_app.task(bind=True, name="generate_report")
def generate_report(self, title: str, report_type: str):
    """
    Simulate report generation with random processing time.
    
    Args:
        title: Report title
        report_type: Type of report (sales, inventory, users)
    
    Returns:
        dict: Report summary with generated statistics
    """
    # Update task state to running
    self.update_state(state="RUNNING", meta={"status": "Generating report..."})
    
    # Simulate processing time (5-15 seconds)
    processing_time = random.randint(5, 15)
    time.sleep(processing_time)
    
    # Generate fake statistics based on report type
    if report_type == "sales":
        result = {
            "title": title,
            "type": report_type,
            "total_sales": random.randint(10000, 50000),
            "orders": random.randint(100, 500),
            "average_order_value": round(random.uniform(50, 200), 2),
            "top_product": f"Product-{random.randint(1, 100)}",
            "processing_time": processing_time
        }
    elif report_type == "inventory":
        result = {
            "title": title,
            "type": report_type,
            "total_items": random.randint(500, 2000),
            "low_stock_items": random.randint(10, 50),
            "out_of_stock": random.randint(0, 10),
            "total_value": random.randint(50000, 200000),
            "processing_time": processing_time
        }
    elif report_type == "users":
        result = {
            "title": title,
            "type": report_type,
            "total_users": random.randint(1000, 5000),
            "active_users": random.randint(500, 2000),
            "new_signups": random.randint(50, 200),
            "churn_rate": round(random.uniform(1, 10), 2),
            "processing_time": processing_time
        }
    else:
        result = {
            "title": title,
            "type": report_type,
            "status": "Unknown report type",
            "processing_time": processing_time
        }
    
    return result
