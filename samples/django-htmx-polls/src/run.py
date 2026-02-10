import os
import sys

# Set Django settings module
os.environ.setdefault("DJANGO_SETTINGS_MODULE", "pollsite.settings")

# Import Django's WSGI application
from django.core.wsgi import get_wsgi_application
from waitress import serve

app = get_wsgi_application()

# Get port from environment (Aspire sets this)
port = int(os.environ.get("PORT", 8000))
print(f"Starting Django polls app on port {port}")
serve(app, host="0.0.0.0", port=port)
