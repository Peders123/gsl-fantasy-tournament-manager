from sqlalchemy import Column, Integer, String, ForeignKey
from database import Base


class Users(Base):
    __tablename__ = "User"

    """Model representing a single user, mapping 1-to-1 with a discord account.

    Attributes:
        user_id (CharField): Primary key. Same as the user's discord id.
        discord_name (CharField): Signed up user's discord name.
    """

    id = Column(String, primary_key=True)
    discord_name = Column(String)


class Tournaments(Base):
    __tablename__ = "Tournament"

    """Model representing an individual tournament.

    Attributes:
        tournament_id (IntegerField): Primary key for a tournament record.
        datetime (DateTimeField): The time the tournament takes place.
        title (CharField): The user-facing name of the tournament.
        description (CharField): Brief description of what the touranment is.
    """
    id = Column(Integer, primary_key=True, index=True)
    # datetime = models.DateTimeField(default=datetime(year=2001, month=11, day=5, hour=0, minute=0, second=0))
    title = Column(String)
    description = Column(String)


class Captains(Base):
    __tablename__ = "Captain"
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
    id = Column(Integer, primary_key=True, index=True)
    tournament_id = Column(Integer, ForeignKey("Tournament.id", ondelete="CASCADE"), nullable=False)
    user_id = Column(String, ForeignKey("User.id", ondelete="CASCADE"), nullable=False)
    smite_name = Column(String)
    team_name = Column(String)
    reason = Column(String)
    captain_budget = Column(Integer)
    

class Players(Base):
    __tablename__ = "Player"

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
    id = Column(Integer, primary_key=True, index=True)
    tournament_id = Column(Integer, ForeignKey("Tournament.id", ondelete="CASCADE"), nullable=False)
    user_id = Column(String, ForeignKey("User.id", ondelete="CASCADE"), nullable=False)
    captain_id = Column(Integer, ForeignKey("Captain.id", ondelete="CASCADE"))
    smite_name = Column(String)
    role_1 = Column(String)
    role_2 = Column(String)
    smite_guru = Column(String)
    estimated_value = Column(Integer)
