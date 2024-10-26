from django.db import models
from django.utils import timezone


class Franchise(models.Model):

    id: int = models.IntegerField(primary_key=True)
    franchise_name = models.CharField(max_length=255)
    franchise_owner = models.CharField(max_length=255)

    @classmethod
    def from_dict(cls, data: dict):
        return cls(
            id=data["id"],
            franchise_name=data["franchise_name"],
            franchise_owner=data["franchise_owner"]
        )

    def __str__(self):
        return self.franchise_name


class Division(models.Model):

    id: int = models.IntegerField(primary_key=True)
    division_name = models.CharField(max_length=255)
    division_rank = models.IntegerField()

    @classmethod
    def from_dict(cls, data: dict):
        return cls(
            id=data["id"],
            division_name=data["division_name"],
            division_rank=data["division_rank"]
        )

    def __str__(self):
        return self.division_name


class Team(models.Model):

    id: int = models.IntegerField(primary_key=True)
    franchise = models.ForeignKey(Franchise, related_name="teams", on_delete=models.CASCADE)
    division = models.ForeignKey(Division, related_name="teams", on_delete=models.CASCADE)

    @classmethod
    def from_dict(cls, data: dict):
        return cls(
            id=data["id"],
            franchise=Franchise.from_dict(data["franchise"]),
            division=Division.from_dict(data["division"])
        )

    def __str__(self):
        return f"{self.franchise.franchise_name} ({self.division.division_name})"


class User(models.Model):

    id: int = models.IntegerField(primary_key=True)
    discord_name = models.CharField(max_length=255)

    @classmethod
    def from_dict(cls, data: dict):
        return cls(
            id=data["id"],
            discord_name=data["discord_name"]
        )

    def __str__(self):
        return self.discord_name


class UserTeam(models.Model):

    id: int = models.IntegerField(primary_key=True)
    user = models.ForeignKey(User, related_name="user_teams", on_delete=models.CASCADE)
    team = models.ForeignKey(Team, related_name="user_teams", on_delete=models.CASCADE, null=True, blank=True)
    join_order = models.IntegerField()
    join_date = models.DateTimeField(null=True, blank=True)
    leave_date = models.DateTimeField(null=True, blank=True)

    @classmethod
    def from_dict(cls, data: dict, team_data: dict):
        return cls(
            id=data["id"],
            user=User.from_dict(data["user"]),
            team=Team.from_dict(team_data),
            join_order=data["join_order"],
            join_date=data["join_date"],
            leave_date=data["leave_date"]
        )

    def __str__(self):
        return f"{self.user.discord_name} - {self.team}"
