from datetime import datetime, timezone

from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import joinedload

from thoth.models import Match, Team
from thoth.schemas import match as match_schema, team as team_schema


async def get_all_matches(database: AsyncSession) -> list[Match]:
    return (await database.scalars(select(Match).order_by(Match.match_date_time))).all()


async def get_all_matches_display(database: AsyncSession):
    return (await database.execute(
        select(Match)
        .options(
            joinedload(Match.team1).joinedload(Team.franchise),  # Load team1's franchise
            joinedload(Match.team1).joinedload(Team.division),   # Load team1's division
            joinedload(Match.team2).joinedload(Team.franchise),  # Load team2's franchise
            joinedload(Match.team2).joinedload(Team.division)    # Load team2's division
        )
        .order_by(Match.match_date_time)
    )).scalars().all()


async def get_match(database: AsyncSession, match_id: int) -> Match:
    return (await database.scalars(select(Match).where(Match.id == match_id))).first()


async def create_match(database: AsyncSession, match: match_schema.MatchCreate) -> Match:

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
