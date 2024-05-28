
from django.urls import include, path

from . import views

urlpatterns = [
    path('', views.index, name='home'),
    path('Users/', views.User_list),
    path('Users/<int:User_id>', views.User_detail),
    path('Tournaments/', views.Tournament_list),
    path('Tournaments/<int:tournament_id>', views.Tournament_detail),
    path('Captains/', views.Captain_list),
    path('Captain/<int:captain_id>', views.Captain_detail),
    path('Players/', views.Player_list),
    path('Players/<int:player_id>', views.Player_detail)
]
