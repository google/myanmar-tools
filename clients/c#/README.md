# Myanmar Tools C# Documentation

This documentation is for C# specific usage of *Myanmar Tools*.
See https://github.com/google/myanmar-tools/ for more.

## Usage

To detect Zawgyi, create a instance of ZawgyiDetector, and call `GetZawgyiProbability` with your string.

```csharp
var detector = new ZawgyiDetector();
double score = detector.GetZawgyiProbability("မ္း");
// score is now 0.999772 (very likely Zawgyi)
```

To convert between Zawgyi and Unicode, use the classe `ZawgyiConverter` as shown below.

```csharp
var converter= new ZawgyiConverter();
// Zawgyi to Unicode
string uniOutput = converter.ZawgyiToUnicode('မ္း');
// uniOutput is now 'မ်း'

// Unicode to Zawgyi
string zawOutput = converter.UnicodeToZawgyi('မ်း');
// zawOutput is now 'မ္း'
```

## Contributing

Bug reports and pull requests are welcome on GitHub at https://github.com/google/myanmar-tools . This project is intended to be a safe, welcoming space for collaboration, and contributors are expected to adhere to the [Contributor Covenant](http://contributor-covenant.org) code of conduct.

## License

The package is available as open source under the terms of the [Apache-2.0 License](http://www.apache.org/licenses/LICENSE-2.0).
