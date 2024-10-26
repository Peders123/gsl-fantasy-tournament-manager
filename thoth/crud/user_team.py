from sqlalchemy import func, select
from sqlalchemy.ext.asyncio import AsyncSession

from thoth.models import UserTeam, User
from thoth.schemas.user_team import UserTeamCreate


async def get_all_user_teams(database: AsyncSession) -> list[UserTeam]:
    return (await database.scalars(select(UserTeam))).all()


async def get_user_teams(database: AsyncSession, user_id: int) -> list[UserTeam]:
    return (await database.scalars(select(UserTeam).where(UserTeam.user_id == user_id))).all()


async def get_current_team_users(database: AsyncSession, team_id: int) -> list[User]:

    return (await database.scalars(
        select(User)
        .join(UserTeam)
        .filter(
            UserTeam.team_id == team_id,
            UserTeam.join_order == (
                select(func.max(UserTeam.join_order))
                .where(UserTeam.user_id == User.id)
                .correlate(User)
                .scalar_subquery()
            )
        )
    )).all()


async def create_user_team(database: AsyncSession, user_team: UserTeamCreate) -> UserTeam:

    join_order = len(await get_user_teams(database, user_team.user_id))

    db_user = UserTeam(
        user_id=user_team.user_id,
        team_id=user_team.team_id,
        join_order=join_order,
        join_date=user_team.join_date,
        leave_date=user_team.leave_date,
    )

    database.add(db_user)

    return db_user
