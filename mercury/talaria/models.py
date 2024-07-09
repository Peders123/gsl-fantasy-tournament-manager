"""
Defines the database models for the Talaria app.
"""
import os

from datetime import datetime

from django.conf import settings
from django.db import models
from django.db.models.signals import post_save
from django.dispatch import receiver

from rest_framework.authtoken.models import Token


@receiver(post_save, sender=settings.AUTH_USER_MODEL)
def create_auth_token(sender, instance=None, created=False, **kwargs):
    """Automatically generates a token upon creation of a new admin user.

    Args:
        sender (_type_): Model that sends the signal.
        instance (_type_, optional): User that was created. Defaults to None.
        created (bool, optional): Whether or not the user was created. Defaults to False.
    """
    if created:
        Token.objects.create(user=instance)


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
    """Model representing a single user, mapping 1-to-1 with a discord account.

    Attributes:
        user_id (IntegerField): Primary key. Same as the user's discord id.
        discord_name (CharField): Signed up user's discord name.
        smite_name (CharField): Sign up user's smite ign.
    """
    user_id = models.IntegerField(primary_key=True)
    discord_name = models.CharField(max_length=32)
    smite_name = models.CharField(max_length=32)


class Captain(models.Model):
    """Model representing a captain for the specific tournament.

    Attributes:
        captain_id (IntegerField): Primary key. Is assigned automatically.
        user_id (ForeignKey): Relation to the 'User' model.
        team_name (CharField): The name of the captain's team.
        reason (CharField): Justification for why the person should be a captain.
        captain_budget (IntegerField): Budget the captain will begin the tournament with.
    """
    captain_id = models.IntegerField(primary_key=True)
    user_id = models.ForeignKey(User, on_delete=models.CASCADE)
    team_name = models.CharField(max_length=32)
    reason = models.CharField(max_length=256)
    captain_budget = models.IntegerField(default=0)


class Player(models.Model):
    """Model representing a standard player in a tournament.

    Attributes:
        player_id (AutoField): Primary key. Is assigned automatically.
        tournament_id (ForeignKey): Relation to the 'Tournament' model.
        user_id (ForeignKey): Relation to the 'User' model.
        captain_id (ForeignKey): Relation to the 'Captain' model. Not assiged at creation, added later.
        role_1 (CharField): Primary role the player wants to be.
        role_2 (CharField): Secondary role the player wants to be.
        smite_guru (CharField): Link to the player's smite guru profile. Optional.
        estimated_value (IntegerField): How much we estimate the player is worth.
    """
    player_id = models.AutoField(primary_key=True)
    tournament_id = models.ForeignKey(Tournament, on_delete=models.CASCADE, default=1)
    user_id = models.ForeignKey(User, on_delete=models.CASCADE)
    captain_id = models.ForeignKey(Captain, on_delete=models.CASCADE, null=True)
    role_1 = models.CharField(max_length=16, default="Fill")
    role_2 = models.CharField(max_length=16, default="Fill")
    smite_guru = models.CharField(max_length=128)
    estimated_value = models.IntegerField(default=0)
