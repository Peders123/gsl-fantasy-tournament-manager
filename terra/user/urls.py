from django.urls import  path

from . import views

urlpatterns = [
    path("", views.overview, name="player_overview"),
    path("division/<int:division_id>", views.division_teams, name='division_teams_old'),
]
