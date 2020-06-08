# Myanmar Tools Swift Documentation

This documentation is for *Swift* specific usage of *Myanmar Tools*.  For general documentation, see [the top-level README](../../README.md).

## Installation

**Cocoapods**

``` 
platform :ios, '(ios_version)'

source 'https://github.com/CocoaPods/Specs.git'

target 'Project-name'  do

	pod 'myanmartools' 

end 
```

## Usage

```
/**
create an instance of ZawgyiDetector class and pass string input that 
you want to predict into predict function
**/

let detector = ZawgyiDetector()
detector.predict(input: "မ္း")

// score is now 0.999772 (very likely Zawgyi)
```

## Zawgyi <-> Unicode conversion

You can check on this - https://github.com/Rabbit-Converter/Rabbit-Swift


