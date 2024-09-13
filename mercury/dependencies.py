from functools import lru_cache

from settings import Settings
from utils.db_setup import DatabaseSessionManager, construct_host_url


@lru_cache
def get_settings():

    return Settings()


SESSIONMANAGER = DatabaseSessionManager(construct_host_url(get_settings().database_tech))


async def get_db_session():

    async with SESSIONMANAGER.session() as session:
        yield session
