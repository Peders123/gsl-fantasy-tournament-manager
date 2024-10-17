from django.urls import  path

from . import views

urlpatterns = [
    path('<int:match_id>/', views.overview, name='match_overview'),
]
