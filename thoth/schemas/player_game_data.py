from pydantic import BaseModel, ConfigDict, computed_field


class _PlayerGameData(BaseModel):

    game_id: int
    player_id: str | None = None
    kills: int
    deaths: int
    assists: int
    gold_earned: int
    damage_dealt: int
    damage_taken: int
    damage_mitigated: int
    structure_damage: int
    player_healing: int
    wards_placed: int
    mvp: bool = False
    god_id: int
    active_1_id: int | None = None
    active_2_id: int | None = None
    item_1_id: int | None = None
    item_2_id: int | None = None
    item_3_id: int | None = None
    item_4_id: int | None = None
    item_5_id: int | None = None
    item_6_id: int | None = None
    team_id: int


class PlayerGameData(_PlayerGameData):

    model_config = ConfigDict(from_attributes=True)

    id: int


class PlayerGameDataCreate(_PlayerGameData):

    pass


class PlayerGameDataDetailed(PlayerGameData):

    gpm: int | None = None

    def calculate_gpm(self, match_duration: int) -> int:
        return int(self.gold_earned / (match_duration / 60))
