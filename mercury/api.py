from fastapi import FastAPI, HTTPException, Depends
from pydantic import BaseModel, Field
import models
from database import engine, SessionLocal
from sqlalchemy.orm import Session

app = FastAPI()

models.Base.metadata.create_all(bind=engine)


def get_db():
    try:
        db = SessionLocal()
        yield db
    finally:
        db.close()
        


@app.get("/ping")
def ping():
    return ("Hey looks like all is well")


class User(BaseModel):
    id: str = Field(max_length=64)
    discord_name: str = Field(max_length=32)


class Tournament(BaseModel):
    # date
    title: str = Field(min_length=1, max_length=64)
    description: str = Field(max_length=256)


class Captain(BaseModel):
    tournament_id: int = Field()
    user_id: str = Field()
    smite_name: str = Field()
    team_name: str = Field()
    reason: str = Field()
    captain_budget: int = Field()


class Player(BaseModel):
    tournament_id: int = Field()
    user_id: str = Field()
    captain_id: str = Field()
    smite_name: str = Field()
    role_1: str = Field()
    role_2: str = Field()
    smite_guru: str = Field()
    estimated_value: int = Field()


@app.get("/Users")
def read_api(db: Session = Depends(get_db)):
    return db.query(models.Users).all()


@app.post("/Users")
def create_User(user: User, db: Session = Depends(get_db)):

    user_model = models.Users()
    user_model.id = user.id
    user_model.discord_name = user.discord_name

    db.add(user_model)
    db.commit()

    return user


@app.put("/Users/{user_id}")
def update_User(user_id: str, user: User, db: Session = Depends(get_db)):

    user_model = db.query(models.Users).filter(
        models.Users.id == user_id
        ).first()

    if user_model is None:
        raise HTTPException(
            status_code=404,
            detail=f"ID {user_id} : Does not exist :(ↄc)"
        )

    user_model.id = user.id
    user_model.discord_name = user.discord_name

    db.add(user_model)
    db.commit()

    return user


@app.delete("/Users/{user_id}")
def delete_user(user_id: str, db: Session = Depends(get_db)):

    user_model = db.query(models.Users).filter(
        models.Users.id == user_id
        ).first()

    if user_model is None:
        raise HTTPException(
            status_code=404,
            detail=f"ID {user_id} : Does not exist :(ↄc)"
        )

    db.query(models.Users).filter(models.Users.id == user_id).delete()

    db.commit()


@app.get("/Tournaments")
def read_api_Tournaments(db: Session = Depends(get_db)):
    return db.query(models.Tournaments).all()


@app.post("/Tournaments")
def create_Tournament(tournament: Tournament, db: Session = Depends(get_db)):

    tournament_model = models.Tournaments()
    tournament_model.title = tournament.title
    tournament_model.description = tournament.description

    db.add(tournament_model)
    db.commit()

    return tournament


@app.put("/Tournaments/{tournament_id}")
def update_Tournament(
    tournament_id: int, tournament: Tournament, db: Session = Depends(get_db)
        ):

    tournament_model = db.query(models.Tournaments).filter(
        models.Tournaments.id == tournament_id
        ).first()

    if tournament_model is None:
        raise HTTPException(
            status_code=404,
            detail=f"ID {tournament_id} : Does not exist :(ↄc)"
        )

    tournament_model.title = tournament.title
    tournament_model.description = tournament.description

    db.add(tournament_model)
    db.commit()

    return tournament


@app.delete("/Tournaments/{tournament_id}")
def delete_tournament(tournament_id: int, db: Session = Depends(get_db)):

    tournament_model = db.query(models.Tournaments).filter(
        models.Tournaments.id == tournament_id
        ).first()

    if tournament_model is None:
        raise HTTPException(
            status_code=404,
            detail=f"ID {tournament_id} : Does not exist :(ↄc)"
        )

    db.query(models.Tournaments).filter(
        models.Tournaments.id == tournament_id
        ).delete()

    db.commit()


@app.get("/Captains")
def read_api_Captains(db: Session = Depends(get_db)):
    return db.query(models.Captains).all()


@app.post("/Captains")
def create_Captain(captain: Captain, db: Session = Depends(get_db)):

    captain_model = models.Captains()
    captain_model.tournament_id = captain.tournament_id
    captain_model.user_id = captain.user_id
    captain_model.smite_name = captain.smite_name
    captain_model.team_name = captain.team_name
    captain_model.reason = captain.reason
    captain_model.captain_budget = captain.captain_budget

    db.add(captain_model)
    db.commit()

    return captain


@app.put("/Captains/{captain_id}")
def update_Captain(
    captain_id: int, captain: Captain, db: Session = Depends(get_db)
        ):

    captain_model = db.query(models.Captains).filter(
        models.Captains.id == captain_id
        ).first()

    if captain_model is None:
        raise HTTPException(
            status_code=404,
            detail=f"ID {captain_id} : Does not exist :(ↄc)"
        )

    captain_model.tournament_id = captain.tournament_id
    captain_model.user_id = captain.user_id
    captain_model.smite_name = captain.smite_name
    captain_model.team_name = captain.team_name
    captain_model.reason = captain.reason
    captain_model.captain_budget = captain.captain_budget

    db.add(captain_model)
    db.commit()

    return captain


@app.delete("/Captains/{captain_id}")
def delete_captain(captain_id: int, db: Session = Depends(get_db)):

    captain_model = db.query(models.Captains).filter(
        models.Captains.id == captain_id
        ).first()

    if captain_model is None:
        raise HTTPException(
            status_code=404,
            detail=f"ID {captain_id} : Does not exist :(ↄc)"
        )

    db.query(models.Captains).filter(models.Captains.id == captain_id).delete()

    db.commit()


@app.get("/Players")
def read_api_PLayers(db: Session = Depends(get_db)):
    return db.query(models.Players).all()


@app.post("/Players")
def create_Player(player: Player, db: Session = Depends(get_db)):

    player_model = models.Players()
    player_model.tournament_id = player.tournament_id
    player_model.user_id = player.user_id
    player_model.smite_name = player.smite_name
    player_model.role_1 = player.role_1
    player_model.role_2 = player.role_2
    player_model.smite_guru = player.smite_guru
    player_model.estimated_value = player.estimated_value

    db.add(player_model)
    db.commit()

    return player


@app.put("/Players/{player_id}")
def update_Player(
    player_id: int, player: Player, db: Session = Depends(get_db)
        ):

    player_model = db.query(models.Players).filter(
        models.Players.id == player_id
        ).first()

    if player_model is None:
        raise HTTPException(
            status_code=404,
            detail=f"ID {player_id} : Does not exist :(ↄc)"
        )

    player_model.tournament_id = player.tournament_id
    player_model.user_id = player.user_id
    player_model.smite_name = player.smite_name
    player_model.role_1 = player.role_1
    player_model.role_2 = player.role_2
    player_model.smite_guru = player.smite_guru
    player_model.estimated_value = player.estimated_value

    db.add(player_model)
    db.commit()

    return player


@app.delete("/Players/{player_id}")
def delete_player(player_id: int, db: Session = Depends(get_db)):

    player_model = db.query(models.Players).filter(
        models.Players.id == player_id
        ).first()

    if player_model is None:
        raise HTTPException(
            status_code=404,
            detail=f"ID {player_id} : Does not exist :(ↄc)"
        )
    db.query(models.Players).filter(models.Players.id == player_id).delete()

    db.commit()
