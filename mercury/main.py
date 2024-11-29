from contextlib import asynccontextmanager

from fastapi import FastAPI

from dependencies import SESSIONMANAGER
from routers import captain, user


@asynccontextmanager
async def lifespan(app: FastAPI):

    yield
    await SESSIONMANAGER.close()


app = FastAPI()

app.include_router(captain.router)
app.include_router(user.router)
