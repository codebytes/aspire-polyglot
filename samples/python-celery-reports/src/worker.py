"""Celery worker process."""
from tasks import celery_app

# Import tasks to register them
from tasks import generate_report

if __name__ == "__main__":
    # Start the Celery worker
    # Run with: celery -A worker worker --loglevel=info
    celery_app.start()
