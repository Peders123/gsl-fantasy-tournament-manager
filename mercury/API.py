from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field
import models
from Database import engine, SessionLocal
from sqlalchemy.orm import Session

app = FastAPI()

models.Base.metadata.create_all(bind=engine)

@app.get("/ping")
def ping():
    return ("Hey looks like all is well")

class User(BaseModel):
    id: str = Field(max_length=64)
    discord_name: str = Field(max_length=32)

@app.get("/")
def read_api():
    return USERS

USERS = []

@app.post("/")
def create_User(user: User):
    USERS.append(user)
    return user

@app.put("/{user_id}")
def update_User(user_id: str, user: User):
    counter = 0

    for x in USERS:
        counter = counter + 1
        if x.id == user_id:
            USERS[counter - 1] = user
            return USERS[counter - 1]
    raise HTTPException(
        status_code=404,
        detail=f"ID {user_id} : Does not exist :(ↄc)"
    )

@app.delete("/{book_id}")
def delete_user(user_id: str):
        counter = 0

        for x in USERS:
            counter = counter + 1
            if x.id == user_id:
                del USERS[counter - 1]
                return f"ID: {user_id} deleted"
        raise HTTPException(
        status_code=404,
        detail=f"ID {user_id} : Does not exist :(ↄc)"
    )