from django.db import models

from tournament.models import Tournament

class Suggestion(models.Model):

    suggestion_id = models.AutoField(primary_key=True)
    tournament_id = models.ForeignKey(Tournament, on_delete=models.CASCADE, default=1)
    player_name = models.CharField(max_length=32)
    discord_nametag = models.CharField(max_length=32)
    suggested_value = models.IntegerField(default=0)