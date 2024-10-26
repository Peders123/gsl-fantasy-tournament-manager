from datetime import timezone

from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import joinedload

from thoth.models import Match, Team
from thoth.schemas import match as match_schema


async def get_all_matches(database: AsyncSession) -> list[Match]:
    return (await database.scalars(select(Match).order_by(Match.match_date_time))).all()


async def get_match_basic(database: AsyncSession, match_id: int) -> match_schema.Match:
    return (await database.scalars(select(Match).where(Match.id == match_id))).first()


async def get_all_matches_display(database: AsyncSession):
    return (await database.scalars(
        select(Match)
        .options(
            joinedload(Match.team1).joinedload(Team.franchise),
            joinedload(Match.team1).joinedload(Team.division),
            joinedload(Match.team2).joinedload(Team.franchise),
            joinedload(Match.team2).joinedload(Team.division)
        )
        .order_by(Match.match_date_time)
    )).all()


async def get_match(database: AsyncSession, match_id: int) -> match_schema.MatchDetailed:
    return (await database.scalars(
        select(Match)
        .options(
            joinedload(Match.team1).joinedload(Team.franchise),
            joinedload(Match.team1).joinedload(Team.division),
            joinedload(Match.team2).joinedload(Team.franchise),
            joinedload(Match.team2).joinedload(Team.division),
            joinedload(Match.games)
        )
        .where(Match.id == match_id)
        .order_by(Match.match_date_time)
    )).first()


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
