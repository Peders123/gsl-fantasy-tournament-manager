from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import aliased

from thoth.models import Team, Franchise, Division


async def get_all_teams(database: AsyncSession) -> list[dict]:
    franchise_alias = aliased(Franchise)
    division_alias = aliased(Division)

    query = (
        select(
            Team.id,
            Team.franchise_id,
            franchise_alias.franchise_name,
            Team.division_id,
            division_alias.division_name
        )
        .join(franchise_alias, Team.franchise_id == franchise_alias.id)
        .join(division_alias, Team.division_id == division_alias.id)
    )

    result: list[Team] = (await database.execute(query)).all()

    # Convert result into a list of dictionaries
    teams = [
        {
            "id": row.id,
            "franchise_id": row.franchise_id,
            "franchise_name": row.franchise_name,
            "division_id": row.division_id,
            "division_name": row.division_name
        }
        for row in result
    ]
    return teams