from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from thoth.models import PlayerGameData
from thoth.schemas.player_game_data import PlayerGameDataCreate


async def get_all_player_game_data(database: AsyncSession) -> list[PlayerGameData]:
    return (await database.scalars(select(PlayerGameData))).all()


async def create_player_game_data(database: AsyncSession, player_game_data: PlayerGameDataCreate) -> PlayerGameData:

    db_player_game_data = PlayerGameData(
        game_id=player_game_data.game_id,
        player_id=player_game_data.player_id,
        kills=player_game_data.kills,
        deaths=player_game_data.deaths,
        assists=player_game_data.assists,
        gold_earned=player_game_data.gold_earned,
        damage_dealt=player_game_data.damage_dealt,
        damage_taken=player_game_data.damage_taken,
        damage_mitigated=player_game_data.damage_mitigated,
        structure_damage=player_game_data.structure_damage,
        player_healing=player_game_data.player_healing,
        wards_placed=player_game_data.wards_placed,
        mvp=player_game_data.mvp,
        god_id=player_game_data.god_id,
        active_1_id=player_game_data.active_1_id,
        active_2_id=player_game_data.active_2_id,
        item_1_id=player_game_data.item_1_id,
        item_2_id=player_game_data.item_2_id,
        item_3_id=player_game_data.item_3_id,
        item_4_id=player_game_data.item_4_id,
        item_5_id=player_game_data.item_5_id,
        item_6_id=player_game_data.item_6_id,
    )

    database.add(db_player_game_data)

    return db_player_game_data
