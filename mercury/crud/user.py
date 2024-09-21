from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from models import User
from schemas.user import UserCreate, UserUpdate


async def get_user(database: AsyncSession, user_id: str) -> User:

    return (await database.scalars(select(User).where(User.id == user_id))).first()


async def get_users(database: AsyncSession) -> list[User]:

    return (await database.scalars(select(User).order_by(User.id))).all()


async def create_user(database: AsyncSession, user_data: UserCreate) -> User:

    db_user = User(
        id=user_data.id,
        discord_name=user_data.discord_name
    )

    database.add(db_user)
    await database.commit()
    await database.refresh(db_user)

    return db_user


async def delete_user(database: AsyncSession, user: User) -> None:

    await database.delete(user)
    await database.commit()


async def replace_user(database: AsyncSession, user: User, user_data: UserCreate):

    for key, value in user_data.model_dump(exclude_unset=False).items():
        setattr(user, key, value)

    await database.commit()
    await database.refresh(user)

    return user


async def update_user(database: AsyncSession, user: User, user_data: UserUpdate):

    for key, value in user_data.model_dump(exclude_unset=True).items():
        setattr(user, key, value)

    await database.commit()
    await database.refresh(user)
    return user
