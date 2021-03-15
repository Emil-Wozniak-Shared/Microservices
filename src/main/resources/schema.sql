CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS posts;

CREATE TABLE IF NOT EXISTS users
(
    id         UUID DEFAULT uuid_generate_v4(),
    first_name VARCHAR   NOT NULL,
    last_name  VARCHAR   NOT NULL,
    email      VARCHAR   NOT NULL,
    karma      SMALLINT  NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    version    INTEGER,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS posts
(
    -- id SERIAL PRIMARY KEY,
    id         UUID         DEFAULT uuid_generate_v4(),
    title      VARCHAR(255),
    content    VARCHAR(255),
    metadata   JSON         default '{}',
    -- In this sample, use Varchar to store enum(name), Spring Data R2dbc can convert Java Enum to pg VARCHAR, and reverse.
    status     VARCHAR(255) default 'DRAFT',
    created_at TIMESTAMP, --NOT NULL DEFAULT LOCALTIMESTAMP,
    updated_at TIMESTAMP,
    version    INTEGER,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id         UUID DEFAULT uuid_generate_v4(),
    content    VARCHAR(255),
    post_id    UUID REFERENCES posts ON DELETE CASCADE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    version    INTEGER,
    PRIMARY KEY (id)
);