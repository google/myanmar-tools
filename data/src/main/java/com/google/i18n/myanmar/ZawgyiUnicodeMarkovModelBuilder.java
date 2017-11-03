package com.google.i18n.myanmar;

import static com.google.i18n.myanmar.ZawgyiUnicodeMarkovModel.getIndexForCodePoint;
import static com.google.i18n.myanmar.ZawgyiUnicodeMarkovModel.getSize;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/** A builder for {@link ZawgyiUnicodeMarkovModel}. */
public final class ZawgyiUnicodeMarkovModelBuilder {

  /** Magic number used to identify this object in byte streams. (Reads in ASCII as "UZMODEL") */
  private static final long BINARY_TAG = 0x555A4D4F44454C20L;

  /** Current serial format version number, used in association with the magic number. */
  private static final int BINARY_VERSION = 1;

  /** Enum used for training. Not intended to be exposed outside of this package. */
  public static enum Category {
    UNICODE,
    ZAWGYI
  }

  private final BinaryMarkovBuilder classifierBuilder;

  public ZawgyiUnicodeMarkovModelBuilder() {
    classifierBuilder = new BinaryMarkovBuilder(getSize());
  }

  /**
   * Adds the given string to this Markov model.
   *
   * @param input The training string.
   * @param category The category corresponding to the training string.
   * @return The Builder, for chaining.
   */
  public ZawgyiUnicodeMarkovModelBuilder trainOnString(String input, Category category) {
    boolean classA = (category == Category.UNICODE);

    // Start at the base state
    int prevState = 0;

    for (int offset = 0; offset < input.length(); ) {
      int codePoint = input.codePointAt(offset);
      int currState = getIndexForCodePoint(codePoint);

      // Add this transition to the Markov chain
      // Ignore 0-to-0 transitions
      if (prevState != 0 || currState != 0) {
        classifierBuilder.addEdge(prevState, currState, classA);
      }

      offset += Character.charCount(codePoint);
      prevState = currState;
    }

    // Return to the base state at the end
    classifierBuilder.addEdge(prevState, 0, classA);

    return this;
  }

  /**
   * Exports this instance to a binary data stream.
   *
   * @throws IOException If there was a problem writing the data.
   */
  public void buildToStream(OutputStream stream) throws IOException {
    DataOutputStream dos = new DataOutputStream(stream);
    // Write magic number and serial version number
    dos.writeLong(BINARY_TAG);
    dos.writeInt(BINARY_VERSION);
    classifierBuilder.buildToStream(stream);
  }

  /** Convenience method to obtain a ZawgyiUnicodeMarkovModel instance directly. */
  public ZawgyiUnicodeMarkovModel buildObject() {
    BinaryMarkov classifier = classifierBuilder.buildObject();
    return new ZawgyiUnicodeMarkovModel(classifier);
  }
}
