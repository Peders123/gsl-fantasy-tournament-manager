from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from thoth.models import Division


async def get_all_divisions(database: AsyncSession) -> list[Division]:
    return (await database.scalars(select(Division).order_by(Division.division_rank))).all()
