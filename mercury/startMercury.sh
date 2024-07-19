#! /usr/bin/bash

cd /data/mercury

export BUILD_TYPE="ops"

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

python3 manage.py runserver 0.0.0.0:80
