from sqlalchemy import Column, Integer, String
from sqlalchemy.orm import relationship

from thoth.utils.database import Base


class Division(Base):

    __tablename__ = "division"

    id = Column(Integer, primary_key=True, nullable=False)
    division_name = Column(String, nullable=False)
    division_rank = Column(Integer, nullable=False)

    teams = relationship("Team", back_populates="division")
