#!/bin/sh
set -eu
test "$POSTGRES_DB" = 'kairos_normalized_demo' || { echo 'Destino nao autorizado' >&2; exit 1; }
test "$(psql -v ON_ERROR_STOP=1 -U "$POSTGRES_USER" -d "$POSTGRES_DB" -tAc 'SELECT current_database()')" = 'kairos_normalized_demo'
# Original SQL is mounted read-only from kairos-data, never copied/reimplemented.
psql -v ON_ERROR_STOP=1 -U "$POSTGRES_USER" -d "$POSTGRES_DB" \
  -f /kairos-data/ScriptSQL_Kairos_Criacao_Tabelas.sql \
  -f /kairos-data/ScriptSQL_Kairos_Triggers_Procedures_Functions.sql \
  -f /kairos-data/ScriptSQL_Kairos_bi_views.sql \
  -f /kairos-data/ScriptSQL_Kairos_Demo_KPIs.sql \
  --set=schema_sha="$(sha256sum /kairos-data/ScriptSQL_Kairos_Criacao_Tabelas.sql | cut -d ' ' -f 1)" \
  -f /integration/target.sql
