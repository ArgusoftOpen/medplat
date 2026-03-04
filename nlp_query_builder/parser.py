import re
import json
from typing import List, Dict, Any, Tuple

OPERATORS = {
    "=": "=",
    ">": ">",
    "<": "<",
    ">=": ">=",
    "<=": "<=",
    "!=": "!=",
    "like": "LIKE"
}

NUMERIC_WORDS = {
    "greater than": ">",
    "less than": "<",
    "greater or equal to": ">=",
    "less or equal to": "<=",
    "equals": "=",
    "equal to": "="
}
DATE_WORDS = {
    "before": "<",
    "after": ">",
    "on": "="
}


class NLParseError(Exception):
    pass


class RuleBasedParser:
    def __init__(self, schema_path: str):
        with open(schema_path, "r", encoding="utf-8") as f:
            self.schema = json.load(f)
        # Build quick lookup maps
        self.table_syn_map = {}
        for t, info in self.schema.get("tables", {}).items():
            for syn in info.get("synonyms", []) + [t]:
                self.table_syn_map[syn.lower()] = t

        self.col_syn_map = {}
        for col, syns in self.schema.get("column_synonyms", {}).items():
            for s in syns + [col]:
                self.col_syn_map[s.lower()] = col

    def _find_table(self, text: str) -> Tuple[str, List[str]]:
        # look for table synonyms in text
        found = []
        for syn, t in self.table_syn_map.items():
            if re.search(r"\b" + re.escape(syn) + r"\b", text, re.I):
                found.append((syn, t))
        if not found:
            raise NLParseError("No table referenced or supported in query.")
        # pick the longest matched synonym (best guess)
        chosen = max(found, key=lambda x: len(x[0]))
        return chosen[1], [t for _, t in found]

    def _find_columns(self, text: str, table: str) -> List[str]:
        # If user says "show *" or "all columns"
        if re.search(r"\b(all|\*)\b", text, re.I):
            return self.schema["tables"][table]["columns"]
        # find column synonyms
        cols = []
        for syn, col in self.col_syn_map.items():
            if re.search(r"\b" + re.escape(syn) + r"\b", text, re.I):
                # Only include if column exists in table
                if col in self.schema["tables"][table]["columns"] and col not in cols:
                    cols.append(col)
        # Also allow explicitly named columns
        for col in self.schema["tables"][table]["columns"]:
            if re.search(r"\b" + re.escape(col) + r"\b", text, re.I) and col not in cols:
                cols.append(col)
        if not cols:
            # default to a safe subset (id + name if present)
            defaults = [c for c in ["id","name"] if c in self.schema["tables"][table]["columns"]]
            return defaults if defaults else [self.schema["tables"][table]["columns"][0]]
        return cols

    def _parse_where(self, text: str, table: str) -> Tuple[str, List[Any]]:
        # Very simple heuristics: look for patterns "<column> <op> <value>" or phrases like "age greater than 30"
        where_clauses = []
        params = []
        # Normalize numeric and date textual operators
        for phrase, op in NUMERIC_WORDS.items():
            text = re.sub(re.escape(phrase), op, text, flags=re.I)
        for phrase, op in DATE_WORDS.items():
            text = re.sub(r"\b" + re.escape(phrase) + r"\b", op, text, flags=re.I)

        # Split on boolean connectors while keeping them
        parts = re.split(r"\s+(and|or)\s+", text, flags=re.I)
        clauses = []
        connectors = []
        # parts will be like [part, connector, part, connector, part,...]
        for i, part in enumerate(parts):
            if i % 2 == 1:
                connectors.append(part.upper())
            else:
                clauses.append(part)

        def extract_clause(piece: str):
            # pattern: column operator value
            pattern = re.compile(r"(?P<col>[a-zA-Z_ ]{1,60})\s*(?P<op>>=|<=|!=|=|>|<|like|LIKE)\s*'?(?P<val>[^'\s,;]+)'?", re.I)
            m = pattern.search(piece)
            if m:
                col_text = m.group('col').strip().lower()
                op = m.group('op').lower()
                val = m.group('val')
                col = self.col_syn_map.get(col_text, col_text)
                if col not in self.schema['tables'][table]['columns']:
                    # try the last token as fallback (e.g. "patients where age" -> "age")
                    last = col_text.split()[-1]
                    col = self.col_syn_map.get(last, last)
                    if col not in self.schema['tables'][table]['columns']:
                        return None
                if op not in OPERATORS:
                    return None
                # cast numeric
                param = int(val) if val.isdigit() else val
                return (f"{col} {OPERATORS[op]} ?", param)
            # fallback simple "col is val"
            pattern2 = re.compile(r"\b(?P<col_word>[a-zA-Z_ ]{1,60})\s+(is|equals|=)\s+'?(?P<val2>[^'\s,;]+)'?", re.I)
            m2 = pattern2.search(piece)
            if m2:
                col_text = m2.group('col_word').strip().lower()
                val = m2.group('val2')
                col = self.col_syn_map.get(col_text, col_text)
                if col not in self.schema['tables'][table]['columns']:
                    last = col_text.split()[-1]
                    col = self.col_syn_map.get(last, last)
                    if col not in self.schema['tables'][table]['columns']:
                        return None
                param = int(val) if val.isdigit() else val
                return (f"{col} = ?", param)
            return None

        extracted = []
        for c in clauses:
            res = extract_clause(c)
            if res:
                extracted.append(res)

        if not extracted:
            return "", []

        where_parts = [e[0] for e in extracted]
        params = [e[1] for e in extracted]

        # Reconstruct boolean joins using connectors if present, default to AND
        if connectors:
            joined = where_parts[0]
            for i, conn in enumerate(connectors):
                op = ' AND ' if conn.upper() not in ('AND', 'OR') else f" {conn.upper()} "
                joined = joined + op + where_parts[i+1]
            return joined, params
        else:
            return " AND ".join(where_parts), params

    def parse(self, nl: str) -> Dict[str, Any]:
        text = nl.strip()
        if not text:
            raise NLParseError("Empty query")
        table, found_tables = self._find_table(text)
        cols = self._find_columns(text, table)
        where_clause, params = self._parse_where(text, table)
        return {
            "table": table,
            "columns": cols,
            "where_sql": where_clause,
            "params": params,
            "found_tables": found_tables
        }


if __name__ == "__main__":
    p = RuleBasedParser("schema.json")
    print(p.parse("Show name and age of patients where age > 30"))
