"""Django settings for the polls demo. Local development only."""
import os
from pathlib import Path
from urllib.parse import unquote, urlparse

BASE_DIR = Path(__file__).resolve().parent.parent

SECRET_KEY = "django-insecure-local-demo-only"
DEBUG = True
ALLOWED_HOSTS = ["*"]

INSTALLED_APPS = ["polls"]

MIDDLEWARE = [
    "django.middleware.security.SecurityMiddleware",
    "django.middleware.common.CommonMiddleware",
    "django.middleware.csrf.CsrfViewMiddleware",
    "django.middleware.clickjacking.XFrameOptionsMiddleware",
]

ROOT_URLCONF = "pollsite.urls"
WSGI_APPLICATION = "pollsite.wsgi.application"

TEMPLATES = [
    {
        "BACKEND": "django.template.backends.django.DjangoTemplates",
        "APP_DIRS": True,
        "OPTIONS": {"context_processors": ["django.template.context_processors.request"]},
    },
]

# Aspire injects POLLSDB_URI (postgresql://user:password@host:port/pollsdb).
# Without it, for example under `manage.py test`, Django uses a local SQLite file.
if uri := os.environ.get("POLLSDB_URI"):
    url = urlparse(uri)
    DATABASES = {
        "default": {
            "ENGINE": "django.db.backends.postgresql",
            "NAME": url.path.lstrip("/"),
            "USER": unquote(url.username or ""),
            "PASSWORD": unquote(url.password or ""),
            "HOST": url.hostname,
            "PORT": url.port or 5432,
        }
    }
else:
    DATABASES = {"default": {"ENGINE": "django.db.backends.sqlite3", "NAME": BASE_DIR / "db.sqlite3"}}

# Leave logging to the root logger configured in run.py (console + OpenTelemetry).
LOGGING_CONFIG = None

USE_TZ = True
DEFAULT_AUTO_FIELD = "django.db.models.BigAutoField"
