from typing import Annotated

from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from sqlalchemy.ext.asyncio import AsyncSession

from crud import captain
from dependencies import get_db_session
from schemas import CaptainView


captain_router = APIRouter(
    prefix='/captain',
    tags=['captain']
)


@captain_router.get('/{captain_id}', response_model=CaptainView)
async def read_captain(captain_id: int, database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await captain.get_captain(database, captain_id)


@captain_router.get('/', response_model=list[CaptainView])
async def read_captains(database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await captain.get_captains(database)
