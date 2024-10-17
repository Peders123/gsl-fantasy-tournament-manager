from sqlalchemy import Column, DateTime, ForeignKey, Integer
from sqlalchemy.orm import relationship
from thoth.utils.database import Base


class Match(Base):

    __tablename__ = "match"

    id = Column(Integer, primary_key=True, nullable=False)
    match_date_time = Column(DateTime, nullable=False)
    best_of = Column(Integer, nullable=False)
    team1_id = Column(Integer, ForeignKey("team.id"), nullable=False)
    team2_id = Column(Integer, ForeignKey("team.id"), nullable=False)

    games = relationship("Game", back_populates="match", order_by="Game.id")
    team1 = relationship("Team", foreign_keys=[team1_id], back_populates="matches_as_team1")
    team2 = relationship("Team", foreign_keys=[team2_id], back_populates="matches_as_team2")
