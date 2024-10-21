from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from thoth.models import UserTeam
from thoth.schemas.user_team import UserTeamCreate


async def get_all_user_teams(database: AsyncSession) -> list[UserTeam]:
    return (await database.scalars(select(UserTeam))).all()


async def get_user_teams(database: AsyncSession, user_id: int, team_id: int) -> list[UserTeam]:
    return (await database.scalars(select(UserTeam).where(UserTeam.user_id == user_id).where(UserTeam.team_id == team_id))).all()


async def create_user_team(database: AsyncSession, user_team: UserTeamCreate) -> UserTeam:

    join_order = len(await get_user_teams(database, user_team.user_id, user_team.team_id))

    db_user = UserTeam(
        user_id=user_team.user_id,
        team_id=user_team.team_id,
        join_order=join_order,
        join_date=user_team.join_date,
        leave_date=user_team.leave_date,
    )

    database.add(db_user)

    return db_user

