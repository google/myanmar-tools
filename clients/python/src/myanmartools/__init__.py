'''
Myanmar Tools
=============

Myanmar Tools implements tools for processing font encodings used in Myanmar.
It currently supports Zawgyi detection.

To detect Zawgyi, create an instance of ZawgyiDetector, and call
``get_zawgyi_probability`` with a string::

    from myanmartools import ZawgyiDetector

    detector = ZawgyiDetector()
    score = detector.get_zawgyi_probability('မ္း')
    # score is now 0.999772 (very likely Zawgyi)

For Zawgyi-to-Unicode conversion, you can use the ICU library. Install it
using ``pip install PyICU``.

To convert Zawgyi to Unicode, create an instance of ICU Transliterator with
the transform ID "Zawgyi-my", and call :code:`transiliterate` with a string::

    from icu import Transliterator

    converter = Transliterator.createInstance('Zawgyi-my')
    output = converter.transliterate('မ္း')
    # output is now 'မ်း'
'''
from .zawgyi_detector import ZawgyiDetector
