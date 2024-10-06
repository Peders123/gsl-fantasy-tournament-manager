from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from thoth.models import Match
from thoth.schemas.match import MatchCreate


async def get_all_matches(database: AsyncSession) -> list[Match]:
    return (await database.scalars(select(Match))).all()


async def create_match(database: AsyncSession, match: MatchCreate) -> Match:

    db_match = Match(
        match_date_time=match.match_date_time,
        best_of=match.best_of,
        team1_id=match.team1_id,
        team2_id=match.team2_id
    )

    database.add(db_match)
    await database.commit()
    await database.refresh(db_match)

    return db_match
