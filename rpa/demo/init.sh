#!/bin/sh
set -eu
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" \
  --set=demo_email="$RPA_EMAIL" --set=demo_password="$RPA_PASSWORD" -f /demo/schema.sql
