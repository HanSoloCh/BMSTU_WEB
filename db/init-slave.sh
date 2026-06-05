#!/bin/sh
set -e

PGDATA=/var/lib/postgresql/data
MASTER_HOST=db_master
REPL_USER=replicator
REPL_PASS=replica123

# Если кластер уже есть — выходим (реплика уже настроена)
if [ -f "$PGDATA/PG_VERSION" ]; then
    echo "Slave already initialized"
    exit 0
fi

echo "Starting base backup from $MASTER_HOST ..."

# Очищаем каталог
rm -rf $PGDATA/*
chmod 700 $PGDATA

# Делаем basebackup
export PGPASSWORD="$REPL_PASS"
pg_basebackup \
    -h "$MASTER_HOST" \
    -D "$PGDATA" \
    -U "$REPL_USER" \
    -v \
    -P \
    --wal-method=stream

touch "$PGDATA/standby.signal"

cat <<EOF >> "$PGDATA/postgresql.auto.conf"
primary_conninfo = 'host=$MASTER_HOST port=5432 user=$REPL_USER password=$REPL_PASS application_name=library_slave'
hot_standby = on
EOF

echo "Slave initialization completed"