import sqlite3
from typing import Dict, Any, Tuple
from .rule_based_parser import RuleBasedNLPParser
from .schema_mapping import SCHEMA_MAPPING

class QueryBuilder:
    def __init__(self, schema_mapping: Dict[str, Dict[str, str]]):
        self.parser = RuleBasedNLPParser(schema_mapping)

    def build_query(self, user_query: str) -> Tuple[str, Tuple[Any, ...]]:
        intent = self.parser.parse(user_query)
        select_cols = [f"{tbl}.{col}" for tbl, col in intent['select']]
        if not select_cols:
            raise ValueError("No valid columns found in query.")
        tables = set(tbl for tbl, _ in intent['select'])
        sql = f"SELECT {', '.join(select_cols)} FROM {', '.join(tables)}"
        params = ()
        if intent['where']:
            # For demo: only allow simple numeric conditions, parameterized
            where = intent['where'][0]
            match = re.match(r'(\w+)\s*(=|>|<|>=|<=)\s*(\d+)', where)
            if match:
                col, op, val = match.groups()
                for tbl, columns in SCHEMA_MAPPING.items():
                    if col in columns:
                        sql += f" WHERE {tbl}.{columns[col]} {op} ?"
                        params = (val,)
                        break
            else:
                raise ValueError("Unsupported WHERE clause.")
        return sql, params
