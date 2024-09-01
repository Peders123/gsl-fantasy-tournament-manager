from fastapi import FastAPI, HTTPException, Depends
from pydantic import BaseModel, Field
import models
from Database import engine, SessionLocal
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

@app.get("/")
def read_api(db: Session = Depends(get_db)):
    return db.query(models.Users).all()

USERS = []

@app.post("/")
def create_User(user: User, db:Session = Depends(get_db)):
    
    user_model = models.Users()
    user_model.id = user.id
    user_model.discord_name = user.discord_name
    

    db.add(user_model)
    db.commit()

    return user

@app.put("/{user_id}")
def update_User(user_id: str, user: User, db: Session = Depends(get_db)):

    user_model = db.query(models.Users).filter(models.Users.id == user_id).first()

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


@app.delete("/{user_id}")
def delete_user(user_id: str, db: Session = Depends(get_db)):
        
    user_model =  db.query(models.Users).filter(models.Users.id == user_id).first()


    if user_model is None:
        raise HTTPException(
            status_code=404,
            detail=f"ID {user_id} : Does not exist :(ↄc)"
        )
    
    db.query(models.Users).filter(models.Users.id == user_id).delete()

    db.commit()