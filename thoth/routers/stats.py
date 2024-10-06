from datetime import datetime, timedelta
from typing import Annotated

from fastapi import APIRouter, Depends, Response, status
from sqlalchemy.ext.asyncio import AsyncSession

from bin.utils.hirez_api import PcSmiteAPI
from thoth.crud import match as match_crud
from thoth.dependencies import get_db_session
from thoth.schemas import match as match_schema
from thoth.utils.game_submission import schedule_game, write_game


router = APIRouter()


@router.get("/ping/")
async def ping():
    """Health test."""
    return {"message": "pong"}


@router.post("/match/", status_code=201, response_model=match_schema.MatchCreate)
async def add_match(match_data: match_schema.MatchCreate, database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await match_crud.create_match(database, match_data)


@router.post("/game/")
async def add_game_data(
    game_id: int,
    order_team_id: int,
    chaos_team_id: int,
    match_id: int,
    response: Response,
    database: Annotated[AsyncSession, Depends(get_db_session)]
):

    api = PcSmiteAPI()
    api.ping()

    game = api.get_demo_details(game_id)[0]
    players = api.get_match_details(game_id)

    match len(players):

        case 0:
            response.status_code = status.HTTP_404_NOT_FOUND

        case 1:
            await schedule_game(game_id, datetime.now() + timedelta(weeks=1), order_team_id, chaos_team_id, match_id, database)
            response.status_code = status.HTTP_202_ACCEPTED

        case _:
            await write_game(game, players[0], order_team_id, chaos_team_id, match_id, database)
            response.status_code = status.HTTP_201_CREATED

    return players
