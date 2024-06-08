
from django.urls import path

from . import views

urlpatterns = [
    path('', views.index, name='home'),
    path('Users/', views.user_list),
    path('Users/<int:user_id>', views.user_detail),
    path('Tournaments/', views.tournament_list),
    path('Tournaments/<int:tournament_id>', views.tournament_detail),
    path('Captains/', views.captain_list),
    path('Captain/<int:captain_id>', views.captain_detail),
    path('Players/', views.player_list),
    path('Players/<int:player_id>', views.player_detail)
]
