from contextlib import asynccontextmanager

from fastapi import FastAPI

from dependencies import SESSIONMANAGER
from routers.captains import captain_router


@asynccontextmanager
async def lifespan(app: FastAPI):

    yield
    await SESSIONMANAGER.close()


app = FastAPI()

app.include_router(captain_router)
