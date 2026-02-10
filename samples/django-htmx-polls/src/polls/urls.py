from django.urls import path
from . import views

urlpatterns = [
    path('', views.index, name='index'),
    path('poll/<int:poll_id>/', views.detail, name='detail'),
    path('poll/<int:poll_id>/vote/<int:choice_id>/', views.vote, name='vote'),
    path('poll/<int:poll_id>/results/', views.results_partial, name='results_partial'),
    path('create/', views.create, name='create'),
    path('add-choice-input/', views.add_choice_input, name='add_choice_input'),
]
