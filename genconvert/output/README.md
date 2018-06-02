# Myanmar Javascript Converters

These JavaScript files support conversion of JS text from Burmese Zawgyi encoding to Unicode and the reverse.

JavaScript files U2Z.js, Z2U.js, and ZNorm.js each provide a translation function that converts input to a new output.

These files are created by compiling ICU Transliteration rules into the JavaScript code and data, thus avoiding the need to include the ICU capabilities in Javascript.

Each contains rules and calls to a function runPhase, which is definited in the file runPhase.js. The transformations implemented by runPhase.js are a subset of what ICU's Transliterator provides.

TODO: describe the capabilities and limitations.

J2U.js converts Zawgyi-encoded text into Burmese Unicode.

U2Z.js converts Burmese Unicode text into Zawgyi encoding.

ZNorm.js takes Zawgyi-encoded text and produces a version of Zawgyi that is more standardized in terms of code point ordering. This allows better comparison of Burmese strings.

## Why these converters?

Conversion of Zawgyi data to Unicode is supported by the ICU Transliterator class in C++ and Java, using CLDR transform rules. However, there is no Javascript implementation of the transliterator, and the full transliterator is not needed for these Burmese transformations.

The use of these allows web developers to support Unicode and Zawgyi text, convering as needed within the web browser environment.
