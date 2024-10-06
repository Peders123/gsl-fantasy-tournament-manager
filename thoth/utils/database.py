import os

import contextlib
import json

from typing import AsyncIterator

from sqlalchemy.ext.asyncio import AsyncConnection, AsyncSession, AsyncEngine, async_sessionmaker, create_async_engine
from sqlalchemy.ext.declarative import declarative_base

Base = declarative_base()


class DatabaseSessionManager:

    def __init__(self, host: str, engine_kwargs: dict = {}):

        self._engine: AsyncEngine = create_async_engine(host, **engine_kwargs)
        self._sessionmaker: async_sessionmaker = async_sessionmaker(autocommit=False, bind=self._engine)

    async def close(self):

        if self._engine is None:
            raise Exception("DatabaseSessionManager is not initialised.")
        await self._engine.dispose()

        self._engine = None
        self._sessionmaker = None

    @contextlib.asynccontextmanager
    async def connect(self) -> AsyncIterator[AsyncConnection]:
        if self._engine is None:
            raise Exception("DatabaseSessionManager is not initialised.")

        async with self._engine.begin() as connection:
            try:
                yield connection
            except Exception:
                await connection.rollback()
                raise

    @contextlib.asynccontextmanager
    async def session(self) -> AsyncIterator[AsyncSession]:
        if self._sessionmaker is None:
            raise Exception("DatabaseSessionManager is not initialised.")

        session: AsyncSession = self._sessionmaker()
        try:
            yield session
        except Exception:
            await session.rollback()
            raise
        finally:
            await session.close()


def load_postgres_data() -> str:

    with open('/data/thoth/secrets.json', 'r', encoding='utf8') as file:
        secrets = json.load(file)

    username: str = "Cadueceus"
    host: str = "djehuty.postgres.database.azure.com"
    service_name: str = "stats"

    password: str = secrets['passwords']['djehuty'][username]

    return f"{username}:{password}@{host}/{service_name}"


def construct_host_url(db_tech: str) -> str:

    if db_tech == "postgres":
        dialect: str = "postgresql"
        driver: str = "asyncpg"
        connection_information: str = load_postgres_data()

    return f"{dialect}+{driver}://{connection_information}"
