#!/bin/bash

while true; do
  curl -s https://janustanukismiteleague.scm.azurewebsites.net/ > /dev/null
  echo "pinged"
  sleep 120
done