from pydantic import BaseModel, ConfigDict


class _Franchise(BaseModel):

    franchise_name: str
    franchise_owner: str


class Franchise(_Franchise):

    model_config = ConfigDict(from_attributes=True)
    id: int


class FranchiseCreate(_Franchise):

    pass
