from datetime import datetime, timezone

from pydantic import BaseModel, ConfigDict, field_serializer

from thoth.schemas.team import TeamDisplay


class _Match(BaseModel):

    match_date_time: datetime
    best_of: int
    team1_id: int
    team2_id: int


class Match(_Match):

    model_config = ConfigDict(from_attributes=True)
    id: int


class MatchDisplay(BaseModel):

    model_config = ConfigDict(from_attributes=True)

    id: int
    match_date_time: datetime
    best_of: int
    team1: TeamDisplay
    team2: TeamDisplay


class MatchCreate(_Match):

    @field_serializer("match_date_time", mode="plain")
    def make_datetime_naive(cls, value: datetime):
        if value and value.tzinfo is not None:
            # Convert to UTC first, then remove tzinfo to make it naive
            value = value.astimezone(timezone.utc).replace(tzinfo=None)
        return value
