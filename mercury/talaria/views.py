from django.shortcuts import render
from django.http import HttpResponse

from django.db import router

from datetime import datetime

from .models import Tournament, Player, User
from .serializers import UserSerializer

from rest_framework import generics


class UserListView(generics.ListAPIView):

    queryset = User.objects.all()
    serializer_class = UserSerializer


def index(request):

    t1 = Tournament()
    t1.save(using='dev')

    t2 = Tournament(title="TSL")
    t2.save(using='default')

    title = Tournament.objects.using('default').all()[0]

    return HttpResponse(f"Hello World {title.title}")