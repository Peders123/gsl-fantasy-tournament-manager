from datetime import datetime, timezone

from pydantic import BaseModel, ConfigDict, field_serializer


class _Match(BaseModel):

    match_date_time: datetime
    best_of: int
    team1_id: int
    team2_id: int


class Match(_Match):

    model_config = ConfigDict(from_attributes=True)
    id: int


class MatchCreate(_Match):

    @field_serializer("match_date_time", mode="plain")
    def make_datetime_naive(cls, value: datetime):
        if value and value.tzinfo is not None:
            # Convert to UTC first, then remove tzinfo to make it naive
            value = value.astimezone(timezone.utc).replace(tzinfo=None)
        return value
