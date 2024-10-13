from thoth.utils.database import DatabaseSessionManager, construct_host_url


SESSIONMANAGER = DatabaseSessionManager(construct_host_url("postgres"))


async def get_db_session():

    async with SESSIONMANAGER.session() as session:
        yield session
