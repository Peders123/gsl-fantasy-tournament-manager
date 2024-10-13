from pydantic import BaseModel, ConfigDict


class _Team(BaseModel):

    franchise_id: int
    division_id: int


class Team(_Team):

    model_config = ConfigDict(from_attributes=True)
    id: int


class TeamDisplay(Team):

    franchise_name: str
    division_name: str


class TeamCreate(_Team):

    pass
