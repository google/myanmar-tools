# Myanmar Tools Dart(Flutter) Documentation

This documentation is for Dart(Flutter) specific usage of *Myanmar Tools*.
See https://github.com/google/myanmar-tools/ for more.

## Usage

To detect Zawgyi, create a final instance of ZawgyiDetector, and call `getZawgyiProbability` with your string.

```dart
final _detector = await ZawGyiDetector.create();
double score = _detector.getZawGyiProbability("မ္း");
// score is now 0.999772 (very likely Zawgyi)
```

To convert between Zawgyi and Unicode, use the classe `ZawGyiConverter` as shown below.

```dart
final converter= ZawGyiConverter.create();
// Zawgyi to Unicode
String uniOutput = converter.zawGyiToUnicode('မ္း');
// uniOutput is now 'မ်း'

// Unicode to Zawgyi
String zawOutput = converter.zawGyiToUnicode('မ်း');
// zawOutput is now 'မ္း'
```

## Contributing

Bug reports and pull requests are welcome on GitHub at https://github.com/google/myanmar-tools . This project is intended to be a safe, welcoming space for collaboration, and contributors are expected to adhere to the [Contributor Covenant](http://contributor-covenant.org) code of conduct.

## License

The package is available as open source under the terms of the [Apache-2.0 License](http://www.apache.org/licenses/LICENSE-2.0).
