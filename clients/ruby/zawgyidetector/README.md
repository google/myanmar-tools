# Zawgyidetector

This documentation is for Ruby specific usage of Myanmar Tools. For general documentation, see [the top-level README](../../README.md).

## Usage

Prerequisites: Ruby >= 2.0.0-p0 and bundler >= 1.0

As this client is not released as a gem to rubygems yet, please check out the repo first and follow the steps to use:

1. After checking out the repo, go to `clients/ruby/zawgyidetector` folder and run `$ bundle install` to install dependencies.

2. Run `$ rake test` to run the test-cases.

3. Run `$ bundle exec rake build` in order to build the gem.

4. To install this gem onto your local machine, run `$ bundle exec rake install`.

5. Run `$ bundle console` for an interactive prompt that will allow you to experiment by loading the zawgyidetector gem.

6. Once irb session is available, create an object by running `detector=ZawgyiDetector.new` and run `detector.get_zawgyi_probability("အပြည်ပြည်ဆိုင်ရာ လူ့အခွင့်အရေး ကြေညာစာတမ်း")` to get score of the input.

## Contributing

Bug reports and pull requests are welcome on GitHub at https://github.com/[USERNAME]/zawgyidetector. This project is intended to be a safe, welcoming space for collaboration, and contributors are expected to adhere to the [Contributor Covenant](http://contributor-covenant.org) code of conduct.

## License

The gem is available as open source under the terms of the [Apache-2.0 License](http://www.apache.org/licenses/LICENSE-2.0).

## Code of Conduct

Everyone interacting in the Zawgyidetector project’s codebases, issue trackers, chat rooms and mailing lists is expected to follow the [code of conduct](https://github.com/[USERNAME]/zawgyidetector/blob/master/CODE_OF_CONDUCT.md).
