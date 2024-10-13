from sqlalchemy import Column, Integer, String, ForeignKey
from sqlalchemy.orm import relationship

from thoth.utils.database import Base


class User(Base):

    __tablename__ = "user"

    id = Column(Integer, primary_key=True, nullable=False)
    discord_name = Column(String,  nullable=False)
    team_id = Column(Integer, ForeignKey("team.id"), nullable=False)

    team = relationship("Team", back_populates="users")
    players = relationship("Player", back_populates="user")
