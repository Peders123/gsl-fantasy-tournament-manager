from sqlalchemy.orm import Session

from models import Captain


def get_captain(database: Session, captain_id: int):

    return database.query(Captain).filter(Captain.captain_id==captain_id).first()


def get_captains(database: Session):

    return database.query(Captain).all()
