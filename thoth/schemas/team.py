from pydantic import BaseModel, ConfigDict

from thoth.schemas.division import Division
from thoth.schemas.franchise import Franchise
from thoth.schemas.user_team import UserTeamDetailed


class _Team(BaseModel):

    franchise_id: int
    division_id: int


class Team(_Team):

    model_config = ConfigDict(from_attributes=True)
    id: int


class TeamDisplay(BaseModel):

    model_config = ConfigDict(fromt_attributes=True)

    id: int
    franchise: Franchise
    division: Division


class TeamUsers(TeamDisplay):

    user_teams: list[UserTeamDetailed]


class TeamCreate(_Team):

    pass
