from sqlalchemy import Boolean, Column, Integer, ForeignKey, String
from sqlalchemy.orm import relationship
from thoth.utils.database import Base



class PlayerGameData(Base):
    __tablename__ = "player_game_data"

    id = Column(Integer, primary_key=True, nullable=False)
    game_id = Column(Integer, ForeignKey("game.id"), nullable=False)
    player_id = Column(String, ForeignKey("player.id"), nullable=True)
    kills = Column(Integer, nullable=False)
    deaths = Column(Integer, nullable=False)
    assists = Column(Integer, nullable=False)
    gold_earned = Column(Integer, nullable=False)
    damage_dealt = Column(Integer, nullable=False)
    damage_taken = Column(Integer, nullable=False)
    damage_mitigated = Column(Integer, nullable=False)
    structure_damage = Column(Integer, nullable=False)
    player_healing = Column(Integer, nullable=False)
    wards_placed = Column(Integer, nullable=False)
    mvp = Column(Boolean, default=False, nullable=False)
    god_id = Column(Integer, nullable=False)
    active_1_id = Column(Integer, nullable=True)
    active_2_id = Column(Integer, nullable=True)
    item_1_id = Column(Integer, nullable=True)
    item_2_id = Column(Integer, nullable=True)
    item_3_id = Column(Integer, nullable=True)
    item_4_id = Column(Integer, nullable=True)
    item_5_id = Column(Integer, nullable=True)
    item_6_id = Column(Integer, nullable=True)

    game = relationship("Game", back_populates="total_player_data")
    player = relationship("Player", back_populates="total_player_data")
