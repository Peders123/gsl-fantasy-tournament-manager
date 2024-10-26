from django.urls import  path

from . import views

urlpatterns = [
    path("", views.overview, name="staff_home"),
    path("create", views.create, name="user_create"),
    path("assign", views.assign, name="player_assign"),
    path("transfer", views.transfer, name="user_transfer"),
]
