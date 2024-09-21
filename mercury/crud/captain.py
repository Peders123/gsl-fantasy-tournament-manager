from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from models import Captain
from schemas.captain import CaptainCreate, CaptainUpdate


async def get_captain(database: AsyncSession, captain_id: int) -> Captain:

    return (await database.scalars(select(Captain).where(Captain.id == captain_id))).first()


async def get_captains(database: AsyncSession) -> list[Captain]:

    return (await database.scalars(select(Captain).order_by(Captain.id))).all()


async def create_captain(database: AsyncSession, captain_data: CaptainCreate) -> Captain:

    db_captain = Captain(
        tournament_id=captain_data.tournament_id,
        user_id=captain_data.user_id,
        smite_name=captain_data.smite_name,
        team_name=captain_data.team_name,
        reason=captain_data.reason,
        captain_budget=captain_data.captain_budget
    )

    database.add(db_captain)
    await database.commit()
    await database.refresh(db_captain)

    return db_captain


async def delete_captain(database: AsyncSession, captain: Captain) -> None:

    await database.delete(captain)
    await database.commit()


async def replace_captain(database: AsyncSession, captain: Captain, captain_data: CaptainCreate):

    for key, value in captain_data.model_dump(exclude_unset=False).items():
        setattr(captain, key, value)

    await database.commit()
    await database.refresh(captain)

    return captain


async def update_captain(database: AsyncSession, captain: Captain, captain_data: CaptainUpdate):

    for key, value in captain_data.model_dump(exclude_unset=True).items():
        setattr(captain, key, value)

    await database.commit()
    await database.refresh(captain)
    return captain
