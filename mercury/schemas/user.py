from pydantic import BaseModel, ConfigDict


class _User(BaseModel):

    id: str
    discord_name: str


class UserView(_User):

    model_config = ConfigDict(from_attributes=True)


class UserCreate(_User):
    pass


class UserUpdate(_User):

    id: str | None = None
    discord_name: str | None = None
