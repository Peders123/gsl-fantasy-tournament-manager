import asyncio

from datetime import datetime, timedelta

from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from bin.utils.hirez_api import PcSmiteAPI
from thoth.crud import (
    game as game_crud,
    player_game_data as player_game_crud,
    player as player_crud,
    match as match_crud,
)
from thoth.models.scheduled import ScheduledTask
from thoth.schemas import game as game_schema, player_game_data as player_game_schema, player as player_schema
from thoth.utils.database import DatabaseSessionManager, construct_host_url


async def schedule_game(
    game_id: int,
    scheduled_date: datetime,
    order_team_id: int,
    chaos_team_id: int,
    match_id: int,
    database: AsyncSession,
):
    task = ScheduledTask(
        id=game_id,
        scheduled_date=scheduled_date,
        order_team_id=order_team_id,
        chaos_team_id=chaos_team_id,
        match_id=match_id,
    )
    database.add(task)
    await database.commit()


async def write_game(
    game: dict, ban_data: dict, order_team_id: int, chaos_team_id: int, match_id: int, database: AsyncSession
):
    winning_team_id = order_team_id if game["Winning_Team"] == 1 else chaos_team_id

    game_data = game_schema.GameCreate(
        id=game["Match"],
        match_id=match_id,
        date_time=datetime.strptime(game["Entry_Datetime"], "%m/%d/%Y %I:%M:%S %p"),
        order_team_id=order_team_id,
        chaos_team_id=chaos_team_id,
        winning_team_id=winning_team_id,
        match_duration=game["Match_Time"],
        ban_1=ban_data["Ban1Id"],
        ban_2=ban_data["Ban2Id"],
        ban_3=ban_data["Ban3Id"],
        ban_4=ban_data["Ban4Id"],
        ban_5=ban_data["Ban5Id"],
        ban_6=ban_data["Ban6Id"],
        ban_7=ban_data["Ban7Id"],
        ban_8=ban_data["Ban8Id"],
        ban_9=ban_data["Ban9Id"],
        ban_10=ban_data["Ban10Id"],
    )

    await game_crud.create_game(database, game_data)


async def write_player_data(
    game_id: int, total_player_data: list[dict], database: AsyncSession, order_team_id: int, chaos_team_id: int
) -> tuple[list[str], list[int]]:

    unknown_players: list[int] = []
    created_players: list[str] = []

    def parse_item_id(id: int):
        return None if id == 0 else id

    for player_num, player_data in enumerate(total_player_data):

        player_id = None if player_data["playerId"] == "0" else player_data["playerId"]

        if player_id:
            if await create_player(player_id, player_data["playerName"], database):
                created_players.append(player_id)
        else:
            unknown_players.append(player_num)

        player_game_data = player_game_schema.PlayerGameDataCreate(
            game_id=game_id,
            player_id=player_id,
            kills=player_data["Kills_Player"],
            deaths=player_data["Deaths"],
            assists=player_data["Assists"],
            gold_earned=player_data["Gold_Earned"],
            damage_dealt=player_data["Damage_Player"],
            damage_taken=player_data["Damage_Taken"],
            damage_mitigated=player_data["Damage_Mitigated"],
            structure_damage=player_data["Structure_Damage"],
            player_healing=player_data["Healing"],
            wards_placed=player_data["Wards_Placed"],
            god_id=player_data["GodId"],
            active_1_id=parse_item_id(player_data["ActiveId1"]),
            active_2_id=parse_item_id(player_data["ActiveId2"]),
            item_1_id=parse_item_id(player_data["ItemId1"]),
            item_2_id=parse_item_id(player_data["ItemId2"]),
            item_3_id=parse_item_id(player_data["ItemId3"]),
            item_4_id=parse_item_id(player_data["ItemId4"]),
            item_5_id=parse_item_id(player_data["ItemId5"]),
            item_6_id=parse_item_id(player_data["ItemId6"]),
            team_id=order_team_id if player_data["TaskForce"] == 1 else chaos_team_id,
        )

        await player_game_crud.create_player_game_data(database, player_game_data)

    return created_players, unknown_players


async def create_player(player_id: str, player_name: str, database: AsyncSession) -> bool:

    if not await player_crud.get_specific_player(database, player_id):

        player = player_schema.PlayerCreate(id=player_id, player_name=player_name)

        await player_crud.create_player(database, player)

        return True

    return False


async def main():

    manager = DatabaseSessionManager(construct_host_url("postgres"))

    async with manager.session() as session:

        now = datetime.now() + timedelta(weeks=2)

        tasks: list[ScheduledTask] = (
            await session.execute(select(ScheduledTask).where(ScheduledTask.scheduled_date < now))
        ).all()[0]

        for task in tasks:
            api = PcSmiteAPI()
            api.ping()
            game = api.get_demo_details(task.id)[0]
            players = api.get_match_details(task.id)
            match = await match_crud.get_match_basic(session, task.match_id)
            await write_game(game, players[0], task.order_team_id, task.chaos_team_id, task.match_id, session)
            await write_player_data(task.id, players, session, match.team1_id, match.team2_id)

        await session.commit()

    await manager.close()


if __name__ == "__main__":

    asyncio.run(main())
