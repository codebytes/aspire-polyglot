"""
URL configuration for pollsite project.
"""
from django.urls import path, include

urlpatterns = [
    path('', include('polls.urls')),
]
