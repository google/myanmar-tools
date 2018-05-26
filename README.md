# Myanmar Tools (Zawgyi detection)

This project includes tools for processing font encodings used in Myanmar, currently with support for the widespread Zawgyi-One font encoding.  For more information on font encodings in Myanmar, read [the Unicode Myanmar FAQ](http://www.unicode.org/faq/myanmar.html).

Features:

- Detect whether a string is Zawgyi or Unicode.
- Support for C++, Java, JavaScript (both Node.js and browser) and Ruby.

Conversion from Zawgyi to Unicode is provided with the help of the CLDR Zawgyi conversion rules (see "Zawgyi-to-Unicode Conversion" below).

This is not an official Google product, but we hope that you’ll find *Myanmar Tools* useful to better support the languages of Myanmar.

## Why Myanmar Tools?

*Myanmar Tools* uses a machine learning model to give very accurate results when detecting Zawgyi versus Unicode.  Detectors that use hand-coded rules for detection are susceptible to flagging content in other languages like Shan and Mon as Zawgyi when it is actually Unicode.

*Myanmar Tools* and the CLDR Zawgyi conversion rules are used by Google, Facebook, and others to provide great experiences to users in Myanmar.

## Using the Zawgyi Detector

See language-specific documentation:

- [C++](clients/cpp/README.md)
- [Java](clients/java/README.md)
- [JavaScript](clients/js/README.md)
- [Ruby](clients/ruby/README.md)

Depending on your programming language, a typical use case should look something like this:

```java
if (zawgyiDetector.getZawgyiProbability(input) > THRESHOLD) {
    // Convert to Unicode, etc.
}
```

The method `getZawgyiProbability` returns a number between 0 and 1 to reflect the probability that a string is Zawgyi, given that it is either Zawgyi or Unicode.  For strings that are sufficiently long, the detector should return a number very close to 0 or 1, but for strings with only a few characters, the number may be closer to the middle.  With this in mind, use the following heuristics to set <code>THRESHOLD</code>:

- If *under*-predicting Zawgyi is bad (e.g., when a human gets to evaluate the result), set a low threshold like <code>0.05</code>. This threshold guarantees that fewer than 1% of Zawgyi strings will go undetected.
- If *over*-predicting Zawgyi is bad (e.g., when conversion will take place automatically), set a high threshold like <code>0.95</code>. This threshold guarantees that fewer than 1% of Unicode strings will be wrongly flagged.

Additionally, keep in mind that you may want to tune your thresholds to the distribution of strings in your input data. For example, if your input data is biased toward Unicode, in order to reduce false positives, you may want to set a higher Zawgyi threshold than if your input data is biased toward Zawgyi. Ultimately, the best way to pick thresholds is to obtain a set of labeled strings representative of the data the detector will be processing, compute their scores, and tune the thresholds to your desired ratio of precision and recall.

If a string contains a non-Burmese affix, it will get the same Zawgyi probability as if the
affix were removed.  That is, `getZawgyiProbability("hello <burmese> world")` ==
`getZawgyiProbability("<burmese>")`.

Some strings are identical in both U and Z; this can happen if the string consists of mostly consonants with few diacritic vowels. The detector may return any value for such strings. If the user is concerned with this case, they can simply run the string through a converter and check whether or not the converter's output is equal to the converter's input.


## Training the Model

The model used by the Zawgyi detector has been trained on several megabytes of data from web sites across the internet.  The data was obtained using the [Corpus Crawler](https://github.com/googlei18n/corpuscrawler/) tool.

To re-train the model, first run Corpus Crawler locally.  For example:

```bash
$ mkdir ~/corpuscrawler_output && cd ~/corpuscrawler_output
$ corpuscrawler --language my --output . &
$ corpuscrawler --language my-t-d0-zawgyi --output . &
$ corpuscrawler --language shn --output . &
```

This will take a long time, as in several days.  The longer you let it run, the better your model will be.  Note that at a minimum, you must ensure that you have obtained data for both Unicode and Zawgyi; the directory should contain `my.txt`, `my-t-d0-zawgyi.txt`, and `shn.txt`.

Once you have data available, train the model by running the following command in this directory:

```bash
$ make train CORPUS=$HOME/corpuscrawler_output
```

## Zawgyi-to-Unicode Conversion

Once determining that a piece of text is Unicode or Zawgyi, it's often useful to convert from one encoding to the other.

Although this package does not support conversion on its own, the Common Locale Data Repository (CLDR) publishes a Zawgyi-to-Unicode converter in the form of transliteration rules.  This functionality is available in [ICU](http://site.icu-project.org/) 58+ with the transform ID "Zawgyi-my":

- Java: [ICU4J Transliterator](http://icu-project.org/apiref/icu4j/com/ibm/icu/text/Transliterator.html)
- C++: [ICU4C Transliterator](http://icu-project.org/apiref/icu4c/classicu_1_1Transliterator.html)

Many other languages, including Python, Ruby, and PHP, have wrapper libraries over ICU4C, which means you can use the Zawgyi converter in those languages, too.  See the samples directory for examples on using the ICU Transliterator.

In languages without full ICU support, like JavaScript in the browser, there are other open-source Zawgyi converters available.  The [Rabbit](https://github.com/Rabbit-Converter/Rabbit) converter is available in several different languages, including JavaScript.  See [the JavaScript README](clients/js/README.md) for more details.

## Contributing New Programming Language Support

We will hapilly consider pull requests that add clients in other programming languages.  To add support for a new programming language, here are some tips:

- Add a new directory underneath `clients`.  This will be the root of your new package.
- Use a build system customary to your language.
- Add your language to the `copy-resources` and `test` rules in the top-level Makefile.
- At a minimum, your package should automatically consume `zawgyiUnicodeModel.dat` and test against `compatibility.tsv`.
- See the other clients for examples.  Most clients are only a couple hundred lines of code.
- When finished, add your client to the *.travis.yml* file.
