from datetime import datetime, timezone as dt_timezone
from zoneinfo import ZoneInfo

from django.db import models
from django.utils import timezone


class Division(models.Model):

    id: int = models.IntegerField(primary_key=True)
    division_name: str = models.CharField(max_length=64)
    division_rank: int = models.IntegerField()

    @classmethod
    def from_dict(cls, data: dict):
        return cls(
            id=data["id"],
            division_name=data["division_name"],
            division_rank=data["division_rank"]
        )
    
    def __str__(self):
        return f"{''.join([word[0] for word in self.division_name.split()])}"


class Franchise(models.Model):

    id: int = models.IntegerField(primary_key=True)
    franchise_name: str = models.CharField(max_length=64)
    franchise_owner: str = models.CharField(max_length=64)

    @classmethod
    def from_dict(cls, data: dict):
        return cls(
            id=data["id"],
            franchise_name=data["franchise_name"],
            franchise_owner=data["franchise_owner"]
        )
    
    def __str__(self):
        return self.franchise_name


class Team(models.Model):

    id: int = models.IntegerField(primary_key=True)
    franchise = models.ForeignKey(Franchise, on_delete=models.CASCADE)
    division = models.ForeignKey(Division, on_delete=models.CASCADE)

    @classmethod
    def from_dict(cls, data: dict):
        return cls(
            id=data["id"],
            franchise=Franchise.from_dict(data["franchise"]),
            division=Division.from_dict(data["division"])
        )

    def __str__(self):
        return f"{self.franchise} ({self.division})"


class Match(models.Model):

    id = models.IntegerField(primary_key=True)
    match_date_time = models.DateTimeField()
    best_of = models.IntegerField()
    team1 = models.ForeignKey(Team, related_name='match_as_team1', on_delete=models.CASCADE)
    team2 = models.ForeignKey(Team, related_name='match_as_team2', on_delete=models.CASCADE)

    @classmethod
    def from_dict(cls, data: dict):
        return cls(
            id=data["id"],
            match_date_time=datetime.strptime(data["match_date_time"], "%Y-%m-%dT%H:%M:%S"),
            best_of=data["best_of"],
            team1=Team.from_dict(data["team1"]),
            team2=Team.from_dict(data["team2"]),
        )

    def __str__(self):
        return f"{self.team1} vs {self.team2}"
    
    @property
    def read_date(self, user_timezone="UTC"):

        date_time = self.match_date_time.replace(tzinfo=dt_timezone.utc).astimezone(ZoneInfo(user_timezone))
        suffix: str = "th" if 4 <= date_time.day <= 20 or 24 <= date_time.day <= 30 else ["st", "nd", "rd"][date_time.day % 10 - 1]

        return date_time.strftime(f"%a - {date_time.day}{suffix} %b - %-I:%M %p")
