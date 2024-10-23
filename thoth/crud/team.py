from sqlalchemy import func, select
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import aliased, joinedload

from thoth.models import Team, UserTeam
from thoth.schemas.team import TeamUsers, TeamDisplay


async def get_team_from_values(database: AsyncSession, division_id: int, franchise_id: int) -> Team:

    return (await database.scalars(select(Team).where(Team.division_id == division_id).where(Team.franchise_id == franchise_id))).first()


async def get_all_teams(database: AsyncSession) -> list[TeamDisplay]:

    return (await database.execute(
        select(Team)
        .options(
            joinedload(Team.franchise),
            joinedload(Team.division)
        )
    )).scalars().all()


async def get_team_users(database: AsyncSession, team_id: int) -> TeamUsers:

    user_team_alias = aliased(UserTeam)

    subquery = (
        select(user_team_alias.user_id, func.max(user_team_alias.join_order).label("max_join_order"))
        .group_by(user_team_alias.user_id)
        .subquery()
    )

    return (await database.scalars(
        select(Team)
        .options(
            joinedload(Team.user_teams).joinedload(UserTeam.user),
            joinedload(Team.franchise),
            joinedload(Team.division)
        )
        .outerjoin(UserTeam, Team.user_teams)
        .outerjoin(subquery, (UserTeam.user_id == subquery.c.user_id) & (UserTeam.join_order == subquery.c.max_join_order))
        .where(Team.id == team_id)
    )).first()
