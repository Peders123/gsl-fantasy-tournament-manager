#! /usr/bin/bash

cd /data/terra

python3 manage.py makemigrations migrate
python3 manage.py runserver