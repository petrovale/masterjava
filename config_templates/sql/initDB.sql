DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS cities;
DROP SEQUENCE IF EXISTS user_seq;
DROP SEQUENCE IF EXISTS project_seq;
DROP SEQUENCE IF EXISTS city_seq;
DROP TYPE IF EXISTS user_flag;
DROP TYPE IF EXISTS group_type;

CREATE TYPE user_flag AS ENUM ('active', 'deleted', 'superuser');

CREATE TYPE group_type AS ENUM ('registering', 'current', 'finished');

CREATE SEQUENCE user_seq START 100000;
CREATE SEQUENCE project_seq START 100000;
CREATE SEQUENCE city_seq START 100000;

CREATE TABLE users (
  id        INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
  full_name TEXT NOT NULL,
  email     TEXT NOT NULL,
  flag      user_flag NOT NULL,
  city_id   INTEGER NOT NULL
);

CREATE UNIQUE INDEX email_idx ON users (email);

CREATE TABLE projects (
  id          INTEGER PRIMARY KEY DEFAULT nextval('project_seq'),
  name        TEXT NOT NULL,
  description TEXT NOT NULL
);

CREATE UNIQUE INDEX project_name_idx ON projects (name);

CREATE TABLE groups (
  id         INTEGER PRIMARY KEY DEFAULT nextval('project_seq'),
  project_id INTEGER NOT NULL,
  name    TEXT NOT NULL,
  type      group_type NOT NULL,
  CONSTRAINT groups_idx UNIQUE (project_id, name),
  FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE CASCADE
);

CREATE TABLE cities (
  id      INTEGER PRIMARY KEY DEFAULT nextval('city_seq'),
  name    TEXT NOT NULL
);

CREATE UNIQUE INDEX city_name_idx ON cities (name);