#! /usr/bin/bash

cd /data/mercury




python3 -m uvicorn crud.api:app --host 0.0.0.0 --port 80
