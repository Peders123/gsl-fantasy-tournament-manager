from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from thoth.models import User
from thoth.schemas.user import UserCreate


async def get_all_users(database: AsyncSession) -> list[User]:
    return (await database.scalars(select(User))).all()


async def create_user(database: AsyncSession, user: UserCreate) -> User:

    db_user = User(
        discord_name=user.discord_name,
        team_id=user.team_id,
    )

    database.add(db_user)

    return db_user

