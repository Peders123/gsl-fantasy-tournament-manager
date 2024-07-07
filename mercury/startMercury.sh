#! /usr/bin/bash

cd /data/mercury

source environment.sh

python3 manage.py makemigrations
python3 manage.py migrate
python3 manage.py migrate --database=dev

if [ $BUILD_TYPE = "dev" ]; then
    echo "RUNNING DEV"
    python3 manage.py populate_database
fi

python3 manage.py runserver 0.0.0.0:8001