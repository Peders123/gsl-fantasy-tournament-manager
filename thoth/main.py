from contextlib import asynccontextmanager

from fastapi import FastAPI

from thoth.dependencies import SESSIONMANAGER
from thoth.routers import data, stats


@asynccontextmanager
async def lifespan(app: FastAPI):

    yield
    await SESSIONMANAGER.close()


app = FastAPI(lifespan=lifespan)
app.include_router(data.router, tags=["Data"])
app.include_router(stats.router, tags=["Stats"])
