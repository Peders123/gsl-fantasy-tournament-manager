FROM python:3.12 AS python-builder

WORKDIR /

RUN apt-get update
RUN apt-get install -y --no-install-recommends build-essential gcc

RUN python -m venv /opt/venv

ENV PATH="/opt/venv/bin:$PATH"

COPY docker/thoth/python-requirements.txt .
RUN pip install -r python-requirements.txt


FROM python:3.12-slim-bookworm AS thoth

RUN apt-get update && apt-get install -y cron

RUN mkdir /data
RUN touch /data/__init__.py

ENV PYTHONPATH=/data

COPY --from=python-builder /opt/venv /opt/venv

ENV PATH="/opt/venv/bin:$PATH"

COPY bin/utils/hirez_api.py /data/bin/utils/hirez_api.py
COPY thoth /data/thoth
COPY secrets.json /data/thoth/secrets.json

ENTRYPOINT ["uvicorn", "thoth.main:app", "--host", "0.0.0.0", "--port", "8002"]