
from django.urls import path

from . import views

urlpatterns = [
    path('', views.index, name='home'),
    path('login', views.login, name='login'),
    path('login/redirect', views.login_redirect, name='redirect')
]
