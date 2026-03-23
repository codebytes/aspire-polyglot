"""
Django settings for pollsite project.
"""

import os
from pathlib import Path
from urllib.parse import urlparse

# Build paths inside the project like this: BASE_DIR / 'subdir'.
BASE_DIR = Path(__file__).resolve().parent.parent

# SECURITY WARNING: keep the secret key used in production secret!
SECRET_KEY = 'django-insecure-sample-key-change-in-production'

# SECURITY WARNING: don't run with debug turned on in production!
DEBUG = True

ALLOWED_HOSTS = ['*']

# Application definition
INSTALLED_APPS = [
    'polls',
    'django.contrib.contenttypes',
    'django.contrib.staticfiles',
]

MIDDLEWARE = [
    'django.middleware.security.SecurityMiddleware',
    'whitenoise.middleware.WhiteNoiseMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
]

ROOT_URLCONF = 'pollsite.urls'

TEMPLATES = [
    {
        'BACKEND': 'django.template.backends.django.DjangoTemplates',
        'DIRS': [],
        'APP_DIRS': True,
        'OPTIONS': {
            'context_processors': [
                'django.template.context_processors.debug',
                'django.template.context_processors.request',
            ],
        },
    },
]

WSGI_APPLICATION = 'pollsite.wsgi.application'

# Database — use PostgreSQL when Aspire provides a connection string, else SQLite
def _parse_aspire_pg_conn(conn_str):
    """Parse Aspire PostgreSQL connection string into Django database config.
    
    Aspire can inject either:
      - A URI: postgresql://user:pass@host:port/dbname
      - A .NET-style string: Host=...;Port=...;Username=...;Password=...;Database=...
    """
    if conn_str.startswith("postgresql://") or conn_str.startswith("postgres://"):
        parsed = urlparse(conn_str)
        return {
            'ENGINE': 'django.db.backends.postgresql',
            'NAME': parsed.path.lstrip('/'),
            'USER': parsed.username or '',
            'PASSWORD': parsed.password or '',
            'HOST': parsed.hostname or 'localhost',
            'PORT': str(parsed.port or 5432),
        }
    # .NET-style key=value pairs
    parts = {}
    for segment in conn_str.split(';'):
        segment = segment.strip()
        if '=' in segment:
            key, value = segment.split('=', 1)
            parts[key.strip().lower()] = value.strip()
    return {
        'ENGINE': 'django.db.backends.postgresql',
        'NAME': parts.get('database', 'pollsdb'),
        'USER': parts.get('username', parts.get('user id', '')),
        'PASSWORD': parts.get('password', ''),
        'HOST': parts.get('host', 'localhost'),
        'PORT': parts.get('port', '5432'),
    }

_pg_conn_str = os.environ.get('ConnectionStrings__pollsdb')

if _pg_conn_str:
    DATABASES = {
        'default': _parse_aspire_pg_conn(_pg_conn_str)
    }
else:
    DATABASES = {
        'default': {
            'ENGINE': 'django.db.backends.sqlite3',
            'NAME': BASE_DIR / 'db.sqlite3',
        }
    }

# Internationalization
LANGUAGE_CODE = 'en-us'
TIME_ZONE = 'UTC'
USE_I18N = True
USE_TZ = True

# Static files (CSS, JavaScript, Images)
STATIC_URL = 'static/'
STATIC_ROOT = BASE_DIR / 'staticfiles'

# Default primary key field type
DEFAULT_AUTO_FIELD = 'django.db.models.BigAutoField'

# CSRF trusted origins (for Aspire reverse proxy)
CSRF_TRUSTED_ORIGINS = ['http://localhost:*', 'https://localhost:*']
