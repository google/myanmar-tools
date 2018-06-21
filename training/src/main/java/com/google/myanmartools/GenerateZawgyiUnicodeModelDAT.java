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

import com.google.myanmartools.ZawgyiUnicodeMarkovModelBuilder.Category;
import java.io.IOException;
import java.util.List;

/** A Java binary used for training the Markov model at build time. */
public final class GenerateZawgyiUnicodeModelDAT {

  /**
   * Trains a ZawgyiUnicodeMarkovModel on the training data found in this directory. Outputs the
   * model in the intermediate binary data format.
   */
  public static void main(String[] args) throws IOException {
    if (args.length < 1) {
      throw new RuntimeException("\n# # # # # # # #\n"
          + "Call this binary with at least one argument:\n"
          + "the path to your CorpusCrawler output directory.\n"
          + "If running from make train, provide the path in CORPUS like this:\n"
          + "$ make train CORPUS=\"/path/to/corpus/crawler/output\"\n"
          + "If running from MVN, provide the path in the argument exec.args like this:\n"
          + "$ mvn -q exec:java -Dexec.args=\"/path/to/corpus/crawler/output\"\n"
          + "# # # # # # # #");
    }
    BurmeseData.DATA_DIRECTORY = args[0];

    int ssv = 0;
    if (args.length >= 2 && args[1].equals("1")) {
      ssv = 1;
    }

    ZawgyiUnicodeMarkovModelBuilder builder = new ZawgyiUnicodeMarkovModelBuilder(ssv);
    List<String> unicodeData = BurmeseData.getUnicodeTrainingData();
    List<String> zawgyiData = BurmeseData.getZawgyiTrainingData();

    for (String input : unicodeData) {
      builder.trainOnString(input, Category.UNICODE);
    }
    for (String input : zawgyiData) {
      builder.trainOnString(input, Category.ZAWGYI);
    }

    // Dump the newly trained model to standard out
    builder.buildToStream(System.out);
    System.out.flush();
  }
}
