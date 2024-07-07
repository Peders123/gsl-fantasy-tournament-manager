import os

from datetime import datetime

from django.db import models

from django.conf import settings
from django.db.models.signals import post_save
from django.dispatch import receiver
from rest_framework.authtoken.models import Token


@receiver(post_save, sender=settings.AUTH_USER_MODEL)
def create_auth_token(sender, instance=None, created=False, **kwargs):

    if created:
        Token.objects.using(os.environ['BUILD_TYPE']).create(user=instance)


class Tournament(models.Model):
    """Model representing an individual tournament.

    Attributes:
        tournament_id (IntegerField): Primary key for a tournament record.
        date (DateField): The day the tournament takes place.
        time (TimeField): The time the tournament takes place.
        title (CharField): The user-facing name of the tournament.
        description (CharField): Brief description of what the touranment is.
    """
    tournament_id = models.IntegerField(primary_key=True)
    date = models.DateField(default=datetime(2001, 11, 5, 0, 0))
    time = models.TimeField(default=datetime(1, 1, 1, 12, 0))
    title = models.CharField(max_length=64, default="Glaeria Smite League")
    description = models.CharField(max_length=256, default="None")

    @property
    def url_id(self):
        """Returns the date in a format to be used in the url."""
        return self.date.strftime('%Y%m%d')


class User(models.Model):

    user_id = models.IntegerField(primary_key=True)
    discord_name = models.CharField(max_length=32)
    smite_name = models.CharField(max_length=32)


class Captain(models.Model):

    captain_id = models.IntegerField(primary_key=True)
    user_id = models.ForeignKey(User, on_delete=models.CASCADE)
    team_name = models.CharField(max_length=32)
    reason = models.CharField(max_length=256)
    captain_budget = models.IntegerField(default=0)


class Player(models.Model):

    player_id = models.AutoField(primary_key=True)
    tournament_id = models.ForeignKey(Tournament, on_delete=models.CASCADE, default=1)
    user_id = models.ForeignKey(User, on_delete=models.CASCADE)
    captain_id = models.ForeignKey(Captain, on_delete=models.CASCADE, null=True)
    role_1 = models.CharField(max_length=16, default="Fill")
    role_2 = models.CharField(max_length=16, default="Fill")
    smite_guru = models.CharField(max_length=128)
    estimated_value = models.IntegerField(default=0)
