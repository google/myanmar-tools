const assert = require("assert");
const google_myanmar_tools = require("myanmar-tools");

const detector = new google_myanmar_tools.ZawgyiDetector();
const converter = new google_myanmar_tools.ZawgyiConverter();

// Unicode string:
const input1 = "အပြည်ပြည်ဆိုင်ရာ လူ့အခွင့်အရေး ကြေညာစာတမ်း";
// Zawgyi string:
const input2 = "အျပည္ျပည္ဆိုင္ရာ လူ႔အခြင့္အေရး ေၾကညာစာတမ္း";

// Detect that the second string is Zawgyi:
const score1 = detector.getZawgyiProbability(input1);
const score2 = detector.getZawgyiProbability(input2);
assert(score1 < 0.001);
assert(score2 > 0.999);
console.log("Unicode Score:", score1.toFixed(6));
console.log("Zawgyi Score:", score2.toFixed(6));

// Convert the second string to Unicode:
const input2converted = converter.zawgyiToUnicode(input2);
assert.equal(input1, input2converted);
console.log("Converted Text:", input2converted);
