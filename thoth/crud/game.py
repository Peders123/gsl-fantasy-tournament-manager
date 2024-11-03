from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import joinedload

from thoth.models import Game
from thoth.schemas.game import GameCreate, GameDetailed


async def get_all_games(database: AsyncSession) -> list[Game]:
    return (await database.scalars(select(Game))).all()


async def get_game(database: AsyncSession, game_id: int) -> Game:

    return (
        await database.scalars(
            select(Game)
            .where(Game.id == game_id)
            .options(
                joinedload(Game.total_player_data),
            )
        )
    ).first()


async def create_game(database: AsyncSession, game: GameCreate) -> Game:

    db_game = Game(
        id=game.id,
        match_id=game.match_id,
        date_time=game.date_time,
        order_team_id=game.order_team_id,
        chaos_team_id=game.chaos_team_id,
        winning_team_id=game.winning_team_id,
        match_duration=game.match_duration,
        ban_1=game.ban_1,
        ban_2=game.ban_2,
        ban_3=game.ban_3,
        ban_4=game.ban_4,
        ban_5=game.ban_5,
        ban_6=game.ban_6,
        ban_7=game.ban_7,
        ban_8=game.ban_8,
        ban_9=game.ban_9,
        ban_10=game.ban_10,
    )

    database.add(db_game)

    return db_game


async def update_game(database: AsyncSession, game_id: int, game: GameCreate) -> Game:

    db_game = await database.get(Game, game_id)

    db_game.match_id = game.match_id
    db_game.date_time = game.date_time
    db_game.order_team_id = game.order_team_id
    db_game.chaos_team_id = game.chaos_team_id
    db_game.winning_team_id = game.winning_team_id
    db_game.match_duration = game.match_duration
    db_game.ban_1 = game.ban_1
    db_game.ban_2 = game.ban_2
    db_game.ban_3 = game.ban_3
    db_game.ban_4 = game.ban_4
    db_game.ban_5 = game.ban_5
    db_game.ban_6 = game.ban_6
    db_game.ban_7 = game.ban_7
    db_game.ban_8 = game.ban_8
    db_game.ban_9 = game.ban_9
    db_game.ban_10 = game.ban_10

    return db_game
