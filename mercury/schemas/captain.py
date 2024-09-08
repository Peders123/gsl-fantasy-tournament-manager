from pydantic import BaseModel, ConfigDict


class _Captain(BaseModel):

    tournament_id: int
    user_id: str
    smite_name: str
    team_name: str
    reason: str
    captain_budget: int


class CaptainView(_Captain):

    model_config = ConfigDict(from_attributes=True)

    id: int


class CaptainCreate(_Captain):
    pass


class CaptainUpdate(_Captain):

    tournament_id: int | None = None
    user_id: str | None = None
    smite_name: str | None = None
    team_name: str | None = None
    reason: str | None = None
    captain_budget: int | None = None