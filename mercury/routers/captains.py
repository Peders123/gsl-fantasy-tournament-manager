from typing import Annotated

from fastapi import APIRouter, Depends
from sqlalchemy.ext.asyncio import AsyncSession

from crud import captain as captain_crud
from dependencies import get_db_session
from schemas import CaptainView, CaptainCreate


captain_router = APIRouter(
    prefix='/captain',
    tags=['captain']
)


@captain_router.get('/{captain_id}', response_model=CaptainView)
async def read_captain(captain_id: int, database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await captain_crud.get_captain(database, captain_id)


@captain_router.get('/', response_model=list[CaptainView])
async def read_captains(database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await captain_crud.get_captains(database)


@captain_router.post('/', status_code=201, response_model=CaptainView)
async def write_captain(captain: CaptainCreate, database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await captain_crud.create_captain(database, captain)
