from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import joinedload

from thoth.models import Team
from thoth.schemas.team import TeamCreate


async def get_team_from_values(database: AsyncSession, division_id: int, franchise_id: int) -> Team:

    return (await database.scalars(select(Team).where(Team.division_id == division_id).where(Team.franchise_id == franchise_id))).first()


async def get_all_teams(database: AsyncSession) -> list[dict]:

    return (await database.execute(
        select(Team)
        .options(
            joinedload(Team.franchise),
            joinedload(Team.division)
        )
    )).scalars().all()
