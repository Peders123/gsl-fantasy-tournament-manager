from django.urls import include, path

from auction.views import room
from . import views

urlpatterns = [
    path('', views.overview, name='tournament_overview'),
    path('players', views.players, name='players'),
    path('captains', views.captains, name="captains"),
    path('suggestions', views.suggestions, name='suggestions'),
    path('players/<p_id>/', views.player_suggestion, name='player_suggestion'),
    path('auction', room, name='auction'),
]
