from django.db import models

from django.utils import timezone


class Tournament(models.Model):

    tournament_id = models.IntegerField(primary_key=True)
    date = models.DateField(default=timezone.now())
    time = models.TimeField(default=timezone.now())
    title = models.CharField(max_length=64, default="Glaeria Smite League")
    description = models.CharField(max_length=256, default="None")

    @property
    def url_id(self):

        return self.date.strftime('%Y%m%d')


class User(models.Model):

    user_id = models.IntegerField(primary_key=True)
    discord_name = models.CharField(max_length=32)
    smite_name = models.CharField(max_length=32)


class Captain(models.Model):

    captain_id = models.IntegerField(primary_key=True)
    user_id = models.ForeignKey(User, on_delete=models.CASCADE)
    team_name = models.CharField(max_length=32)
    captain_budget = models.IntegerField()


class Player(models.Model):

    player_id = models.AutoField(primary_key=True)
    tournament_id = models.ForeignKey(Tournament, on_delete=models.CASCADE, default=1)
    user_id = models.ForeignKey(User, on_delete=models.CASCADE)
    captain_id = models.ForeignKey(Captain, on_delete=models.CASCADE, null=True)
    role_1 = models.CharField(max_length=16, default="Fill")
    role_2 = models.CharField(max_length=16, default="Fill")
    estimated_value = models.IntegerField()
