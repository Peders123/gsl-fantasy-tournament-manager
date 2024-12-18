#!/bin/bash

cd /data/terra

python3 manage.py makemigrations auction
python3 manage.py migrate auction

python3 manage.py makemigrations home
python3 manage.py migrate home

python3 manage.py collectstatic --noinput

export DJANGO_SETTINGS_MODULE=terra.settings
daphne -b 0.0.0.0 -p 8000 terra.asgi:application
