from typing import Annotated

from fastapi import APIRouter, Depends
from sqlalchemy.ext.asyncio import AsyncSession


from thoth.crud import division as division_crud, franchise as franchise_crud
from thoth.schemas import division as division_schema, franchise as franchise_schema
from thoth.dependencies import get_db_session


router = APIRouter()


@router.get("/division/", response_model=list[division_schema.Division])
async def get_divisions(database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await division_crud.get_all_divisions(database)


@router.get("/franchise/", response_model=list[franchise_schema.Franchise])
async def get_franchises(database: Annotated[AsyncSession, Depends(get_db_session)]):
    return await franchise_crud.get_all_franchises(database)
