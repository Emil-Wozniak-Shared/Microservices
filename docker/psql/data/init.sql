CREATE USER ifzz WITH PASSWORD 'ifzz' LOGIN;
ALTER USER ifzz WITH SUPERUSER CREATEDB CREATEROLE INHERIT LOGIN REPLICATION;
CREATE DATABASE ifzz;
CREATE DATABASE microservice;
GRANT ALL PRIVILEGES ON DATABASE ifzz TO ifzz;
GRANT ALL PRIVILEGES ON DATABASE microservice TO ifzz;

\c microservice

CREATE TABLE IF NOT EXISTS users
(
    id         UUID NOT NULL,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    password   VARCHAR(255),
    email      VARCHAR(255),
    karma      SMALLINT,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    version    BIGINT,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS posts
(
    id         UUID NOT NULL,
    title      VARCHAR(255),
    content    VARCHAR(255),
    metadata   VARCHAR(255),
    status     INTEGER,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    version    BIGINT,
    CONSTRAINT pk_post PRIMARY KEY (id)
);

CREATE TABLE comments
(
    id         UUID NOT NULL,
    content    VARCHAR(255),
    post_id    UUID,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    version    BIGINT,
    CONSTRAINT pk_comment PRIMARY KEY (id)
);

BEGIN;
DO
$do$
    BEGIN
        IF EXISTS(SELECT 1 FROM pg_extension WHERE extname liKE 'uuid-ossp') THEN
            RAISE NOTICE 'uuid-ossp already exists';
        ELSE
            CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
        END IF;

        CREATE OR REPLACE FUNCTION public.CREATE_IF_NOT_EXIST(
            name varchar,
            columns varchar,
            isTimestamp boolean = false
        )
            RETURNS void
            language plpgsql
        AS
        $$
        BEGIN
            IF EXISTS(select 1 from pg_tables where tablename = name) THEN
                RAISE NOTICE 'ALREADY EXISTS: %', name;
            ELSE
                RAISE NOTICE 'Create table: %', name;
                IF isTimestamp THEN
                    EXECUTE format(
                                        'CREATE TABLE IF NOT EXISTS ' || name ||
                                        ' (
                                        id UUID DEFAULT uuid_generate_v1(), ' ||
                                        columns || ',
                                        created_at TIMESTAMP NOT NULL,
                                        updated_at TIMESTAMP,
                                        version INTEGER, PRIMARY KEY (id)
                                        );');
                ELSE
                    EXECUTE format(
                                        'CREATE TABLE IF NOT EXISTS ' || name ||
                                        ' (
                                        id UUID DEFAULT uuid_generate_v1(), ' ||
                                        columns || ',
                                        version INTEGER, PRIMARY KEY (id)
                                        );');
                END IF;
            END IF;
        END ;
        $$;

        PERFORM CREATE_IF_NOT_EXIST(
                name := 'users',
                columns := '
                               first_name VARCHAR NOT NULL,
                               last_name VARCHAR NOT NULL,
                               password VARCHAR(500) NOT NULL,
                               email VARCHAR NOT NULL,
                               karma SMALLINT NOT NULL',
                isTimestamp := true
            );

        PERFORM CREATE_IF_NOT_EXIST(
                name := 'posts',
                columns := '
                               title VARCHAR(255),
                               content VARCHAR(255), metadata JSON default ''{}'',
                               status VARCHAR(255) ',
                isTimestamp := true
            );

        PERFORM CREATE_IF_NOT_EXIST(
                name := 'comments',
                columns := '
                               content VARCHAR(255),
                               post_id UUID REFERENCES posts ON DELETE CASCADE',
                isTimestamp := true
            );

    END
$do$;
COMMIT
