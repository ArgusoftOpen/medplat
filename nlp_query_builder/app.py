import sqlite3
from flask import Flask, request, jsonify
from parser import RuleBasedParser, NLParseError
import os

app = Flask(__name__)
BASE_DIR = os.path.dirname(__file__)
SCHEMA_PATH = os.path.join(BASE_DIR, "schema.json")
DB_PATH = os.path.join(BASE_DIR, "demo.db")
parser = RuleBasedParser(SCHEMA_PATH)

ALLOWED_OPERATORS = {"=", ">", "<", ">=", "<=", "!=", "LIKE"}


def validate_parsed(parsed: dict):
    # ensure table and columns are in schema
    table = parsed["table"]
    if table not in parser.schema["tables"]:
        raise NLParseError("Unsupported table requested")
    for c in parsed["columns"]:
        if c not in parser.schema["tables"][table]["columns"]:
            raise NLParseError(f"Unsupported column requested: {c}")
    # where clause operators already validated in parser, but ensure no suspicious text
    if parsed["where_sql"]:
        for op in [";", "--"]:
            if op in parsed["where_sql"]:
                raise NLParseError("Invalid characters in where clause")


@app.route("/preview", methods=["POST"])
def preview():
    data = request.get_json() or {}
    q = data.get("query", "")
    try:
        parsed = parser.parse(q)
        validate_parsed(parsed)
        cols = ", ".join(parsed["columns"]) if parsed["columns"] else "*"
        sql = f"SELECT {cols} FROM {parsed['table']}"
        if parsed["where_sql"]:
            sql = sql + " WHERE " + parsed["where_sql"]
        return jsonify({
            "status": "ok",
            "preview_sql": sql,
            "params": parsed["params"],
            "parsed": parsed
        })
    except NLParseError as e:
        return jsonify({"status": "error", "message": str(e)}), 400
    except Exception as e:
        return jsonify({"status": "error", "message": "Internal error"}), 500


@app.route("/execute", methods=["POST"])
def execute():
    # For safety, execution allowed only if ENABLE_EXECUTE env var is set to '1'
    import os
    if os.environ.get("ENABLE_EXECUTE") != "1":
        return jsonify({"status": "error", "message": "Execution disabled on this server"}), 403
    data = request.get_json() or {}
    q = data.get("query", "")
    try:
        parsed = parser.parse(q)
        validate_parsed(parsed)
        cols = ", ".join(parsed["columns"]) if parsed["columns"] else "*"
        sql = f"SELECT {cols} FROM {parsed['table']}"
        if parsed["where_sql"]:
            sql = sql + " WHERE " + parsed["where_sql"]
        # execute parameterized
        conn = sqlite3.connect(DB_PATH)
        cur = conn.cursor()
        cur.execute(sql, parsed["params"])
        rows = cur.fetchall()
        cols_names = [d[0] for d in cur.description] if cur.description else []
        conn.close()
        return jsonify({"status": "ok", "rows": rows, "columns": cols_names})
    except NLParseError as e:
        return jsonify({"status": "error", "message": str(e)}), 400
    except Exception as e:
        return jsonify({"status": "error", "message": "Execution error"}), 500


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5003)
