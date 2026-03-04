import sqlite3
import os

BASE_DIR = os.path.dirname(__file__)
DB_PATH = os.path.join(BASE_DIR, "demo.db")

schema_sql = '''
CREATE TABLE IF NOT EXISTS patients (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT,
  age INTEGER,
  gender TEXT,
  registration_date TEXT
);

CREATE TABLE IF NOT EXISTS visits (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  patient_id INTEGER,
  visit_date TEXT,
  diagnosis TEXT,
  facility_id INTEGER
);
'''

sample_inserts = [
    ("Alice Smith", 34, "F", "2022-01-10"),
    ("Bob Kumar", 45, "M", "2021-11-03"),
    ("Carol Jones", 28, "F", "2023-03-22")
]

if __name__ == '__main__':
    if os.path.exists(DB_PATH):
        print("demo.db already exists at:", DB_PATH)
    else:
        conn = sqlite3.connect(DB_PATH)
        cur = conn.cursor()
        cur.executescript(schema_sql)
        cur.executemany("INSERT INTO patients (name, age, gender, registration_date) VALUES (?, ?, ?, ?)", sample_inserts)
        conn.commit()
        conn.close()
        print("Created demo.db at:", DB_PATH)
