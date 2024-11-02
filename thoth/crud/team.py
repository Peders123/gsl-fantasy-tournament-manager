from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import joinedload

from thoth.crud.user_team import get_current_team_users
from thoth.models import Team, UserTeam, User
from thoth.schemas.team import TeamUsers, TeamDisplay


async def get_team_from_values(database: AsyncSession, division_id: int, franchise_id: int) -> Team:

    return (
        await database.scalars(
            select(Team).where(Team.division_id == division_id).where(Team.franchise_id == franchise_id)
        )
    ).first()


async def get_team(database: AsyncSession, team_id: int) -> TeamDisplay:

    return (
        await database.scalars(
            select(Team).where(Team.id == team_id).options(joinedload(Team.franchise), joinedload(Team.division))
        )
    ).first()


async def get_team_divisions(database: AsyncSession, division_id: int) -> list[TeamDisplay]:

    return (
        (
            await database.execute(
                select(Team).where(Team.division_id == division_id).options(joinedload(Team.franchise))
            )
        )
        .scalars()
        .all()
    )


async def get_all_teams(database: AsyncSession) -> list[TeamDisplay]:

    return (
        (await database.execute(select(Team).options(joinedload(Team.franchise), joinedload(Team.division))))
        .scalars()
        .all()
    )


async def get_team_users(database: AsyncSession, team_id: int) -> Team:

    team = await get_team(database, team_id)
    team.users = await get_current_team_users(database, team_id)

    return team
