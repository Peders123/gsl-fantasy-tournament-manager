FROM maven:3.9.8-eclipse-temurin-22 AS java-builder

WORKDIR /data

COPY janus /data/janus

WORKDIR /data/janus

RUN mvn clean package


FROM eclipse-temurin:22-jre as janus

WORKDIR /data/janus

COPY --from=java-builder /data/janus/target/janus-1.0-SNAPSHOT.jar .
COPY --from=java-builder /data/janus/config.json .
COPY janus/src /data/janus/src

ENTRYPOINT ["java", "-cp", "janus-1.0-SNAPSHOT.jar", "com.tanukismite.fantasy.bot.App"]


FROM python:3.12 AS python-builder

WORKDIR /

RUN apt-get update
RUN apt-get install -y --no-install-recommends build-essential gcc

RUN python -m venv /opt/venv

ENV PATH="/opt/venv/bin:$PATH"

COPY requirements.txt .
RUN pip install -r requirements.txt


FROM python:3.12-slim-bookworm as terra

RUN mkdir -p /data/terra

COPY --from=python-builder /opt/venv /opt/venv

ENV PATH="/opt/venv/bin:$PATH"

COPY terra/startTerra.sh /data/terra/.
COPY terra/manage.py /data/terra/.
COPY terra/home /data/terra/home
COPY terra/terra /data/terra/terra
COPY terra/tournament /data/terra/tournament

COPY docker/environment.sh /data/terra/.

RUN apt-get update && apt-get install -y dos2unix && dos2unix /data/terra/startTerra.sh


FROM python:3.12-slim-bookworm as mercury

RUN mkdir -p /data/mercury

RUN apt-get update
RUN apt-get install -y expect

COPY --from=python-builder /opt/venv /opt/venv

ENV PATH="/opt/venv/bin:$PATH"

COPY mercury/startMercury.sh /data/mercury
COPY mercury/mercury /data/mercury/mercury
COPY mercury/talaria /data/mercury/talaria
COPY mercury/manage.py /data/mercury/.
COPY mercury/dData.json /data/mercury/.

EXPOSE 80

RUN apt-get update && apt-get install -y dos2unix && dos2unix /data/mercury/startMercury.sh

WORKDIR /data/mercury

CMD ["./startMercury.sh"]
