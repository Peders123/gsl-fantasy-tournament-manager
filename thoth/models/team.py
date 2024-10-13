from sqlalchemy import Column, ForeignKey, Integer
from sqlalchemy.orm import relationship

from thoth.utils.database import Base


class Team(Base):

    __tablename__ = "team"

    id = Column(Integer, primary_key=True, nullable=False)
    franchise_id = Column(Integer, ForeignKey("franchise.id"), nullable=False)
    division_id = Column(Integer, ForeignKey("division.id"), nullable=False)

    franchise = relationship("Franchise", back_populates="teams")
    division = relationship("Division", back_populates="teams")
    games_as_order = relationship("Game", foreign_keys="[Game.order_team_id]", back_populates="order_team")
    games_as_chaos = relationship("Game", foreign_keys="[Game.chaos_team_id]", back_populates="chaos_team")
    games_won = relationship("Game", foreign_keys="[Game.winning_team_id]", back_populates="winning_team")
    matches_as_team1 = relationship("Match", foreign_keys="[Match.team1_id]", back_populates="team1")
    matches_as_team2 = relationship("Match", foreign_keys="[Match.team2_id]", back_populates="team2")
    users = relationship("User", back_populates="team")
