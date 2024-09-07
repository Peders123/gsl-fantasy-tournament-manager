from functools import lru_cache

from settings import Settings
from utils.db_setup import DatabaseSessionManager, construct_host_url


@lru_cache
def get_settings():

    return Settings()


async def get_db_session():

    settings = get_settings()

    sessionmanager = DatabaseSessionManager(construct_host_url(settings.database_tech))
    async with sessionmanager.session() as session:
        yield session
