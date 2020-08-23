# Myanmar Tools JavaScript Documentation

This documentation is for JavaScript specific usage of *Myanmar Tools*.  For general documentation, see [the top-level README](../../README.md).

## Node.JS Usage

Add the dependency to your project:

```bash
$ npm install --save myanmar-tools
```

To detect Zawgyi, create an instance of ZawgyiDetector, and call `getZawgyiProbability` with your string.

```js
const google_myanmar_tools = require("myanmar-tools"); 
const detector = new google_myanmar_tools.ZawgyiDetector();
const score = detector.getZawgyiProbability("မ္း");
// score is now 0.9994253818489524 (very likely Zawgyi)
```

Zawgyi can be converted to Unicode as follows:

```js
const google_myanmar_tools = require("myanmar-tools");
const converter = new google_myanmar_tools.ZawgyiConverter();
const output = converter.zawgyiToUnicode("မ္း");
// output is now "မ်း"
```

You can use `converter.unicodeToZawgyi("...")` to convert in the opposite direction when possible.

For a complete working example, see [samples/node/demo.js](../../samples/node/demo.js).

## Browser Usage

Include the file *zawgyi_detector.min.js*.  It is available on [Google Hosted Libraries](https://developers.google.com/speed/libraries/#myanmar-tools):

```html
<script src="https://ajax.googleapis.com/ajax/libs/myanmar-tools/1.2.1/zawgyi_detector.min.js"></script>
```

After doing this, the ZawgyiDetector will be available as the global `google_myanmar_tools.ZawgyiDetector`, and you can use it the same way as above:

```js
const detector = new google_myanmar_tools.ZawgyiDetector();
const score = detector.getZawgyiProbability("မ္း");
// score is now 0.0.9997572675217831 (very likely Zawgyi)
```

Likewise for the converter:

```html
<script src="https://ajax.googleapis.com/ajax/libs/myanmar-tools/1.2.1/zawgyi_converter.min.js"></script>
```

```js
const converter = new google_myanmar_tools.ZawgyiConverter();
const output = converter.zawgyiToUnicode("မ္း");
// output is now "မ်း"
```
