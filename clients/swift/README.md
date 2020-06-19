# MyanmarTools

Swift bindings for Myanmar Tools (Zawgyi detection and conversion).

See https://github.com/google/myanmar-tools/ for more.

## Usage

```
import MyanmarTools

let detector = ZawgyiDetector()
print(detector.getZawgyiProbability(input))
```

