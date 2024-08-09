"""
Defines the database models for the Talaria app.
"""
from datetime import datetime
from django.utils import timezone

import pytz

from django.db import models


class Tournament(models.Model):
    """Model representing an individual tournament.

    Attributes:
        tournament_id (IntegerField): Primary key for a tournament record.
        datetime (DateTimeField): The time the tournament takes place.
        title (CharField): The user-facing name of the tournament.
        description (CharField): Brief description of what the touranment is.
    """
    tournament_id = models.IntegerField(primary_key=True)
    datetime = models.DateTimeField(default=datetime(year=2001, month=11, day=5, hour=0, minute=0, second=0))
    title = models.CharField(max_length=64, default="Glaeria Smite League")
    description = models.CharField(max_length=256, default="None")

    class Meta:

        managed = False
        db_table = "talaria_tournament"

    @property
    def url_id(self):
        """Returns the date in a format to be used in the url."""
        return self.datetime.strftime('%Y%m%d')
    
    def is_past(self):
        """Checks if the tournament time is in the past, considering BST."""
        # Define the BST timezone
        bst = pytz.timezone('Europe/London')

        # Ensure the datetime is aware and in BST
        if timezone.is_naive(self.datetime):
            tournament_time = bst.localize(self.datetime)
        else:
            tournament_time = self.datetime

        # Convert tournament time to the current timezone
        tournament_time = tournament_time.astimezone(timezone.get_current_timezone())

        # Compare with the current time
        return tournament_time < timezone.now()


class User(models.Model):
    """Model representing a single user, mapping 1-to-1 with a discord account.

    Attributes:
        user_id (IntegerField): Primary key. Same as the user's discord id.
        discord_name (CharField): Signed up user's discord name.
    """
    user_id = models.CharField(primary_key=True)
    discord_name = models.CharField(max_length=32)

    class Meta:

        managed = False
        db_table = "talaria_user"


class Captain(models.Model):
    """Model representing a captain for the specific tournament.

    Attributes:
        captain_id (IntegerField): Primary key. Is assigned automatically.
        tournament_id (ForeignKey): Relation to the 'Tournaments' model.
        user_id (ForeignKey): Relation to the 'User' model.
        smite_name (CharField): Sign up user's smite ign.
        team_name (CharField): The name of the captain's team.
        reason (CharField): Justification for why the person should be a captain.
        captain_budget (IntegerField): Budget the captain will begin the tournament with.
    """
    captain_id = models.IntegerField(primary_key=True)
    tournament_id = models.ForeignKey(Tournament, on_delete=models.CASCADE)
    user_id = models.ForeignKey(User, on_delete=models.CASCADE)
    smite_name = models.CharField(max_length=32)
    team_name = models.CharField(max_length=32)
    reason = models.CharField(max_length=256)
    captain_budget = models.IntegerField(default=0)

    class Meta:

        managed = False
        db_table = "talaria_captain"


class Player(models.Model):
    """Model representing a standard player in a tournament.

    Attributes:
        player_id (AutoField): Primary key. Is assigned automatically.
        tournament_id (ForeignKey): Relation to the 'Tournament' model.
        user_id (ForeignKey): Relation to the 'User' model.
        captain_id (ForeignKey): Relation to the 'Captain' model. Not assiged at creation, added later.
        smite_name (CharField): Sign up user's smite ign.
        role_1 (CharField): Primary role the player wants to be.
        role_2 (CharField): Secondary role the player wants to be.
        smite_guru (CharField): Link to the player's smite guru profile. Optional.
        estimated_value (IntegerField): How much we estimate the player is worth.
    """
    player_id = models.AutoField(primary_key=True)
    tournament_id = models.ForeignKey(Tournament, on_delete=models.CASCADE, default=1)
    user_id = models.ForeignKey(User, on_delete=models.CASCADE)
    captain_id = models.ForeignKey(Captain, on_delete=models.CASCADE, null=True)
    smite_name = models.CharField(max_length=32)
    role_1 = models.CharField(max_length=16, default="Fill")
    role_2 = models.CharField(max_length=16, default="Fill")
    smite_guru = models.CharField(max_length=128)
    estimated_value = models.IntegerField(default=0)

    class Meta:

        managed = False
        db_table = "talaria_player"
