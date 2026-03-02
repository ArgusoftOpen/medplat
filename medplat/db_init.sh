#!/bin/bash
set -e
export PGPASSWORD=$POSTGRES_PASSWORD;
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
  \connect $POSTGRES_DB $POSTGRES_USER
  BEGIN;
    CREATE SCHEMA IF NOT EXISTS archive;
    ALTER SCHEMA archive OWNER TO $POSTGRES_USER;

    CREATE SCHEMA IF NOT EXISTS analytics;
    ALTER SCHEMA analytics OWNER TO $POSTGRES_USER;

    ALTER DATABASE $POSTGRES_DB SET search_path TO public,analytics,archive ;
  COMMIT;
EOSQL

cd /usr/lib/postgresql/15/bin
pg_ctl restart
