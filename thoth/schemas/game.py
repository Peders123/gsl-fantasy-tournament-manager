from datetime import datetime

from pydantic import BaseModel, ConfigDict

from thoth.schemas.player_game_data import PlayerGameDataDetailed


class _Game(BaseModel):

    id: int
    match_id: int
    date_time: datetime | None = None
    order_team_id: int | None = None
    chaos_team_id: int | None = None
    winning_team_id: int
    match_duration: int | None = None
    ban_1: int | None = None
    ban_2: int | None = None
    ban_3: int | None = None
    ban_4: int | None = None
    ban_5: int | None = None
    ban_6: int | None = None
    ban_7: int | None = None
    ban_8: int | None = None
    ban_9: int | None = None
    ban_10: int | None = None


class Game(_Game):

    model_config = ConfigDict(from_attributes=True)


class GameDetailed(Game):
    total_player_data: list[PlayerGameDataDetailed]

    def calculate_gpm_for_all_players(self):
        for player_data in self.total_player_data:
            player_data.gpm = player_data.calculate_gpm(self.match_duration)


class GameCreate(_Game):
    """@field_serializer("date_time", mode="plain")
    def parse_date_time(cls, value):
        return datetime.strptime(value, "%m/%d/%Y %I:%M:%S %p")"""
