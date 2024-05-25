from django.db import models


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

    player_id = models.IntegerField(primary_key=True)
    user_id = models.ForeignKey(User, on_delete=models.CASCADE)
    captain_id = models.ForeignKey(Captain, on_delete=models.CASCADE)
    role_1 = models.CharField(max_length=16, default="Fill")
    role_2 = models.CharField(max_length=16, default="Fill")
    estimated_value = models.IntegerField()
