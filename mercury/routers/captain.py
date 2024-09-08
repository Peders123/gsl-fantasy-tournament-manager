from typing import Annotated

from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.ext.asyncio import AsyncSession

from crud import captain as crud_captain
from dependencies import get_db_session
from schemas.captain import CaptainView, CaptainCreate, CaptainUpdate


captain_router = APIRouter(
    prefix='/captain',
    tags=['captain']
)


@captain_router.get('/{captain_id}', response_model=CaptainView)
async def read_captain(captain_id: int, database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await crud_captain.get_captain(database, captain_id)


@captain_router.get('/', response_model=list[CaptainView])
async def read_captains(database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await crud_captain.get_captains(database)


@captain_router.post('/', status_code=201, response_model=CaptainView)
async def write_captain(captain_data: CaptainCreate, database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await crud_captain.create_captain(database, captain_data)


@captain_router.delete('/{captain_id}', status_code=204)
async def delete_captain(captain_id: int, database: Annotated[AsyncSession, Depends(get_db_session)]):
    db_captain = await crud_captain.get_captain(database, captain_id)
    if not db_captain:
        raise HTTPException(status_code=404, detail='Captain not found')
    await crud_captain.delete_captain(database)


@captain_router.put('/{captain_id}', status_code=201)
async def replace_captain(captain_id: int, captain_data: CaptainCreate,
                          database: Annotated[AsyncSession, Depends(get_db_session)]):
    db_captain = await crud_captain.get_captain(database, captain_id)
    if not db_captain:
        raise HTTPException(status_code=404, detail='Captain not found')
    return await crud_captain.replace_captain(database, db_captain, captain_data)


@captain_router.patch('/{captain_id}')
async def update_captain(captain_id: int, captain_data: CaptainUpdate,
                         database: Annotated[AsyncSession, Depends(get_db_session)]):
    db_captain = await crud_captain.get_captain(database, captain_id)
    if not db_captain:
        raise HTTPException(status_code=404, detail='Captain not found')
    return await crud_captain.update_captain(database, db_captain, captain_data)
