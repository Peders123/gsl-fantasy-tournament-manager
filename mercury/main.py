from fastapi import FastAPI

from routers import captain, user


app = FastAPI()

app.include_router(captain.router)
app.include_router(user.router)
