from typing import Annotated

from fastapi import APIRouter, Depends
from sqlalchemy.ext.asyncio import AsyncSession


from thoth.crud import division as division_crud, franchise as franchise_crud, user as user_crud, player as player_crud, team as team_crud
from thoth.schemas import division as division_schema, franchise as franchise_schema, user as user_schema, player as player_schema, team as team_schema
from thoth.dependencies import get_db_session


router = APIRouter()


@router.get("/division/", response_model=list[division_schema.Division])
async def get_divisions(database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await division_crud.get_all_divisions(database)


@router.get("/franchise/", response_model=list[franchise_schema.Franchise])
async def get_franchises(database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await franchise_crud.get_all_franchises(database)


@router.get("/user/", response_model=list[user_schema.User])
async def get_user(database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await user_crud.get_all_users(database)


@router.post("/user/", status_code=201, response_model=user_schema.UserCreate)
async def add_user(user_data: user_schema.UserCreate, database: Annotated[AsyncSession, Depends(get_db_session)]):
    new_user = await user_crud.create_user(database, user_data)
    await database.commit()
    return new_user


@router.get("/player/", response_model=list[player_schema.Player])
async def get_player(database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await player_crud.get_all_players(database)


@router.post("/player/", status_code=201, response_model=player_schema.PlayerCreate)
async def add_player(player_data: player_schema.PlayerCreate, database: Annotated[AsyncSession, Depends(get_db_session)]):
    new_player = await player_crud.create_player(database, player_data)
    await database.commit()
    return new_player


@router.get("/team/", response_model=list[team_schema.TeamDisplay])
async def get_teams(database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await team_crud.get_all_teams(database)
