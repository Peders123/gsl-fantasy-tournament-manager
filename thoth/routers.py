from datetime import datetime, timedelta
from typing import Annotated

from fastapi import APIRouter, Depends, Response, status
from sqlalchemy.ext.asyncio import AsyncSession

from bin.utils.hirez_api import PcSmiteAPI
from thoth.dependencies import get_db_session
from thoth.models.scheduled import ScheduledTask
from thoth.utils.game_submission import schedule_game


router = APIRouter()


@router.get("/ping/")
async def ping():
    """Health test."""
    return {"message": "pong"}


@router.post("/match/")
async def add_match_id(
    match_id: int,
    order_team_id: int,
    chaos_team_id: int,
    response: Response,
    database: Annotated[AsyncSession, Depends(get_db_session)]
):

    api = PcSmiteAPI()
    api.ping()

    match = api.get_demo_details(match_id)
    players = api.get_match_details(match_id)

    match len(players):

        case 0:
            response.status_code = status.HTTP_404_NOT_FOUND

        case 1:
            await schedule_game(match_id, datetime.now() + timedelta(weeks=1), order_team_id, chaos_team_id, database)
            response.status_code = status.HTTP_202_ACCEPTED

        case _:
            response.status_code = status.HTTP_201_CREATED

    return [players, match]
