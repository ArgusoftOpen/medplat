# NLP Query Builder (Rule-based)

This is a small, self-contained Flask service that demonstrates a rule-based NLP-to-SQL query builder.

Features
- Rule-based parsing of simple natural language queries
- Mapping of user keywords to underlying schema (`schema.json`)
- Preview endpoint (`/preview`) that returns a parameterized SQL and params
- Safe execution endpoint (`/execute`) — disabled by default, enabled with `ENABLE_EXECUTE=1`
- Demo SQLite DB generation (`init_db.py`)

Quickstart

1. Create a Python virtualenv and install dependencies:

```bash
python -m venv venv
source venv/bin/activate  # or venv\Scripts\activate on Windows
pip install -r requirements.txt
```

2. Initialize demo DB:

```bash
python init_db.py
```

3. Run the service:

```bash
set ENABLE_EXECUTE=0  # or export ENABLE_EXECUTE=0 on linux
env\Scripts\python app.py  # or python app.py
```

4. Example requests:

Preview:

```bash
curl -X POST http://localhost:5003/preview -H "Content-Type: application/json" -d '{"query":"show name and age of patients where age > 30"}'
```

Execute (only if `ENABLE_EXECUTE=1`):

```bash
curl -X POST http://localhost:5003/execute -H "Content-Type: application/json" -d '{"query":"show name where age > 30 patients"}'
```

Design Notes
- Parser is rule-based to keep things deterministic and auditable.
- Schema mapping in `schema.json` controls allowed tables/columns and synonyms.
- All generated SQL uses parameterized queries and validates table/column names against schema to prevent injection.
- Modular structure allows replacing `parser.py` with an ML-based component later.
