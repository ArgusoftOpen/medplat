import unittest
from parser import RuleBasedParser, NLParseError
import os

BASE = os.path.dirname(__file__)
SCHEMA = os.path.join(BASE, "schema.json")

class ParserTests(unittest.TestCase):
    def setUp(self):
        self.parser = RuleBasedParser(SCHEMA)

    def test_basic_select(self):
        r = self.parser.parse("Show name and age of patients")
        self.assertEqual(r['table'], 'patients')
        self.assertIn('name', r['columns'])
        self.assertIn('age', r['columns'])

    def test_where_numeric(self):
        r = self.parser.parse("patients where age > 30")
        self.assertEqual(r['where_sql'], 'age > ?')
        self.assertEqual(r['params'], [30])

    def test_where_and_or(self):
        r = self.parser.parse("patients where age > 30 and gender = F")
        # order of clauses may be age then gender
        self.assertIn('age > ?', r['where_sql'])
        self.assertIn('gender = ?', r['where_sql'])

    def test_date_before(self):
        r = self.parser.parse("visits where visit_date before 2023-01-01")
        self.assertEqual(r['where_sql'], 'visit_date < ?')
        self.assertEqual(r['params'], ['2023-01-01'])

    def test_unsupported_table(self):
        with self.assertRaises(NLParseError):
            self.parser.parse("show data of foobar")

if __name__ == '__main__':
    unittest.main()
