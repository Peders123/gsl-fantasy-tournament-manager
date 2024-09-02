from fastapi import FastAPI
app = FastAPI()


@app.get("/ping")
def ping():
    return ("Hey looks like all is well")
