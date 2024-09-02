import os

from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework import status
from rest_framework import viewsets
from .models import Tournament, User, Captain, Player
from .serializers import (
    TournamentGetSerializer, TournamentPostSerializer,
    UserSerializer,
    CaptainGetSerializer, CaptainPostSerializer,
    PlayerGetSerializer, PlayerPostSerializer
)


class TournamentViewSet(viewsets.ModelViewSet):

    queryset = Tournament.objects.all()

    def get_serializer_class(self):
        if self.action == 'create':
            return TournamentPostSerializer
        return TournamentGetSerializer


class UserViewSet(viewsets.ModelViewSet):

    queryset = User.objects.all()
    serializer_class = UserSerializer


class CaptainViewSet(viewsets.ModelViewSet):

    queryset = Captain.objects.all()

    def get_serializer_class(self):
        if self.action == 'create':
            return CaptainPostSerializer
        return CaptainGetSerializer

    @action(detail=False, methods=['get'], url_path='by-user/(?P<user_id>[^/.]+)')
    def get_by_user_id(self, request, user_id=None):
        try:
            captain = Captain.objects.get(user_id=user_id)
            serializer = self.get_serializer(captain)
            return Response(serializer.data, status=status.HTTP_200_OK)
        except Captain.DoesNotExist:
            return Response({"detail": "Captain not found"}, status=status.HTTP_404_NOT_FOUND)


class PlayerViewSet(viewsets.ModelViewSet):

    queryset = Player.objects.all()

    def get_serializer_class(self):
        if self.action == 'create':
            return PlayerPostSerializer
        return PlayerGetSerializer
    
    @action(detail=False, methods=['get'], url_path='by-user/(?P<user_id>[^/.]+)')
    def get_by_user_id(self, request, user_id=None):
        try:
            player = Player.objects.get(user_id=user_id)
            serializer = self.get_serializer(player)
            return Response(serializer.data, status=status.HTTP_200_OK)
        except Player.DoesNotExist:
            return Response({"detail": "Player not found"}, status=status.HTTP_404_NOT_FOUND)
