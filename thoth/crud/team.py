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

    team.matches = (await database.scalars(
        select(func.count(Match.id))
        .select_from(Team)
        .join(Match, (Team.id == Match.team1_id) | (Team.id == Match.team2_id))
        .filter(Team.id == team_id)
    )).first()

    wins = (
        await database.scalars(
            select(
                func.sum(case((Game.winning_team_id == Team.id, 1), else_=0))
            )
            .select_from(Team)
            .join(Game, (Team.id == Game.order_team_id) | (Team.id == Game.chaos_team_id))
            .filter(Team.id == team_id)
        )
    ).first()

    losses = (
        await database.scalars(
            select(
                func.sum(case((Game.winning_team_id != Team.id, 1), else_=0))
            )
            .select_from(Team)
            .join(Game, (Team.id == Game.order_team_id) | (Team.id == Game.chaos_team_id))
            .filter(Team.id == team_id)
        )
    ).first()

    team.wins = wins
    team.losses = losses
    team.point_difference = wins - losses

    return team


async def get_standings(database: AsyncSession, division_id: int):

    match_counts = (
        select(
            Team.id.label('team_id'),
            func.count(Match.id).label('matches_played')
        )
        .join(Match, (Team.id == Match.team1_id) | (Team.id == Match.team2_id))
        .filter(Team.division_id == division_id)
        .group_by(Team.id)
        .subquery()
    )
    
    # Calculate the number of game wins and losses based on winning_team_id
    game_results = (
        select(
            Team.id.label('team_id'),
            func.sum(case((Game.winning_team_id == Team.id, 1), else_=0)).label('wins'),
            func.count(Game.id).label('games_played')
        )
        .join(Game, (Team.id == Game.winning_team_id) | (Team.id == Game.match_id))
        .filter(Team.division_id == division_id)
        .group_by(Team.id)
        .subquery()
    )

    # Combine the match and game stats, handling potential NULLs
    standings_query = (
        select(
            Team,
            func.coalesce(match_counts.c.matches_played, 0).label('matches_played'),
            func.coalesce(game_results.c.wins, 0).label('total_wins'),
            func.coalesce(game_results.c.games_played, 0).label('total_losses'),
            (func.coalesce(game_results.c.wins, 0) - (func.coalesce(game_results.c.games_played, 0) - func.coalesce(game_results.c.wins, 0))).label('win_loss_diff')
        )
        .outerjoin(match_counts, Team.id == match_counts.c.team_id)
        .outerjoin(game_results, Team.id == game_results.c.team_id)
        .filter(Team.division_id == division_id)
        .order_by((func.coalesce(game_results.c.wins, 0) - (func.coalesce(game_results.c.games_played, 0) - func.coalesce(game_results.c.wins, 0))).desc())
    )

    # Execute the query asynchronously
    result = await database.execute(standings_query)
    return result.all()


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
