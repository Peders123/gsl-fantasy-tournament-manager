from datetime import datetime

from sqlalchemy import select, func, case
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import joinedload, aliased

from thoth.crud.user_team import get_current_team_users
from thoth.models import Team, UserTeam, User, Match, Game
from thoth.schemas.team import TeamUsers, TeamDisplay


async def get_team_from_values(database: AsyncSession, division_id: int, franchise_id: int) -> Team:

    return (
        await database.scalars(
            select(Team).where(Team.division_id == division_id).where(Team.franchise_id == franchise_id)
        )
    ).first()


async def get_team(database: AsyncSession, team_id: int) -> TeamDisplay:

    return (
        await database.scalars(
            select(Team).where(Team.id == team_id).options(joinedload(Team.franchise), joinedload(Team.division))
        )
    ).first()


async def get_team_divisions(database: AsyncSession, division_id: int) -> list[TeamDisplay]:

    return (
        (
            await database.execute(
                select(Team).where(Team.division_id == division_id).options(joinedload(Team.franchise))
            )
        )
        .scalars()
        .all()
    )


async def get_team_standing_data(database: AsyncSession, team_id: int) -> list[TeamDisplay]:

    team = await get_team(database, team_id)

    matches_team1 = (await database.scalars(
        select(func.count(Match.id))
        .select_from(Team)
        .join(Match, (Team.id == Match.team1_id))
        .filter(
            Team.id == team_id,
            Match.match_date_time < datetime.now()
        )
    )).first()

    matches_team2 = (await database.scalars(
        select(func.count(Match.id))
        .select_from(Team)
        .join(Match, (Team.id == Match.team2_id))
        .filter(
            Team.id == team_id,
            Match.match_date_time < datetime.now()
        )
    )).first()

    wins = (
        await database.scalars(
            select(
                func.sum(case((Game.winning_team_id == Team.id, 1), else_=0))
            )
            .select_from(Team)
            .join(Match, (Team.id == Match.team1_id) | (Team.id == Match.team2_id))
            .join(Game, (Match.id == Game.match_id))
            .filter(Team.id == team_id)
        )
    ).first()

    losses = (
        await database.scalars(
            select(
                func.sum(case((Game.winning_team_id != Team.id, 1), else_=0))
            )
            .select_from(Team)
            .join(Match, (Team.id == Match.team1_id) | (Team.id == Match.team2_id))
            .join(Game, (Match.id == Game.match_id))
            .filter(Team.id == team_id)
        )
    ).first()

    team.matches = (matches_team1 if matches_team1 else 0) + (matches_team2 if matches_team2 else 0)
    team.wins = wins if wins else 0
    team.losses = losses if losses else 0
    team.point_difference = wins - losses

    return team


async def get_all_teams(database: AsyncSession) -> list[TeamDisplay]:

    return (
        (await database.execute(select(Team).options(joinedload(Team.franchise), joinedload(Team.division))))
        .scalars()
        .all()
    )


async def get_team_users(database: AsyncSession, team_id: int) -> Team:

    team = await get_team(database, team_id)
    team.users = await get_current_team_users(database, team_id)

    return team
