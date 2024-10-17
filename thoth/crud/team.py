from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import joinedload

from thoth.models import Team


async def get_all_teams(database: AsyncSession) -> list[dict]:

    return (await database.execute(
        select(Team)
        .options(
            joinedload(Team.franchise),
            joinedload(Team.division)
        )
    )).scalars().all()
