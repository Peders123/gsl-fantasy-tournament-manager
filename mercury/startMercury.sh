#! /usr/bin/bash

cd /data/mercury

echo "Migrating Talaria"
python3 manage.py makemigrations talaria
python3 manage.py migrate talaria

echo "Migrating general"
python3 manage.py makemigrations
python3 manage.py migrate

echo "Finished Migrations"

python3 manage.py collectstatic

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

echo "Running Server"

python3 manage.py runserver 0.0.0.0:80
