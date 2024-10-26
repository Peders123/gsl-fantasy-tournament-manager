from sqlalchemy import Column, Integer, ForeignKey, DateTime
from sqlalchemy.orm import relationship

from thoth.utils.database import Base


class UserTeam(Base):

    __tablename__ = "user_team"

    id = Column(Integer, primary_key=True, nullable=False)
    user_id = Column(Integer, ForeignKey("user.id"), nullable=False)
    team_id = Column(Integer, ForeignKey("team.id"), nullable=True)
    join_order = Column(Integer, nullable=False)
    join_date = Column(DateTime, nullable=True)
    leave_date = Column(DateTime, nullable=True)

    team = relationship("Team", back_populates="user_teams")
    user = relationship("User", back_populates="user_teams")
