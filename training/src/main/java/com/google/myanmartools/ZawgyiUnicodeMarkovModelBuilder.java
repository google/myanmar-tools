/* Copyright 2017 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.myanmartools;

import static com.google.myanmartools.ZawgyiUnicodeMarkovModel.getIndexForCodePoint;
import static com.google.myanmartools.ZawgyiUnicodeMarkovModel.getSize;

import com.google.myanmartools.BinaryMarkov;
import com.google.myanmartools.ZawgyiUnicodeMarkovModel;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/** A builder for {@link ZawgyiUnicodeMarkovModel}. */
public final class ZawgyiUnicodeMarkovModelBuilder {

  /** Magic number used to identify this object in byte streams. (Reads in ASCII as "UZMODEL") */
  private static final long BINARY_TAG = ZawgyiUnicodeMarkovModel.BINARY_TAG;

  /** Enum used for training. Not intended to be exposed outside of this package. */
  public static enum Category {
    UNICODE,
    ZAWGYI
  }

  private final BinaryMarkovBuilder classifierBuilder;
  private final int ssv;

  public ZawgyiUnicodeMarkovModelBuilder(int ssv) {
    classifierBuilder = new BinaryMarkovBuilder(getSize(ssv));
    this.ssv = ssv;
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
      int currState = getIndexForCodePoint(codePoint, ssv);

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
    dos.writeInt(2);
    // Serial version 2 includes the SSV.
    dos.writeInt(ssv);
    classifierBuilder.buildToStream(stream);
  }

  /** Convenience method to obtain a ZawgyiUnicodeMarkovModel instance directly. */
  public ZawgyiUnicodeMarkovModel buildObject() {
    BinaryMarkov classifier = classifierBuilder.buildObject();
    return new ZawgyiUnicodeMarkovModel(classifier, ssv);
  }
}
