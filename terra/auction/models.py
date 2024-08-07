from django.db import models

from tournament.models import Tournament, Captain, Player


class Bidder(models.Model):

    bidder_id = models.AutoField(primary_key=True)
    tournament_id = models.ForeignKey(Tournament, on_delete=models.CASCADE)
    captain_id = models.ForeignKey(Captain, on_delete=models.CASCADE)
    join_order = models.IntegerField()
    currently_in = models.BooleanField()


class Biddee(models.Model):

    biddee_id = models.AutoField(primary_key=True)
    bidder_id = models.ForeignKey(Bidder, on_delete=models.CASCADE)
    tournament_id = models.ForeignKey(Tournament, on_delete=models.CASCADE)
    player_id = models.ForeignKey(Player, on_delete=models.CASCADE)
    bidded_amount = models.IntegerField()
    draft_order = models.IntegerField()
