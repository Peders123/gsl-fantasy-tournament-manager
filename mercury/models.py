from sqlalchemy import Column, Integer, String
from Database import Base

class Books(Base):
    __tablename__ = "books"

    id = Column(String, primary_key=True, index=True)
    discord_id = Column(String)