from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from models import Captain
from schemas import CaptainCreate


async def get_captain(database: AsyncSession, captain_id: int):

    return (await database.scalars(select(Captain).where(Captain.id == captain_id))).first()


async def get_captains(database: AsyncSession):

    return (await database.scalars(select(Captain))).all()


async def create_captain(database: AsyncSession, captain: CaptainCreate):

    db_captain = Captain(
        tournament_id=captain.tournament_id,
        user_id=captain.user_id,
        smite_name=captain.smite_name,
        team_name=captain.team_name,
        reason=captain.reason,
        captain_budget=captain.captain_budget
    )

    database.add(db_captain)
    await database.commit()
    await database.refresh(db_captain)

    return db_captain
