from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from thoth.models import Player
from thoth.schemas.player import PlayerCreate


async def get_all_players(database: AsyncSession) -> list[Player]:
    return (await database.scalars(select(Player))).all()


async def get_specific_player(database: AsyncSession, player_id: str) -> Player:
    return (await database.scalars(select(Player).where(Player.id == player_id))).first()


async def create_player(database: AsyncSession, player: PlayerCreate) -> Player:

    db_player = Player(
        id=player.id,
        player_name=player.player_name,
        user_id=player.user_id,
    )

    database.add(db_player)

    return db_player
