package com.google.i18n.myanmar;

import com.google.i18n.myanmar.ZawgyiUnicodeMarkovModelBuilder.Category;
import java.io.IOException;
import java.util.List;

/** A Java binary used for training the Markov model at build time. */
public final class GenerateZawgyiUnicodeModelDAT {

  /**
   * Trains a ZawgyiUnicodeMarkovModel on the training data found in this directory. Outputs the
   * model in the intermediate binary data format.
   */
  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      throw new RuntimeException("\n# # # # # # # #\n"
          + "Call this binary with one argument, the path to your CorpusCrawler output directory.\n"
          + "If running from make train, provide the path in CORPUS like this:\n"
          + "$ make train CORPUS=\"/path/to/corpus/crawler/output\"\n"
          + "If running from MVN, provide the path in the argument exec.args like this:\n"
          + "$ mvn -q exec:java -Dexec.args=\"/path/to/corpus/crawler/output\"\n"
          + "# # # # # # # #");
    }
    BurmeseData.DATA_DIRECTORY = args[0];

    ZawgyiUnicodeMarkovModelBuilder builder = new ZawgyiUnicodeMarkovModelBuilder();
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
