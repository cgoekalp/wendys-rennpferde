-- create table horse if not exists
CREATE TABLE IF NOT EXISTS simulation (
  -- use auto incrementing IDs as primary key
  id        BIGINT AUTO_INCREMENT PRIMARY KEY,
  name      VARCHAR(255) NOT NULL UNIQUE,
  created   DATETIME     NOT NULL,
  finished boolean default false
);
CREATE TABLE IF NOT EXISTS horse (
  -- use auto incrementing IDs as primary key
  id        BIGINT AUTO_INCREMENT PRIMARY KEY,
  name      VARCHAR(255) NOT NULL,
  breed     TEXT,
  min_speed DOUBLE       NOT NULL,
  max_speed DOUBLE       NOT NULL,
  created   DATETIME     NOT NULL,
  updated   DATETIME     NOT NULL,
  deleted boolean default false
);
CREATE TABLE IF NOT EXISTS jockey (
  -- use auto incrementing IDs as primary key
  id        BIGINT AUTO_INCREMENT PRIMARY KEY,
  name      VARCHAR(255) NOT NULL,
  skill DOUBLE       NOT NULL,
  created   DATETIME     NOT NULL,
  updated   DATETIME     NOT NULL,
  deleted boolean default false
);
CREATE TABLE IF NOT EXISTS simulation_participant (
  -- use auto incrementing IDs as primary key
  id        BIGINT AUTO_INCREMENT PRIMARY KEY,
  simulation_id BIGINT NOT NULL,
  jockey_id BIGINT NOT NULL,
  horse_id BIGINT NOT NULL,
  luck_factor DOUBLE       NOT NULL,
  FOREIGN KEY (simulation_id) REFERENCES simulation(id),
  FOREIGN KEY (jockey_id) REFERENCES jockey(id),
  FOREIGN KEY (horse_id) REFERENCES horse(id),
  UNIQUE ( simulation_id, jockey_id, horse_id )
);
CREATE TABLE IF NOT EXISTS simulation_combination_result (
      id        BIGINT AUTO_INCREMENT PRIMARY KEY,
      horse_name      VARCHAR(255) NOT NULL,
      jockey_name VARCHAR(255) NOT NULL,
      luck_factor DOUBLE       NOT NULL,
      avg_speed DOUBLE       NOT NULL,
      horse_speed DOUBLE       NOT NULL,
      skill  DOUBLE       NOT NULL,
      rank INTEGER NOT NULL,
      simulation_id BIGINT NOT NULL,
      FOREIGN KEY (simulation_id) REFERENCES simulation(id)
);