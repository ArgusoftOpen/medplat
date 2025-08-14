CREATE TABLE IF NOT EXISTS indicator_master (
  id SERIAL PRIMARY KEY,
  indicator_name TEXT NOT NULL,
  description TEXT,
  sql_query TEXT NOT NULL,
  query_result INTEGER,
  created_by INTEGER,
  created_on TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS derived_attributes (
  id SERIAL PRIMARY KEY,
  derived_name TEXT NOT NULL,
  formula TEXT NOT NULL,
  result DOUBLE PRECISION,
  created_on TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS dataset_master (
  id SERIAL PRIMARY KEY,
  dataset_name TEXT NOT NULL,
  sql_query TEXT NOT NULL,
  created_on TIMESTAMP DEFAULT NOW(),
  created_by INTEGER
);