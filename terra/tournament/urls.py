from django.urls import path

from . import views

urlpatterns = [
    path('', views.overview, name='tournament_overview'),
    path('/auction', views.auction, name='auction'),
    path('/players', views.players, name='players')
]
