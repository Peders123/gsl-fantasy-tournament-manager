from sqlalchemy import Column, DateTime, Integer

from thoth.utils.database import Base


class ScheduledTask(Base):

    __tablename__ = "scheduled_task"

    id = Column(Integer, primary_key=True, nullable=False)
    scheduled_date = Column(DateTime, nullable=False)
    order_team_id = Column(Integer, nullable=False)
    chaos_team_id = Column(Integer, nullable=False)
    match_id = Column(Integer, nullable=False)
