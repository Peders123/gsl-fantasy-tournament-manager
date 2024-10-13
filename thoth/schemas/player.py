from pydantic import BaseModel, ConfigDict


class _Player(BaseModel):

    id: str
    player_name: str
    user_id: int | None = None


class Player(_Player):

    model_config = ConfigDict(from_attributes=True)


class PlayerCreate(_Player):

    pass
