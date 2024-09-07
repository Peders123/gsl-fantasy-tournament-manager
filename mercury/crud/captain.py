from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from models import Captain


async def get_captain(database: AsyncSession, captain_id: int):

    return (await database.scalars(select(Captain).where(Captain.captain_id==captain_id))).first()


async def get_captains(database: AsyncSession):

    return (await database.scalars(select(Captain))).all()
