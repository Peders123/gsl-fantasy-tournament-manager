#! /usr/bin/bash

cd /data/mercury


echo "Migrating general"
echo "migrations are yet to be added"

echo "Finished Migrations"


echo "Running Server"

python3 -m uvicorn api:app --host 0.0.0.0 --port 80
