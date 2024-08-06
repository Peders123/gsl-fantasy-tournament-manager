from django.urls import path

from . import views

urlpatterns = [
    path('', views.auction, name='auction'),
    path('<str:room_name>/', views.room, name='room'),
]
