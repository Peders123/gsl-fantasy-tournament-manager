import asyncio

from datetime import datetime, timedelta

from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from bin.utils.hirez_api import PcSmiteAPI
from thoth.crud import game as game_crud
from thoth.models.scheduled import ScheduledTask
from thoth.schemas import game as game_schema
from thoth.utils.database import DatabaseSessionManager, construct_host_url


async def schedule_game(
    game_id: int,
    scheduled_date: datetime,
    order_team_id: int,
    chaos_team_id: int,
    match_id: int,
    database: AsyncSession
):
    task = ScheduledTask(
        id=game_id,
        scheduled_date=scheduled_date,
        order_team_id=order_team_id,
        chaos_team_id=chaos_team_id,
        match_id=match_id
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


async def main():

    manager = DatabaseSessionManager(construct_host_url("postgres"))

    async with manager.session() as session:

        now = datetime.now() + timedelta(weeks=2)

        tasks: list[ScheduledTask] = (await session.execute(select(ScheduledTask).where(ScheduledTask.scheduled_date < now))).all()[0]

        for task in tasks:
            api = PcSmiteAPI()
            api.ping()
            game = api.get_demo_details(task.id)[0]
            players = api.get_match_details(task.id)
            await write_game(game, players[0], task.order_team_id, task.chaos_team_id, task.match_id, session)

    await manager.close()


if __name__ == "__main__":

    asyncio.run(main())
