# Zawgyi Detector Binary Data

This project builds the binary data file used at runtime in all of the client
implementations.  The model is trained from the text corpora provided when
running the training code in this project.

## Custom Data

### CorpusCrawler Corpus

Refer to the top-level README for instructions on re-training the model with a
custom corpuscrawler corpus.

### Fully Custom Data

It is possible to use a different data source other than corpuscrawler.  To do
this, replace the file `BurmeseData.java` with a custom implementation that
implements the four methods.  For example:

```java
package com.google.myanmartools;

import java.io.IOException;
import java.util.List;

public final class BurmeseData {

  // Dummy; not used
  static volatile String DATA_DIRECTORY;

  public static List<String> getZawgyiTrainingData() throws IOException {
    // ...
  }

  public static List<String> getUnicodeTrainingData() throws IOException {
    // ...
  }

  public static List<String> getZawgyiTestingData() throws IOException {
    // ...
  }

  public static List<String> getUnicodeTestingData() throws IOException {
    // ...
  }
}
```

## Compatibility Test Data

The file `compatibility.tsv` contains tab-separated values representing the
exact expected return value of the getZawgyiProbability API. APIs in other
languages should use this data file to assert that they have the same behavior
as the reference Java implementation.

If the training data is updated, the compatibility test data needs to be re-
generated. To do this, run `make compatibility.tsv` at the top level.

## Data file format

The binary data file format is as follows:

    <LONG: model magic number>
    <INT: model serial version number>
    <BMARKOV: unicode chain>

The type "BMARKOV" encodes the difference between the transition matrices for
the Markov chains. The format is:

    <LONG: bmarkov magic number>
    <INT: bmarkov serial version number>
    <SHORT: SIZE of markov chain>
    [ROWS]

The number of rows is equal to the SIZE of the markov chain.  To save space,
each row is saved sparsely.

    <SHORT: number of entries>
    <FLOAT32: fallback value> only if number of entries is nonzero
    [ENTRIES]

The value of each cell in the current row is the fallback value, unless another
value is given in the list of entries. If there are no entries, the default
value is 0.0 and a default value is not explicitly given in the binary file.
There are between 0 and SIZE entries in each row, inclusive. The format for each
entry is:

    <SHORT: column index>
    <FLOAT32: value>

See the files *MarkovChain.java* and *ZawgyiUnicodeMarkovModel.java* for an
example of reading and writing the binary data format.

In accordance with Java conventions, all number types are stored big-endian.
