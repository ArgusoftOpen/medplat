from typing import Dict, List, Tuple

# Example schema mapping: {table: {user_keyword: column_name}}
SCHEMA_MAPPING = {
    'patients': {
        'name': 'name',
        'age': 'age',
        'gender': 'gender',
    },
    'appointments': {
        'date': 'appointment_date',
        'doctor': 'doctor_name',
    }
}

def map_keywords_to_schema(keywords: List[str]) -> List[Tuple[str, str]]:
    mapped = []
    for kw in keywords:
        for table, columns in SCHEMA_MAPPING.items():
            if kw in columns:
                mapped.append((table, columns[kw]))
    return mapped
