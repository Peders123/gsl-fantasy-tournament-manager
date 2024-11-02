from datetime import datetime

from pydantic import BaseModel, ConfigDict, field_serializer

from thoth.schemas.player_game_data import PlayerGameDataDetailed


class _Game(BaseModel):

    id: int
    match_id: int
    date_time: datetime
    order_team_id: int
    chaos_team_id: int
    winning_team_id: int
    match_duration: int
    ban_1: int
    ban_2: int
    ban_3: int
    ban_4: int
    ban_5: int
    ban_6: int
    ban_7: int
    ban_8: int
    ban_9: int
    ban_10: int


class Game(_Game):

    model_config = ConfigDict(from_attributes=True)


class GameDetailed(Game):
    total_player_data: list[PlayerGameDataDetailed]

    def calculate_gpm_for_all_players(self):
        print("GAME")
        for player_data in self.total_player_data:
            print("HELLO WORLD")
            player_data.gpm = player_data.calculate_gpm(self.match_duration)


class GameCreate(_Game):
    """@field_serializer("date_time", mode="plain")
    def parse_date_time(cls, value):
        return datetime.strptime(value, "%m/%d/%Y %I:%M:%S %p")"""
