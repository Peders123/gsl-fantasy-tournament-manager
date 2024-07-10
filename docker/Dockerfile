
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

COPY gsl_fantasy_tournament/startTerra.sh /data/terra/.
COPY gsl_fantasy_tournament/gsl_fantasy_tournament /data/terra/gsl_fantasy_tournament
COPY gsl_fantasy_tournament/home /data/terra/home
COPY gsl_fantasy_tournament/tournament /data/terra/tournament
COPY gsl_fantasy_tournament/db.sqlite3 /data/terra/.
COPY gsl_fantasy_tournament/manage.py /data/terra/.

COPY docker/environment.sh /data/terra/.

