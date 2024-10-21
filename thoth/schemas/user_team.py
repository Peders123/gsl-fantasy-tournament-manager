from datetime import datetime, timezone

from pydantic import BaseModel, ConfigDict, field_serializer


class _UserTeam(BaseModel):

    user_id: int
    team_id: int | None
    join_date: datetime | None
    leave_date: datetime | None


class UserTeam(_UserTeam):

    model_config = ConfigDict(from_attributes=True)
    id: int
    join_order: int


class UserTeamCreate(_UserTeam):

    @field_serializer("join_date", mode="plain")
    def make_join_date_naive(cls, value: datetime):
        return make_datetime_naive(value)

    @field_serializer("leave_date", mode="plain")
    def make_leave_date_naive(cls, value: datetime):
        return make_datetime_naive(value)


def make_datetime_naive(value: datetime):
    if value and value.tzinfo is not None:
        value = value.astimezone(timezone.utc).replace(tzinfo=None)
    return value
