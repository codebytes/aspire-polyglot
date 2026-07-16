#!/usr/bin/env python
"""Django's command-line utility for administrative tasks."""
import os
import sys


def main():
    """Run administrative tasks."""
    os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'pollsite.settings')
    
    # Auto-run migrations and seed on startup
    if 'runserver' in sys.argv or len(sys.argv) == 1:
        from django.core.management import execute_from_command_line
        
        # Run migrations
        print("Running migrations...")
        execute_from_command_line(['manage.py', 'migrate', '--noinput'])
        
        # Seed database if empty
        print("Checking for seed data...")
        execute_from_command_line(['manage.py', 'seed'])
    
    try:
        from django.core.management import execute_from_command_line
    except ImportError as exc:
        raise ImportError(
            "Couldn't import Django. Are you sure it's installed and "
            "available on your PYTHONPATH environment variable? Did you "
            "forget to activate a virtual environment?"
        ) from exc
    execute_from_command_line(sys.argv)


if __name__ == '__main__':
    main()
