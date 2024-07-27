#! /usr/bin/bash

cd /data/mercury

mkdir -p /data/mercury/mount/mercury/testing
touch /data/mercury/mount/mercury/migrations/__init__.py

python3 manage.py makemigrations talaria
python3 manage.py migrate talaria

python3 manage.py makemigrations
python3 manage.py migrate

if [[ $BUILD_TYPE = "dev" ]]; then
    python3 manage.py populate_database
    expect -c "
        spawn python3 manage.py createsuperuser --username=Peders --email=admin@example.com
        expect \"Password:\"
        send \"Pa55we1rd\r\"
        expect \"Password (again):\"
        send \"Pa55we1rd\r\"
        expect eof
    "
fi

python3 manage.py runserver 0.0.0.0:8001
