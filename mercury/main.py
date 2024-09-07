from fastapi import Depends, FastAPI
from sqlalchemy.orm import Session

from crud import captain
from dependencies import get_db
from schemas import CaptainView


app = FastAPI()


@app.get("/captains", response_model=list[CaptainView])
def read_captains(db: Session = Depends(get_db)):

    captains = captain.get_captains(db)

    print(captains[0].tournament_id_id)

    return captains