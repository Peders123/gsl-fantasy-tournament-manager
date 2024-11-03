from django.urls import  path

from . import views

urlpatterns = [
    path("", views.overview, name="team_overview"),
    path("division/<int:division_id>", views.division_teams, name='division_teams'),
    path("<int:team_id>/", views.profile, name="team_profile"),
    path("standings/<int:division_id>/", views.standings, name="standings"),
]
