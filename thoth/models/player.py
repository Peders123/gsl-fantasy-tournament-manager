from sqlalchemy import Column, ForeignKey, Integer, String
from sqlalchemy.orm import relationship
from thoth.utils.database import Base


class Player(Base):

    __tablename__ = "player"

    id = Column(String, primary_key=True, nullable=False)
    player_name = Column(String, nullable=False)
    user_id = Column(Integer, ForeignKey("user.id"), nullable=True)

    user = relationship("User", back_populates="players")
    total_player_data = relationship("PlayerGameData", back_populates="player")
