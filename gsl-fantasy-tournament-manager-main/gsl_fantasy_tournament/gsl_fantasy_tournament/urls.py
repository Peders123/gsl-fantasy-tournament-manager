"""
URL configuration for gsl_fantasy_tournament project.

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/4.2/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import include, path

from . import views

urlpatterns = [
    path('admin/', admin.site.urls),
    path('', include('home.urls')),
    path('<str:date>', include('tournament.urls')),
    path('Users/', views.User_list),
    path('Users/<int:User_id>', views.User_detail),
    path('Tournaments/', views.Tournament_list),
    path('Tournaments/<int:tournament_id>', views.Tournament_detail),
    path('Captains/', views.Captain_list),
    path('Captain/<int:captain_id>', views.Captain_detail),
    path('Players/', views.Player_list),
    path('Players/<int:player_id>', views.Player_detail)

]
