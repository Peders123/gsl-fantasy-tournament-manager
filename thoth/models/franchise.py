from sqlalchemy import Column, Integer, String
from sqlalchemy.orm import relationship

from thoth.utils.database import Base


class Franchise(Base):

    __tablename__ = "franchise"

    id = Column(Integer, primary_key=True, nullable=False)
    franchise_name = Column(String, nullable=False)
    franchise_owner = Column(String, nullable=False)
    data_name = Column(String, nullable=False)

    teams = relationship("Team", back_populates="franchise")
