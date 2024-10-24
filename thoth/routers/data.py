from typing import Annotated

from fastapi import APIRouter, Depends
from sqlalchemy.ext.asyncio import AsyncSession


from thoth.crud import division as division_crud, franchise as franchise_crud, user as user_crud, player as player_crud, team as team_crud, user_team as user_team_crud
from thoth.schemas import division as division_schema, franchise as franchise_schema, user as user_schema, player as player_schema, team as team_schema, user_team as user_team_schema
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


@router.post("/user/", status_code=201, response_model=user_schema.User)
async def add_user(user_data: user_schema.UserCreate, database: Annotated[AsyncSession, Depends(get_db_session)]):
    new_user = await user_crud.create_user(database, user_data)
    await database.commit()
    await database.refresh(new_user)
    return new_user


@router.post("/user/team/", status_code=201, response_model=user_team_schema.UserTeam)
async def get_user_team(user_team_data: user_team_schema.UserTeamCreate, database: Annotated[AsyncSession, Depends(get_db_session)]):
    new_user_team = await user_team_crud.create_user_team(database, user_team_data)
    await database.commit()
    await database.refresh(new_user_team)
    return new_user_team


@router.get("/team/users/", response_model=list[team_schema.TeamUsers])
async def get_team_users(database: Annotated[AsyncSession, Depends(get_db_session)]):
    return [await team_crud.get_team_users(database, team.id) for team in await team_crud.get_all_teams(database)]


@router.get("/player/", response_model=list[player_schema.Player])
async def get_player(database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await player_crud.get_all_players(database)


@router.get("/player/unassigned/", response_model=list[player_schema.Player])
async def get_unassigned_players(database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await player_crud.get_unassigned_players(database)


@router.patch("/player/{player_id}/set_user/", response_model=player_schema.Player)
async def set_player_user_id(player_id: str, user_id: int, database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await player_crud.set_player_user(database, player_id, user_id)  


@router.post("/player/", status_code=201, response_model=player_schema.Player)
async def add_player(player_data: player_schema.PlayerCreate, database: Annotated[AsyncSession, Depends(get_db_session)]):
    new_player = await player_crud.create_player(database, player_data)
    await database.commit()
    await database.refresh(new_player)
    return new_player


@router.get("/team/", response_model=list[team_schema.TeamDisplay])
async def get_teams(database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await team_crud.get_all_teams(database)


@router.get("/team/{division_id}/{franchise_id}", response_model=team_schema.Team)
async def get_teams(division_id: int, franchise_id: int, database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await team_crud.get_team_from_values(database, division_id, franchise_id)
