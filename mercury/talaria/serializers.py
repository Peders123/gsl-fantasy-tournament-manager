from rest_framework import serializers

from .models import User, Tournament, Captain, Player


class UserSerializer(serializers.ModelSerializer):

    class Meta:

        model = User
        fields = ['user_id', 'discord_name', 'smite_name']


class TournamentSerializer(serializers.ModelSerializer):

    class Meta:

        model = Tournament
        fields = ['tournament_id', 'date', 'time', 'title', 'description']


class CaptainSerializer(serializers.ModelSerializer):

    class Meta:

        model = Captain
        fields = ['captain_id', 'user_id', 'team_name', 'captain_budget']


class PlayerSerializer(serializers.ModelSerializer):

    class Meta:

        model = Player
        fields = ['player_id', 'tournament_id', 'user_id', 'captain_id', 'role_1', 'role_2', 'estimated_value']
