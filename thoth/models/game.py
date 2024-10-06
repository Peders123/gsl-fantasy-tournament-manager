from sqlalchemy import Column, Integer, String, DateTime, ForeignKey
from sqlalchemy.orm import relationship
from thoth.utils.database import Base



class Game(Base):
    __tablename__ = "game"

    id = Column(Integer, primary_key=True, nullable=False)
    match_id = Column(Integer, ForeignKey("match.id"), nullable=False)
    date_time = Column(DateTime, nullable=False)
    order_team_id = Column(Integer, ForeignKey("team.id"), nullable=False)
    chaos_team_id = Column(Integer, ForeignKey("team.id"), nullable=False)
    winning_team_id = Column(Integer, ForeignKey("team.id"), nullable=True)
    match_duration = Column(Integer, nullable=False)
    ban_1 = Column(Integer, nullable=True)
    ban_2 = Column(Integer, nullable=True)
    ban_3 = Column(Integer, nullable=True)
    ban_4 = Column(Integer, nullable=True)
    ban_5 = Column(Integer, nullable=True)
    ban_6 = Column(Integer, nullable=True)
    ban_7 = Column(Integer, nullable=True)
    ban_8 = Column(Integer, nullable=True)
    ban_9 = Column(Integer, nullable=True)
    ban_10 = Column(Integer, nullable=True)

    match = relationship("Match", back_populates="games")
    order_team = relationship("Team", foreign_keys=[order_team_id], back_populates="games_as_order")
    chaos_team = relationship("Team", foreign_keys=[chaos_team_id], back_populates="games_as_chaos")
    winning_team = relationship("Team", foreign_keys=[winning_team_id], back_populates="games_won")
