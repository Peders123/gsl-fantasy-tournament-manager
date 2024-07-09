import os

from rest_framework import viewsets
from .models import Tournament, User, Captain, Player
from .serializers import (
    TournamentGetSerializer, TournamentPostSerializer,
    UserSerializer,
    CaptainGetSerializer, CaptainPostSerializer,
    PlayerGetSerializer, PlayerPostSerializer
)


class TournamentViewSet(viewsets.ModelViewSet):

    queryset = Tournament.objects.using(os.environ['BUILD_TYPE']).all()

    def get_serializer_class(self):
        if self.action == 'create':
            return TournamentPostSerializer
        return TournamentGetSerializer


class UserViewSet(viewsets.ModelViewSet):

    queryset = User.objects.using(os.environ['BUILD_TYPE']).all()
    serializer_class = UserSerializer


class CaptainViewSet(viewsets.ModelViewSet):

    queryset = Captain.objects.using(os.environ['BUILD_TYPE']).all()

    def get_serializer_class(self):
        if self.action == 'create':
            return CaptainPostSerializer
        return CaptainGetSerializer


class PlayerViewSet(viewsets.ModelViewSet):

    queryset = Player.objects.using(os.environ['BUILD_TYPE']).all()

    def get_serializer_class(self):
        if self.action == 'create':
            return PlayerPostSerializer
        return PlayerGetSerializer
