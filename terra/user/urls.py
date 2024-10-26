from django.urls import  path

from . import views

urlpatterns = [
    path("", views.overview, name="player_overview"),
    path("create", views.create, name="user_create"),
    path("assign", views.assign, name="player_assign"),
    path("transfer", views.transfer, name="user_transfer"),
    path("division/<int:division_id>", views.division_teams, name='division_teams'),
]
