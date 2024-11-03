from pydantic import BaseModel, ConfigDict, computed_field

from thoth.schemas.division import Division
from thoth.schemas.franchise import Franchise
from thoth.schemas.user_team import UserTeamDetailed
from thoth.schemas.user import User


class _Team(BaseModel):

    franchise_id: int
    division_id: int


class Team(_Team):

    model_config = ConfigDict(from_attributes=True)
    id: int


class TeamDisplay(BaseModel):

    model_config = ConfigDict(from_attributes=True)

    id: int
    franchise: Franchise
    division: Division


class TeamUsers(TeamDisplay):

    users: list[User]


class TeamStandingsModel(TeamDisplay):

    matches: int
    wins: int
    losses: int
    point_difference: int
    

class TeamCreate(_Team):

    pass
