-- Создаём пользователя только для чтения
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'readonlyuser') THEN
        CREATE ROLE readonlyuser WITH LOGIN PASSWORD 'readonly123';
    END IF;
END
  $$;

GRANT CONNECT ON DATABASE librarydb TO readonlyuser;
GRANT USAGE ON SCHEMA public TO readonlyuser;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO readonlyuser;
GRANT SELECT ON ALL SEQUENCES IN SCHEMA public TO readonlyuser;

ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT ON TABLES TO readonlyuser;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT ON SEQUENCES TO readonlyuser;

-- Если нужен пользователь для репликации (для slave-базы)
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'replicator') THEN
        CREATE ROLE replicator WITH REPLICATION LOGIN PASSWORD 'replicator123';
    END IF;
END
  $$;