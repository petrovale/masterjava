DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS user_group;
DROP TABLE IF EXISTS project;
DROP TABLE IF EXISTS city;
DROP SEQUENCE IF EXISTS user_seq;
DROP SEQUENCE IF EXISTS common_seq;
DROP TYPE IF EXISTS user_flag;
DROP TYPE IF EXISTS group_type;

CREATE TYPE user_flag AS ENUM ('active', 'deleted', 'superuser');

CREATE TYPE group_type AS ENUM ('REGISTERING', 'CURRENT', 'FINISHED');

CREATE SEQUENCE user_seq START 100000;
CREATE SEQUENCE common_seq START 100000;

CREATE TABLE city (
  id      INTEGER PRIMARY KEY DEFAULT nextval('common_seq'),
  ref     TEXT UNIQUE,
  name    TEXT NOT NULL
);

CREATE TABLE users (
  id        INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
  full_name TEXT NOT NULL,
  email     TEXT NOT NULL,
  flag      user_flag NOT NULL,
  city_id   INTEGER REFERENCES city (id)
);

CREATE UNIQUE INDEX email_idx ON users (email);

CREATE TABLE project (
  id          INTEGER PRIMARY KEY DEFAULT nextval('common_seq'),
  name        TEXT NOT NULL UNIQUE,
  description TEXT
);

CREATE TABLE groups (
  id         INTEGER PRIMARY KEY DEFAULT nextval('common_seq'),
  name       TEXT NOT NULL UNIQUE,
  type       group_type NOT NULL,
  project_id INTEGER NOT NULL REFERENCES project (id)
);

CREATE TABLE user_group (
  user_id  INTEGER NOT NULL REFERENCES users (id) ON DELETE CASCADE,
  group_id INTEGER NOT NULL REFERENCES groups (id),
  CONSTRAINT users_group_idx UNIQUE (user_id, group_id)
);