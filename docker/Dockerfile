FROM maven:3.9.8-eclipse-temurin-22 AS java-builder

WORKDIR /data

COPY janus /data/janus

WORKDIR /data/janus

RUN mvn clean package


FROM eclipse-temurin:22-jre AS janus

WORKDIR /data/janus

RUN apt-get update
RUN apt-get install -y curl

RUN mkdir -p mount/janus

COPY --from=java-builder /data/janus/target/janus-1.0-SNAPSHOT.jar .
COPY --from=java-builder /data/janus/config.json .
COPY janus/src /data/janus/src
COPY secrets.json /data/janus/secrets.json
COPY janus/keep_alive.sh /data/janus/keep_alive.sh

RUN chmod +x /data/janus/keep_alive.sh

ENTRYPOINT ["sh", "-c", "/data/janus/keep_alive.sh & java -cp janus-1.0-SNAPSHOT.jar com.tanukismite.fantasy.bot.App"]


FROM python:3.12 AS python-builder

WORKDIR /

RUN apt-get update
RUN apt-get install -y --no-install-recommends build-essential gcc

RUN python -m venv /opt/venv

ENV PATH="/opt/venv/bin:$PATH"

COPY requirements.txt .
RUN pip install -r requirements.txt


FROM python:3.12-slim-bookworm AS terra

RUN apt-get update && apt-get install -y dos2unix

RUN mkdir -p /data/terra

COPY --from=python-builder /opt/venv /opt/venv

ENV PATH="/opt/venv/bin:$PATH"

COPY config/assets/smite /data/assets/smite
COPY terra/startTerra.sh /data/terra/.
COPY terra/manage.py /data/terra/.
COPY terra/static /data/terra/static
COPY terra/home /data/terra/home
COPY terra/terra /data/terra/terra
COPY terra/match /data/terra/match
COPY terra/user /data/terra/user
COPY terra/tournament /data/terra/tournament
COPY terra/auction /data/terra/auction
COPY secrets.json /data/terra/secrets.json

EXPOSE 8000

RUN dos2unix /data/terra/startTerra.sh

WORKDIR /data/terra
RUN chmod +x startTerra.sh
ENTRYPOINT ["./startTerra.sh"]


FROM python:3.12-slim-bookworm AS mercury


RUN apt-get update
RUN apt-get install -y expect

COPY --from=python-builder /opt/venv /opt/venv

ENV PATH="/opt/venv/bin:$PATH"

COPY mercury /data/mercury
COPY mercury/startMercury.sh /data/mercury
COPY secrets.json /data/mercury/secrets.json

EXPOSE 80 2222

RUN apt-get update && apt-get install -y dos2unix && dos2unix /data/mercury/startMercury.sh

WORKDIR /data/mercury
RUN chmod +x startMercury.sh
ENTRYPOINT ["./startMercury.sh"]
