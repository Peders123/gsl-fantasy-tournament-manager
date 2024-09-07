from datetime import datetime

from pydantic import BaseModel, ConfigDict


class User(BaseModel):

    user_id: str
    discord_name: str


class UserView(User):

    model_config = ConfigDict(from_attributes=True)


class Tournament(BaseModel):
    
    datetime: datetime
    title: str
    description: str


class TournamentView(Tournament):

    model_config = ConfigDict(from_attributes=True)

    tournament_id: int


class Captain(BaseModel):

    tournament_id_id: int | None
    user_id_id: str | None
    smite_name: str
    team_name: str
    reason: str
    captain_budget: int


class CaptainView(Captain):
    
    model_config = ConfigDict(from_attributes=True)

    captain_id: int


class Player(BaseModel):

    tournament_id: int
    user_id: str
    captain_id: int
    smite_name: str
    role_1: str
    role_2: str
    smite_guru: str


class PlayerView(Player):
    
    model_config = ConfigDict(from_attributes=True)
