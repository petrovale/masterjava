--liquibase formatted sql

--changeset gkislin:1
CREATE SEQUENCE common_seq START 100000;

CREATE TABLE city (
  id   INTEGER PRIMARY KEY DEFAULT nextval('common_seq'),
  ref  TEXT UNIQUE,
  name TEXT NOT NULL
);

ALTER TABLE users
  ADD COLUMN city_id INTEGER REFERENCES city (id);

--changeset gkislin:2
CREATE TABLE project (
  id          INTEGER PRIMARY KEY DEFAULT nextval('common_seq'),
  name        TEXT NOT NULL UNIQUE,
  description TEXT
);

CREATE TYPE GROUP_TYPE AS ENUM ('REGISTERING', 'CURRENT', 'FINISHED');

CREATE TABLE groups (
  id         INTEGER PRIMARY KEY DEFAULT nextval('common_seq'),
  name       TEXT       NOT NULL UNIQUE,
  type       GROUP_TYPE NOT NULL,
  project_id INTEGER    NOT NULL REFERENCES project (id)
);

CREATE TABLE user_group (
  user_id  INTEGER NOT NULL REFERENCES users (id) ON DELETE CASCADE,
  group_id INTEGER NOT NULL REFERENCES groups (id),
  CONSTRAINT users_group_idx UNIQUE (user_id, group_id)
);
