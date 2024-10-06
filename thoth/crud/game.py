from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from thoth.models import Game
from thoth.schemas.game import GameCreate


async def get_all_games(database: AsyncSession) -> list[Game]:
    return (await database.scalars(select(Game))).all()


async def create_game(database: AsyncSession, match: GameCreate) -> Game:

    db_match = Game(
        id=match.id,
        match_id=match.match_id,
        date_time=match.date_time,
        order_team_id=match.order_team_id,
        chaos_team_id=match.chaos_team_id,
        winning_team_id=match.winning_team_id,
        match_duration=match.match_duration,
        ban_1=match.ban_1,
        ban_2=match.ban_2,
        ban_3=match.ban_3,
        ban_4=match.ban_4,
        ban_5=match.ban_5,
        ban_6=match.ban_6,
        ban_7=match.ban_7,
        ban_8=match.ban_8,
        ban_9=match.ban_9,
        ban_10=match.ban_10,
    )

    database.add(db_match)
    await database.commit()
    await database.refresh(db_match)

    return db_match
