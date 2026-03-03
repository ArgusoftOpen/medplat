import re
from typing import Dict, List, Tuple, Any

class RuleBasedNLPParser:
    """
    Parses natural language queries into structured query intents using rule-based logic.
    """
    def __init__(self, schema_mapping: Dict[str, Dict[str, str]]):
        self.schema_mapping = schema_mapping

    def parse(self, user_query: str) -> Dict[str, Any]:
        # Very basic rule-based parsing for demo purposes
        user_query = user_query.lower()
        intent = {'select': [], 'where': []}
        for table, columns in self.schema_mapping.items():
            for col_alias, col_name in columns.items():
                if col_alias in user_query:
                    intent['select'].append((table, col_name))
        # Extract simple WHERE conditions (e.g., 'where age > 30')
        where_match = re.search(r'where (.+)', user_query)
        if where_match:
            intent['where'].append(where_match.group(1))
        return intent
