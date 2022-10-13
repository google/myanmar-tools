# Copyright 2018 Aung Kyaw Phyo

# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at

#     http://www.apache.org/licenses/LICENSE-2.0

# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

require 'test/unit'
require 'myanmar-tools'
require 'csv'

class ZawgyiDetectorTest < Test::Unit::TestCase
  def setup
    @detector = MyanmarTools::ZawgyiDetector.new
  end

  def test_ignore_non_myanmar_code_points
    all_ascii     = 'blah blah blah blah blah'
    mixed_unicode = '<span>blah blah ဒဂုန်ဦးစန်းငွေ </span> blah blah blah blah'
    mixed_zawgyi  = 'blah blah blah blah blah သို႔သြားပါ။ blah blah blah'

    assert_equal (-1.0 / 0.0), @detector.get_zawgyi_probability(all_ascii)
    assert_operator 0.01, :>, @detector.get_zawgyi_probability(mixed_unicode)
    assert_operator 0.99, :<, @detector.get_zawgyi_probability(mixed_zawgyi)
  end

  def test_return_low_score_for_strong_unicode
    strong_unicode = 'အပြည်ပြည်ဆိုင်ရာ လူ့အခွင့်အရေး ကြေညာစာတမ်း'
    assert_operator 0.001, :>, @detector.get_zawgyi_probability(strong_unicode)
  end

  def test_return_hgih_score_for_strong_zawgyi
    strong_zawgyi = 'အျပည္ျပည္ဆိုင္ရာ လူ႔အခြင့္အေရး ေၾကညာစာတမ္း'
    assert_operator 0.999, :<, @detector.get_zawgyi_probability(strong_zawgyi)
  end

  def test_ignore_numerals
    # Digits/numerals are ignored and treated like non-Myanmar code points.
    num_in_mm_with_non_mm_code_point = '၉၆.၀ kHz'
    num_in_mm_with_mm_code_point     = '၂၄၀၉ ဒဂုန်'
    mm_code_point                       = 'ဒဂုန်'

    assert_equal (-1.0 / 0.0), @detector.get_zawgyi_probability(num_in_mm_with_non_mm_code_point)
    assert_equal @detector.get_zawgyi_probability(num_in_mm_with_mm_code_point), @detector.get_zawgyi_probability(mm_code_point)
  end

  def test_difficult_code_points
    # These are strings that the detector has gotten wrong. They mostly render in both Zawgyi and
    # Unicode, but the words they spell make sense in one encoding but not in the other. As the
    # detector improves, change this test case with scores to match the new output.
    cases = []
    # STRINGS IDENTICAL IN UNICODE/ZAWGYI
    cases << {cp: "အသံကို အစားထိုးလိုပါသလား။", score: 0.995}
    cases << {cp: "နမူနာ", score: 0.26}
    cases << {cp: " ဦး", score: 0.35}

    # UNICODE STRINGS WITH HIGH ZAWGYI SCORES
    cases << {cp: "အစားထိုး အထူးအက္ခရာ", score: 0.995}
    cases << {cp: "ယခု မိုးရွာနေပါသလား။", score: 0.995}
    cases << {cp: "အခြား", score: 0.74}

    # DIFFICULT STRINGS THAT DETECT CORRECTLY
    cases << {cp: "ကာမစာအုပ္မ်ား(ေစာက္ပတ္စာအုပ္မ်ား)", score: 1.0}
    cases << {cp: "ညႇပ္စရာမလို", score: 0.82}

    cases.each{|c|            
        result = @detector.get_zawgyi_probability(c[:cp])
        assert_in_delta 0.005,result,c[:score]
    }
  end

  def test_compatibility
    file_path = File.join(File.dirname(File.dirname(__FILE__)),'/lib/myanmar-tools/resources/compatibility.tsv')
    CSV.read(file_path, col_sep: "\t").each{ |line|
      expected = line[0] == '-Infinity' ? -1.0/0.0 : line[0].to_f
      assert_equal expected, @detector.get_zawgyi_probability(line[1])
    }
  end
end
