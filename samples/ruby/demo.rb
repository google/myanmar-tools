# IMPORTANT: Firstly, run 'bundle install' to install Myanmar-Tools and ICU converter dependencies.
# Then, run this file:
# $ ruby demo.rb

require 'myanmar-tools'
require 'icu'

detector    = MyanmarTools::ZawgyiDetector.new 
converter   = ICU::Transliterator.new 'Zawgyi-my'

# Unicode string
unicode_input   = 'အပြည်ပြည်ဆိုင်ရာ လူ့အခွင့်အရေး ကြေညာစာတမ်း'

# Zawgyi string
zawgyi_input    = 'အျပည္ျပည္ဆိုင္ရာ လူ႔အခြင့္အေရး ေၾကညာစာတမ္း'

unicode_score   = detector.get_zawgyi_probability(unicode_input)
zawgyi_score    = detector.get_zawgyi_probability(zawgyi_input)

raise 'Wrong unicode score' unless unicode_score < 0.001
raise 'Wrong zawgyi score' unless zawgyi_score > 0.999

puts 'Unicode Score: %0.6f' % unicode_score
puts 'Zawgyi Score: %0.6f' % zawgyi_score

# Convert zawgyi input to to unicode
zawgyi_input_converted  = converter.transliterate(zawgyi_input)
raise 'The two inputs do not match: ' unless zawgyi_input_converted == unicode_input

puts "Converted Text: #{zawgyi_input_converted}"
