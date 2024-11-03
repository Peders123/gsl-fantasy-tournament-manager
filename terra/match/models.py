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
    data_name: str = models.CharField(max_length=64)

    @classmethod
    def from_dict(cls, data: dict):
        return cls(
            id=data["id"],
            franchise_name=data["franchise_name"],
            franchise_owner=data["franchise_owner"],
            data_name=data["data_name"]
        )
    
    def __str__(self):
        return self.franchise_name
    
    @property
    def short_name(self):
        return f"{''.join([word[0] for word in self.franchise_name.split()])}"


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

    @property
    def short_name(self):
        return self.franchise.short_name


class Match(models.Model):

    id = models.IntegerField(primary_key=True)
    match_date_time = models.DateTimeField()
    best_of = models.IntegerField()
    team1 = models.ForeignKey(Team, related_name='match_as_team1', on_delete=models.CASCADE)
    team2 = models.ForeignKey(Team, related_name='match_as_team2', on_delete=models.CASCADE)

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.games: list[Game] = []

    @classmethod
    def from_dict(cls, data: dict):
        return cls(
            id=data["id"],
            match_date_time=datetime.strptime(data["match_date_time"], "%Y-%m-%dT%H:%M:%S"),
            best_of=data["best_of"],
            team1=Team.from_dict(data["team1"]),
            team2=Team.from_dict(data["team2"]),
        )

    def set_games(self, games: list):
        self.games = games

    def __str__(self):
        return f"{self.team1} vs {self.team2}"
    
    @property
    def read_date(self, user_timezone="UTC"):

        date_time = self.match_date_time.replace(tzinfo=dt_timezone.utc).astimezone(ZoneInfo(user_timezone))
        suffix: str = "th" if 4 <= date_time.day <= 20 or 24 <= date_time.day <= 30 else ["st", "nd", "rd"][date_time.day % 10 - 1]

        return date_time.strftime(f"%a - {date_time.day}{suffix} %b - %-I:%M %p")

    @property
    def is_future(self):
        return datetime.now() < self.match_date_time

    @property
    def days_in_future(self):
        return (self.match_date_time - datetime.now()).days
    
    @property
    def result(self):
        team1_score = 0
        team2_score = 0
        for game in self.games:
            if game.winning_team_id == self.team1.id:
                team1_score += 1
            else:
                team2_score += 1
        return f"{team1_score} - {team2_score}"
    
    def get_team(self, team_id):
        return self.team1 if self.team1.id == team_id else self.team2
    

class Game(models.Model):

    id = models.IntegerField(primary_key=True)
    match = models.ForeignKey(Match, on_delete=models.CASCADE)
    date_time = models.DateTimeField()
    order_team_id = models.IntegerField()
    chaos_team_id = models.IntegerField()
    winning_team_id = models.IntegerField()
    match_duration = models.IntegerField()
    ban_1 = models.IntegerField()
    ban_2 = models.IntegerField()
    ban_3 = models.IntegerField()
    ban_4 = models.IntegerField()
    ban_5 = models.IntegerField()
    ban_6 = models.IntegerField()
    ban_7 = models.IntegerField()
    ban_8 = models.IntegerField()
    ban_9 = models.IntegerField()
    ban_10 = models.IntegerField()

    @classmethod
    def from_dict(cls, data: dict, match: Match):
        return cls(
            id=data["id"],
            match_id=match,
            date_time=datetime.strptime(data["date_time"], "%Y-%m-%dT%H:%M:%S") if data["date_time"] else None,
            order_team_id=data["order_team_id"],
            chaos_team_id=data["chaos_team_id"],
            winning_team_id=data["winning_team_id"],
            match_duration=data["match_duration"],
            ban_1=data["ban_1"],
            ban_2=data["ban_2"],
            ban_3=data["ban_3"],
            ban_4=data["ban_4"],
            ban_5=data["ban_5"],
            ban_6=data["ban_6"],
            ban_7=data["ban_7"],
            ban_8=data["ban_8"],
            ban_9=data["ban_9"],
            ban_10=data["ban_10"],
        )
    
    @property
    def duration(self):
        return f"{self.match_duration // 60}m {self.match_duration % 60}s"
    
    @property
    def no_bans(self):
        return all(getattr(self, f"ban_{i}") is None or getattr(self, f"ban_{i}") == 0 for i in range(1, 11))
    
    @property
    def bans(self):
        return {
            "order": [
                self.ban_1,
                self.ban_3,
                self.ban_5,
                self.ban_8,
                self.ban_10
            ],
            "chaos": [
                self.ban_2,
                self.ban_4,
                self.ban_6,
                self.ban_7,
                self.ban_9
            ]
        }
