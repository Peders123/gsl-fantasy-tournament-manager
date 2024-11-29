from datetime import datetime

from pydantic import BaseModel, ConfigDict


class _User(BaseModel):

    id: str
    discord_name: str


class UserView(_User):

    model_config = ConfigDict(from_attributes=True)


class UserCreate(_User):
    pass


class _Tournament(BaseModel):

    datetime: datetime
    title: str
    description: str


class TournamentView(_Tournament):

    model_config = ConfigDict(from_attributes=True)

    id: int


class TournamentCreate(_Tournament):
    pass


class _Player(BaseModel):

    tournament_id: int
    user_id: str
    captain_id: int
    smite_name: str
    role_1: str
    role_2: str
    smite_guru: str


class PlayerView(_Player):

    model_config = ConfigDict(from_attributes=True)

    id: int


class PlayerCreate(_Player):
    pass
