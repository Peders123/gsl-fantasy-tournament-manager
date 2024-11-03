from sqlalchemy import Column, Integer, String
from sqlalchemy.orm import relationship

from thoth.utils.database import Base


class User(Base):

    __tablename__ = "user"

    id = Column(Integer, primary_key=True, nullable=False)
    discord_name = Column(String, nullable=False)

    players = relationship("Player", back_populates="user")
    user_teams = relationship("UserTeam", back_populates="user")
