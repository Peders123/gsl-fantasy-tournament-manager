from datetime import datetime, timezone

from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from thoth.models import Match
from thoth.schemas.match import MatchCreate


async def get_all_matches(database: AsyncSession) -> list[Match]:
    return (await database.scalars(select(Match).order_by(Match.match_date_time))).all()


async def get_match(database: AsyncSession, match_id: int) -> Match:
    return (await database.scalars(select(Match).where(Match.id == match_id))).first()


async def create_match(database: AsyncSession, match: MatchCreate) -> Match:

    if match.match_date_time.tzinfo is not None:
        match.match_date_time = match.match_date_time.astimezone(timezone.utc).replace(tzinfo=None)

    db_match = Match(
        match_date_time=match.match_date_time,
        best_of=match.best_of,
        team1_id=match.team1_id,
        team2_id=match.team2_id
    )

    database.add(db_match)

    return db_match
