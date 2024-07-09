"""
Defines the serializers for all models defined in Talaria.
"""
import os

from rest_framework import serializers

from .models import User, Tournament, Captain, Player


class TournamentGetSerializer(serializers.ModelSerializer):

    class Meta:

        model = Tournament
        fields = ['tournament_id', 'date', 'time', 'title', 'description']


class TournamentPostSerializer(serializers.ModelSerializer):

    class Meta:

        model = Tournament
        fields = ['date', 'time', 'title', 'description']

    def create(self, validated_data):

        tournament = Tournament.objects.using(os.environ['BUILD_TYPE']).create(
            date=validated_data['date'],
            time=validated_data['time'],
            title=validated_data['title'],
            description=validated_data['description']
        )

        return tournament


class UserSerializer(serializers.ModelSerializer):

    class Meta:

        model = User
        fields = ['user_id', 'discord_name', 'smite_name']

    def create(self, validated_data):

        user = User.objects.using(os.environ['BUILD_TYPE']).create(
            user_id=validated_data['user_id'],
            discord_name=validated_data['discord_name'],
            smite_name=validated_data['smite_name']
        )

        return user


class CaptainGetSerializer(serializers.ModelSerializer):

    class Meta:

        model = Captain
        fields = ['captain_id', 'user_id', 'team_name', 'reason', 'captain_budget']


class CaptainPostSerializer(serializers.ModelSerializer):

    class Meta:

        model = Captain
        fields = ['user_id', 'team_name', 'reason']

    def create(self, validated_data):

        captain = Captain.objects.using(os.environ['BUILD_TYPE']).create(
            user_id=validated_data['user_id'],
            team_name=validated_data['team_name'],
            reason=validated_data['reason']
        )

        return captain


class PlayerGetSerializer(serializers.ModelSerializer):

    class Meta:

        model = Player
        fields = ['player_id', 'tournament_id', 'user_id', 'captain_id', 'role_1', 'role_2',
                  'smite_guru']


class PlayerPostSerializer(serializers.ModelSerializer):

    class Meta:

        model = Player
        fields = ['tournament_id', 'user_id', 'captain_id', 'role_1', 'role_2', 'smite_guru']

    def create(self, validated_data):

        player = Player.objects.using(os.environ['BUILD_TYPE']).create(
            tournament_id=validated_data['tournament_id'],
            user_id=validated_data['user_id'],
            captain_id=validated_data['captain_id'],
            role_1=validated_data['role_1'],
            role_2=validated_data['role_2'],
            smite_guru=validated_data['smite_guru']
        )

        return player
