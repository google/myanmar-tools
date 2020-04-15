import csv, unittest
from importlib.resources import open_text
from myanmartools import ZawgyiDetector

class TestZawgyiDetector(unittest.TestCase):
    '''
    Tests the ZawgyiDetector class.
    '''
    def setUp(self):
        '''
        Creates a ZawgyiDetector object for testing.
        '''
        self.detector = ZawgyiDetector()

    def test_strong(self):
        '''
        Tests a Unicode string which should produce a very low score
        and a Zawgyi string which should produce a very high score.
        '''
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
        '''
        Compares results with those obtained from Java version of the
        detector. They should be the same, within float tolerances.
        '''
        with open_text('myanmartools.resources', 'compatibility.tsv') as f:
            for row in csv.reader(f, delimiter='\t'):
                self.assertAlmostEqual(
                    self.detector.get_zawgyi_probability(row[1]),
                    float(row[0])
                )

if __name__ == '__main__':
    unittest.main()
