from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from crud import captain
from dependencies import get_db
from schemas import CaptainView


router = APIRouter(
    prefix='/captain',
    tags=['captain']
)


@router.get('/captains/{captain_id}', response_model=CaptainView)
def read_captain(captain_id: int, db: Session = Depends(get_db)):
    return captain.get_captain(db, captain_id)


@router.get('/captains', response_model=list[CaptainView])
def read_captains(db: Session = Depends(get_db)):
    return captain.get_captains(db)
