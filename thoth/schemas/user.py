from pydantic import BaseModel, ConfigDict


class _User(BaseModel):

    discord_name: str


class User(_User):

    model_config = ConfigDict(from_attributes=True)
    id: int


class UserCreate(_User):

    pass
