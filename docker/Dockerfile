FROM maven:3.9.8-eclipse-temurin-22 AS java-builder

WORKDIR /data

COPY fantasy-tournament-bot /data/janus/fantasy-tournament-bot
COPY config.json /data/janus/config.json

WORKDIR /data/janus/fantasy-tournament-bot

RUN mvn clean package


FROM python:3.12 AS python-builder

WORKDIR /

RUN apt-get update
RUN apt-get install -y --no-install-recommends build-essential gcc

RUN python -m venv /opt/venv

ENV PATH="/opt/venv/bin:$PATH"

COPY gsl_fantasy_tournament/python-requirements.txt .
RUN pip install -r python-requirements.txt


FROM eclipse-temurin:22-jre as janus

WORKDIR /data/janus/fantasy-tournament-bot

COPY --from=java-builder /data/janus/fantasy-tournament-bot/target/fantasy-tournament-bot-1.0-SNAPSHOT.jar .
COPY --from=java-builder /data/janus/config.json ../
COPY fantasy-tournament-bot/src /data/janus/fantasy-tournament-bot/src

ENTRYPOINT ["java", "-cp", "fantasy-tournament-bot-1.0-SNAPSHOT.jar", "com.glaeriasmite.fantasy.bot.App"]


FROM python:3.12-slim-bookworm as terra

RUN mkdir -p /data/terra

COPY --from=python-builder /opt/venv /opt/venv

ENV PATH="/opt/venv/bin:$PATH"

COPY gsl_fantasy_tournament/startTerra.sh /data/terra/.
COPY gsl_fantasy_tournament/gsl_fantasy_tournament /data/terra/gsl_fantasy_tournament
COPY gsl_fantasy_tournament/home /data/terra/home
COPY gsl_fantasy_tournament/tournament /data/terra/tournament
COPY gsl_fantasy_tournament/db.sqlite3 /data/terra/.
COPY gsl_fantasy_tournament/manage.py /data/terra/.
