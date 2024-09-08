from fastapi import FastAPI

from mercury.routers.captain import captain_router


app = FastAPI()

app.include_router(captain_router)
