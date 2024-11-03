from pydantic import BaseModel, ConfigDict


class _Division(BaseModel):

    division_name: str
    division_rank: int


class Division(_Division):

    model_config = ConfigDict(from_attributes=True)
    id: int


class DivisionCreate(_Division):

    pass
