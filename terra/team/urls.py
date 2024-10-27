from django.urls import  path

from . import views

urlpatterns = [
    path("", views.overview, name="team_overview"),
    path("division/<int:division_id>", views.division_teams, name='division_teams'),
]