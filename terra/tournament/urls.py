from django.urls import path

from . import views

urlpatterns = [
    path('', views.overview, name='tournament_overview'),
    path('players', views.players, name='players'),
    path('auction', views.auction, name='auction'),
    path('captains', views.captains, name="captains"),
    path('suggestions', views.suggestions, name='suggestions'),
    path('players/<p_id>/', views.player_suggestion, name= 'player_suggestion'),
]
