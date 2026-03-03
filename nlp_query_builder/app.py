import re
from .query_builder import QueryBuilder
from .schema_mapping import SCHEMA_MAPPING

def main():
    print("Welcome to the NLP Query Builder!")
    qb = QueryBuilder(SCHEMA_MAPPING)
    while True:
        user_query = input("Enter your query (or 'exit'): ")
        if user_query.strip().lower() == 'exit':
            break
        try:
            sql, params = qb.build_query(user_query)
            print("\nQuery Preview:")
            print("SQL:", sql)
            print("Parameters:", params)
            confirm = input("Execute this query? (y/n): ")
            if confirm.lower() == 'y':
                print("(Execution would happen here; DB connection not implemented in this demo.)")
            else:
                print("Query cancelled.")
        except Exception as e:
            print("Error:", str(e))

if __name__ == "__main__":
    main()
