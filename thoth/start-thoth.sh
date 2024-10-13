#!/bin/bash

# The cron job command
CRON_JOB="0 0 * * * /usr/bin/python3 /data/thoth/check_database.py"

# Check if the cron job already exists
(crontab -l | grep -F "$CRON_JOB") >/dev/null 2>&1

# If the cron job doesn't exist, add it to the crontab
if [ $? -ne 0 ]; then
  # Add the cron job to the crontab
  (crontab -l 2>/dev/null; echo "$CRON_JOB") | crontab -
  echo "Cron job added successfully!"
else
  echo "Cron job already exists!"
fi