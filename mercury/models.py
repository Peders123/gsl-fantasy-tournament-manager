from sqlalchemy import Column, Integer, String, ForeignKey, DateTime
from sqlalchemy.orm import relationship

from utils.db_setup import Base


class User(Base):
    __tablename__ = "talaria_user"

    """Model representing a single user, mapping 1-to-1 with a discord account.

    Attributes:
        user_id (CharField): Primary key. Same as the user's discord id.
        discord_name (CharField): Signed up user's discord name.
    """

    user_id = Column(String, primary_key=True)
    discord_name = Column(String)

    captains = relationship("Captain", back_populates="user")
    players = relationship("Player", back_populates="user")


class Tournament(Base):
    __tablename__ = "talaria_tournament"

    """Model representing an individual tournament.

    Attributes:
        tournament_id (IntegerField): Primary key for a tournament record.
        datetime (DateTimeField): The time the tournament takes place.
        title (CharField): The user-facing name of the tournament.
        description (CharField): Brief description of what the touranment is.
    """
    tournament_id = Column(Integer, primary_key=True, index=True)
    datetime = Column(DateTime)
    title = Column(String)
    description = Column(String)

    captains = relationship("Captain", back_populates="tournament")
    players = relationship("Player", back_populates="tournament")


class Captain(Base):
    __tablename__ = "talaria_captain"
    """Model representing a captain for the specific tournament.

    Attributes:
        captain_id (IntegerField): Primary key. Is assigned automatically.
        tournament_id (ForeignKey): Relation to the 'Tournaments' model.
        user_id (ForeignKey): Relation to the 'User' model.
        smite_name (CharField): Sign up user's smite ign.
        team_name (CharField): The name of the captain's team.
        reason (CharField): Justification for why the person should be a captain.
        captain_budget (IntegerField): Budget the captain will begin the tournament with.
    """
    captain_id = Column(Integer, primary_key=True, index=True)
    tournament_id_id = Column(Integer, ForeignKey("talaria_tournament.tournament_id", ondelete="CASCADE"),
                              nullable=False)
    user_id_id = Column(String, ForeignKey("talaria_user.user_id", ondelete="CASCADE"), nullable=False)
    smite_name = Column(String)
    team_name = Column(String)
    reason = Column(String)
    captain_budget = Column(Integer)

    players = relationship("Player", back_populates="captain")
    tournament = relationship("Tournament", back_populates="captains")
    user = relationship("User", back_populates="captains")


class Player(Base):
    __tablename__ = "talaria_player"

    """Model representing a standard player in a tournament.

    Attributes:
        player_id (AutoField): Primary key. Is assigned automatically.
        tournament_id (ForeignKey): Relation to the 'Tournament' model.
        user_id (ForeignKey): Relation to the 'User' model.
        captain_id (ForeignKey): Relation to the 'Captain' model. Not assiged at creation, added later.
        smite_name (CharField): Sign up user's smite ign.
        role_1 (CharField): Primary role the player wants to be.
        role_2 (CharField): Secondary role the player wants to be.
        smite_guru (CharField): Link to the player's smite guru profile. Optional.
        estimated_value (IntegerField): How much we estimate the player is worth.
    """
    player_id = Column(Integer, primary_key=True, index=True)
    tournament_id_id = Column(Integer, ForeignKey("talaria_tournament.tournament_id", ondelete="CASCADE"),
                              nullable=False)
    user_id_id = Column(String, ForeignKey("talaria_user.user_id", ondelete="CASCADE"), nullable=False)
    captain_id_id = Column(Integer, ForeignKey("talaria_captain.captain_id", ondelete="CASCADE"))
    smite_name = Column(String)
    role_1 = Column(String)
    role_2 = Column(String)
    smite_guru = Column(String)
    estimated_value = Column(Integer)

    tournament = relationship("Tournament", back_populates="players")
    user = relationship("User", back_populates="players")
    captain = relationship("Captain", back_populates="players")
