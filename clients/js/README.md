# Myanmar Tools JavaScript Documentation

This documentation is for Java specific usage of *Myanmar Tools*.  For general documentation, see [the top-level README](../../README.md).

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
// score is now 0.999772 (very likely Zawgyi)
```

To convert Zawgyi to Unicode, you can look at [third-party packages on npm](https://www.npmjs.com/search?q=zawgyi%20convert).  For example, using the package "rabbit-node":

```js
const Rabbit = require("rabbit-node");
const output = Rabbit.zg2uni("မ္း");
// output is now "မ်း"
```

Note: Google does not endorse any of the available third-party packages over any other.

For a complete working example, see [samples/node/demo.js](../../samples/node/demo.js).

## Browser Usage

Include the file *zawgyi_detector.min.js*.  It is available on [Google Hosted Libraries](https://developers.google.com/speed/libraries/#myanmar-tools):

```html
<script src="https://ajax.googleapis.com/ajax/libs/myanmar-tools/1.0.1/zawgyi_detector.min.js"></script>
```

After doing this, the ZawgyiDetector will be available as the global `google_myanmar_tools.ZawgyiDetector`, and you can use it the same way as above:

```js
const detector = new google_myanmar_tools.ZawgyiDetector();
const score = detector.getZawgyiProbability("မ္း");
// score is now 0.999772 (very likely Zawgyi)
```
