# Myanmar Tools Ruby Documentation

This documentation is for Ruby specific usage of *Myanmar Tools*. For general documentation, see [the top-level README](../../README.md).

## Ruby Usage

Prerequisites: Ruby >= 2.0.0-p0 and bundler >= 1.0

Add the dependency to your project:

```bash
$ gem install myanmar-tools
or
$ bundle add myanmar-tools
```

To detect Zawgyi, create an instance of ZawgyiDetector, and call `get_zawgyi_probability` with your string.

```ruby
require 'myanmar-tools'
detector = MyanmarTools::ZawgyiDetector.new
score    = detector.get_zawgyi_probability('အျပည္ျပည္ဆိုင္ရာ လူ႔အခြင့္အေရး ေၾကညာစာတမ္း')
# score is 1.0 (The input is definitely Zawgyi)
```

To convert from Zawgyi to Unicode, you can use [the Ruby wrapper over icu4c](https://github.com/fantasticfears/icu4r).  Install it like this:

```bash
$ gem install icu
or
$ bundle add icu
```

Then convert from Z to U like this:

```ruby
require 'icu'
converter = ICU::Transliterator.new "Zawgyi-my"
output    = converter.transliterate("မ္း")
# output is now "မ်း"
```

For a complete working example, see [samples/ruby/demo.rb](../../samples/ruby/demo.rb).

## Contributing

Bug reports and pull requests are welcome on GitHub at https://github.com/google/myanmar-tools . This project is intended to be a safe, welcoming space for collaboration, and contributors are expected to adhere to the [Contributor Covenant](http://contributor-covenant.org) code of conduct.

## License

The gem is available as open source under the terms of the [Apache-2.0 License](http://www.apache.org/licenses/LICENSE-2.0).

## Code of Conduct

Everyone interacting in the Myanmar-Tools project’s codebases, issue trackers, chat rooms and mailing lists is expected to follow the [code of conduct](https://github.com/[USERNAME]/zawgyidetector/blob/master/CODE_OF_CONDUCT.md).
