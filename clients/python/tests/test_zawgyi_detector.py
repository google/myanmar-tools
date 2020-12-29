"""Tests for Zawgyi detector module."""

import csv
from importlib.resources import open_text
import unittest

from myanmartools import ZawgyiDetector


class TestZawgyiDetector(unittest.TestCase):
    """Test for ZawgyiDetector class."""

    def setUp(self):
        """Create a ZawgyiDetector instance."""
        self.detector = ZawgyiDetector()

    def test_strong(self):
        """Test a Unicode string and a Zawgyi string."""
        strong_unicode = 'အပြည်ပြည်ဆိုင်ရာ လူ့အခွင့်အရေး ကြေညာစာတမ်း'
        strong_zawgyi = 'အျပည္ျပည္ဆိုင္ရာ လူ႔အခြင့္အေရး ေၾကညာစာတမ္း'
        self.assertLess(
            self.detector.get_zawgyi_probability(strong_unicode),
            0.001
        )
        self.assertGreater(
            self.detector.get_zawgyi_probability(strong_zawgyi),
            0.999
        )

    def test_compatibility(self):
        """Compare results with Java results."""
        with open_text('myanmartools.resources', 'compatibility.tsv') as f:
            for row in csv.reader(f, delimiter='\t'):
                self.assertAlmostEqual(
                    self.detector.get_zawgyi_probability(row[1]),
                    float(row[0])
                )
