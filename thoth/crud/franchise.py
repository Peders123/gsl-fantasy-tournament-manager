from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from thoth.models import Franchise


async def get_all_franchises(database: AsyncSession) -> list[Franchise]:
    return (await database.scalars(select(Franchise).order_by(Franchise.id))).all()
