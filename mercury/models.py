from sqlalchemy import Column, Integer, String
from Database import Base

class Users(Base):
    __tablename__ = "users"

    id = Column(String, primary_key=True)
    discord_name = Column(String)