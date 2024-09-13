from typing import Annotated

from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.ext.asyncio import AsyncSession

from crud import user as crud_user
from dependencies import get_db_session
from schemas.user import UserView, UserCreate, UserUpdate


router = APIRouter(
    prefix='/user',
    tags=['User']
)


@router.get('/{user_id}', response_model=UserView)
async def read_user(user_id: int, database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await crud_user.get_user(database, user_id)


@router.get('/', response_model=list[UserView])
async def read_users(database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await crud_user.get_users(database)


@router.post('/', status_code=201, response_model=UserView)
async def write_user(user_data: UserCreate, database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await crud_user.create_user(database, user_data)


@router.delete('/{user_id}', status_code=204)
async def delete_user(user_id: str, database: Annotated[AsyncSession, Depends(get_db_session)]):
    db_user = await crud_user.get_user(database, user_id)
    if not db_user:
        raise HTTPException(status_code=404, detail='user not found')
    await crud_user.delete_user(database)


@router.put('/{user_id}', status_code=201)
async def replace_user(user_id: str, user_data: UserCreate,
                          database: Annotated[AsyncSession, Depends(get_db_session)]):
    db_user = await crud_user.get_user(database, user_id)
    if not db_user:
        raise HTTPException(status_code=404, detail='user not found')
    return await crud_user.replace_user(database, db_user, user_data)


@router.patch('/{user_id}')
async def update_user(user_id: str, user_data: UserUpdate,
                         database: Annotated[AsyncSession, Depends(get_db_session)]):
    db_user = await crud_user.get_user(database, user_id)
    if not db_user:
        raise HTTPException(status_code=404, detail='user not found')
    return await crud_user.update_user(database, db_user, user_data)
