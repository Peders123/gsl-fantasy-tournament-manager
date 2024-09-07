from fastapi import FastAPI

from routers.captains import captain_router


app = FastAPI()

app.include_router(captain_router)
