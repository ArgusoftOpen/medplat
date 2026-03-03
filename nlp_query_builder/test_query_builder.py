# Test cases for the NLP Query Builder
import unittest
from nlp_query_builder.query_builder import QueryBuilder
from nlp_query_builder.schema_mapping import SCHEMA_MAPPING

class TestQueryBuilder(unittest.TestCase):
    def setUp(self):
        self.qb = QueryBuilder(SCHEMA_MAPPING)

    def test_simple_select(self):
        sql, params = self.qb.build_query("Show me name and age")
        self.assertIn("SELECT", sql)
        self.assertIn("name", sql)
        self.assertIn("age", sql)
        self.assertEqual(params, ())

    def test_where_clause(self):
        sql, params = self.qb.build_query("Show me name where age > 30")
        self.assertIn("WHERE", sql)
        self.assertEqual(params, ("30",))

    def test_invalid_column(self):
        with self.assertRaises(ValueError):
            self.qb.build_query("Show me salary")

    def test_invalid_where(self):
        with self.assertRaises(ValueError):
            self.qb.build_query("Show me name where age is old")

if __name__ == "__main__":
    unittest.main()
