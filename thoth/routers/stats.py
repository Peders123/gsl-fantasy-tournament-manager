from datetime import datetime, timedelta
from typing import Annotated

from fastapi import APIRouter, Depends, Response, status
from sqlalchemy.ext.asyncio import AsyncSession

from bin.utils.hirez_api import PcSmiteAPI
from thoth.crud import match as match_crud, game as game_crud, player_game_data as game_data_crud
from thoth.dependencies import get_db_session
from thoth.schemas import match as match_schema, game as game_schema, player_game_data as game_data_schema
from thoth.utils.game_submission import schedule_game, write_game, write_player_data


router = APIRouter()


@router.get("/ping/")
async def ping():
    """Health test."""
    return {"message": "pong"}


@router.post("/match/", status_code=201, response_model=match_schema.Match)
async def add_match(match_data: match_schema.MatchCreate, database: Annotated[AsyncSession, Depends(get_db_session)]):
    new_match = await match_crud.create_match(database, match_data)
    await database.commit()
    await database.refresh(new_match)
    return new_match


@router.get("/match/", response_model=list[match_schema.Match])
async def get_all_matches(database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await match_crud.get_all_matches(database)


@router.get("/match/display/", response_model=list[match_schema.MatchDisplay])
async def get_matches_for_display(database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await match_crud.get_all_matches_display(database)


@router.get("/match/{match_id}/", response_model=match_schema.MatchDetailed)
async def get_match_detailed(match_id: int, database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await match_crud.get_match(database, match_id)


@router.get("/game/{game_id}/", response_model=game_schema.GameDetailed)
async def get_game_details(game_id: int, database: Annotated[AsyncSession, Depends(get_db_session)]):
    game = game_schema.GameDetailed(**(await game_crud.get_game(database, game_id)).__dict__)
    game.calculate_gpm_for_all_players()
    return game


@router.post("/game/create/", response_model=game_schema.Game)
async def create_game(game_data: game_schema.GameCreate, database: Annotated[AsyncSession, Depends(get_db_session)]):
    new_game = await game_crud.create_game(database, game_data)
    await database.commit()
    await database.refresh(new_game)
    return new_game


@router.post("/player_game_data/", response_model=game_data_schema.PlayerGameData)
async def create_player_data(player_game_data: game_data_schema.PlayerGameDataCreate, database: Annotated[AsyncSession, Depends(get_db_session)]):
    new_player_data = await game_data_crud.create_player_game_data(database, player_game_data)
    await database.commit()
    await database.refresh(new_player_data)
    return new_player_data


@router.post("/game/")
async def add_game_data(
    game_id: int,
    order_team_id: int,
    chaos_team_id: int,
    match_id: int,
    response: Response,
    database: Annotated[AsyncSession, Depends(get_db_session)]
):
    
    match: match_schema.Match = await match_crud.get_match_basic(database, match_id)

    if not match:
        response.status_code = status.HTTP_404_NOT_FOUND
        return {"error": "Match id not found"}
    
    match_teams = {match.team1_id, match.team2_id}

    for team_id in [order_team_id, chaos_team_id]:
        if team_id not in match_teams:
            response.status_code = status.HTTP_400_BAD_REQUEST
            return {"error": f"Team {team_id} not in Match {match_id}"}

    api = PcSmiteAPI()
    api.ping()

    game = api.get_demo_details(game_id)[0]
    players = api.get_match_details(game_id)

    match len(players):

        case 0:
            response.status_code = status.HTTP_404_NOT_FOUND
            return {"error": "Game id not found"}

        case 1:
            await schedule_game(game_id, datetime.now() + timedelta(weeks=1), order_team_id, chaos_team_id, match_id, database)
            response.status_code = status.HTTP_202_ACCEPTED
            await database.commit()
            return {"message": f"Game id {game_id} scheduled for {datetime.now() + timedelta(weeks=1)}"}

        case _:
            await write_game(game, players[0], order_team_id, chaos_team_id, match_id, database)
            created, unknown = await write_player_data(game_id, players, database, match.team1_id, match.team2_id)
            response.status_code = status.HTTP_201_CREATED

            await database.commit()

            return {
                "created_players": created,
                "unknown_players": unknown
            }


@router.get("/check_game_data/")
async def get_game_data(
    game_id: int
):
    
    api = PcSmiteAPI()
    api.ping()

    game = api.get_demo_details(game_id)[0]

    return game


@router.get("/check_player_data/")
async def get_player_data(
    game_id: int
):
    
    api = PcSmiteAPI()
    api.ping()

    players = api.get_match_details(game_id)

    return players
