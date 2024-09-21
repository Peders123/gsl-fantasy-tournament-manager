from sqlalchemy import Column, Integer, String, ForeignKey, DateTime
from sqlalchemy.orm import relationship

from utils.db_setup import Base


class User(Base):
    """Model representing a single user, mapping 1-to-1 with a discord account.

    Attributes:
        id (CharField): Primary key. Same as the user's discord id.
        discord_name (CharField): Signed up user's discord name.
    """

    __tablename__ = "user"

    id = Column(String, primary_key=True)
    discord_name = Column(String)

    captains = relationship("Captain", back_populates="user")
    players = relationship("Player", back_populates="user")


class Tournament(Base):
    """Model representing an individual tournament.

    Attributes:
        id (IntegerField): Primary key for a tournament record.
        datetime (DateTimeField): The time the tournament takes place.
        title (CharField): The user-facing name of the tournament.
        description (CharField): Brief description of what the touranment is.
    """

    __tablename__ = "tournament"

    id = Column(Integer, primary_key=True, index=True)
    datetime = Column(DateTime)
    title = Column(String)
    description = Column(String)

    captains = relationship("Captain", back_populates="tournament")
    players = relationship("Player", back_populates="tournament")


class Captain(Base):
    """Model representing a captain for the specific tournament.

    Attributes:
        id (IntegerField): Primary key. Is assigned automatically.
        tournament_id (ForeignKey): Relation to the 'Tournaments' model.
        user_id (ForeignKey): Relation to the 'User' model.
        smite_name (CharField): Sign up user's smite ign.
        team_name (CharField): The name of the captain's team.
        reason (CharField): Justification for why the person should be a captain.
        budget (IntegerField): Budget the captain will begin the tournament with.
    """

    __tablename__ = "captain"

    id = Column(Integer, primary_key=True, index=True)
    tournament_id = Column(Integer, ForeignKey("tournament.id", ondelete="CASCADE"), nullable=False)
    user_id = Column(String, ForeignKey("user.id", ondelete="CASCADE"), nullable=False)
    smite_name = Column(String)
    team_name = Column(String)
    reason = Column(String)
    captain_budget = Column(Integer)

    players = relationship("Player", back_populates="captain")
    tournament = relationship("Tournament", back_populates="captains")
    user = relationship("User", back_populates="captains")


class Player(Base):
    """Model representing a standard player in a tournament.

    Attributes:
        id (AutoField): Primary key. Is assigned automatically.
        tournament_id (ForeignKey): Relation to the 'Tournament' model.
        user_id (ForeignKey): Relation to the 'User' model.
        smite_name (CharField): Sign up user's smite ign.
        role_1 (CharField): Primary role the player wants to be.
        role_2 (CharField): Secondary role the player wants to be.
        smite_guru (CharField): Link to the player's smite guru profile. Optional.
        estimated_value (IntegerField): How much we estimate the player is worth.
    """

    __tablename__ = "player"

    id = Column(Integer, primary_key=True, index=True)
    tournament_id = Column(Integer, ForeignKey("tournament.id", ondelete="CASCADE"), nullable=False)
    user_id = Column(String, ForeignKey("user.id", ondelete="CASCADE"), nullable=False)
    captain_id = Column(Integer, ForeignKey("captain.id", ondelete="CASCADE"))
    smite_name = Column(String)
    role_1 = Column(String)
    role_2 = Column(String)
    smite_guru = Column(String)
    estimated_value = Column(Integer)

    tournament = relationship("Tournament", back_populates="players")
    user = relationship("User", back_populates="players")
    captain = relationship("Captain", back_populates="players")
