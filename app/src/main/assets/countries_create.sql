CREATE TABLE IF NOT EXISTS country (
  _id integer primary key autoincrement,
  idx integer,
  iso text NOT NULL,
  name text NOT NULL,
  nicename text NOT NULL,
  iso3 text DEFAULT NULL,
  numcode integer DEFAULT NULL,
  phonecode integer NOT NULL
);