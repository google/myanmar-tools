# Myanmar Tools (Zawgyi detection)

This project includes tools for processing font encodings used in Myanmar, currently with support for the widespread Zawgyi-One font encoding.  For more information on font encodings in Myanmar, read [the Unicode Myanmar FAQ](http://www.unicode.org/faq/myanmar.html).

Features:

- Detect whether a string is Zawgyi or Unicode.
- Support for C++, Java, and JavaScript (both Node.js and browser).

This is not an official Google product, but we hope that you’ll find this useful to better support the languages of Myanmar.

## Training the Model

The model used by the Zawgyi detector has been trained on several megabytes of data from web sites across the internet.  The data was obtained using the [Corpus Crawler](https://github.com/googlei18n/corpuscrawler/) tool.

To re-train the model, first run Corpus Crawler locally.  For example:

    $ mkdir ~/corpuscrawler_output && cd ~/corpuscrawler_output
    $ corpuscrawler --language my --output . &
    $ corpuscrawler --language shn --output . &

This will take a long time, as in several days.  The longer you let it run, the better your model will be.  Note that at a minimum, you must ensure that you have obtained data for both Unicode and Zawgyi; the directory should contain `my.txt`, `my-t-d0-zawgyi.txt`, and `shn.txt`.

Once you have data available, train the model by running the following command in this directory:

    $ make train CORPUS=$HOME/corpuscrawler_output

## Contributing New Programming Language Support

We will hapilly consider pull requests that add new programming language support.  To add support for a new programming language, here are some tips:

- Add a new directory underneath `clients`.  This will be the root of your new package.
- Use a build system customary to your language.
- Add your language to the `copy-resources` and `test` rules in the top-level Makefile.
- At a minimum, your package should automatically consume `zawgyiUnicodeModel.dat` and test against `compatibility.tsv`.
- See the other clients for examples.  Most clients are only a couple hundred lines of code.
