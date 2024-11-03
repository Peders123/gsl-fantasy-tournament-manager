import json

from contextlib import asynccontextmanager
from fastapi import FastAPI, Request
from fastapi.responses import JSONResponse
from starlette.middleware.base import BaseHTTPMiddleware

from thoth.dependencies import SESSIONMANAGER
from thoth.routers import data, stats


with open("/data/thoth/secrets.json") as secrets:
    SECRETS = json.load(secrets)


class AuthMiddleware(BaseHTTPMiddleware):
    async def dispatch(self, request: Request, call_next):
        auth_header = request.headers.get("Authorization")

        if auth_header != f"{SECRETS['tokens']['secret-key']['thoth']}":
            return JSONResponse(
                {"detail": "Unauthorized"},
                status_code=401
            )

        response = await call_next(request)
        return response


@asynccontextmanager
async def lifespan(app: FastAPI):

    yield
    await SESSIONMANAGER.close()


app = FastAPI(lifespan=lifespan)
app.include_router(data.router, tags=["Data"])
app.include_router(stats.router, tags=["Stats"])
app.add_middleware(AuthMiddleware)
