import asyncio

from datetime import datetime, timedelta

from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from thoth.models.scheduled import ScheduledTask
from thoth.utils.database import DatabaseSessionManager, construct_host_url


SESSION_MANAGER = DatabaseSessionManager(construct_host_url("postgres"))


async def schedule_game(
    match_id: int, scheduled_date: datetime, order_team_id: int, chaos_team_id: int, database: AsyncSession
):

    task = ScheduledTask(
        id=match_id, scheduled_date=scheduled_date, order_team_id=order_team_id, chaos_team_id=chaos_team_id
    )
    database.add(task)
    await database.commit()


async def main():

    async with SESSION_MANAGER.session() as session:

        now = datetime.now() + timedelta(weeks=2)

        tasks: list[ScheduledTask] = (await session.execute(select(ScheduledTask).where(ScheduledTask.scheduled_date < now))).all()[0]

        for task in tasks:
            print(task.id)


if __name__ == "__main__":

    asyncio.run(main())
