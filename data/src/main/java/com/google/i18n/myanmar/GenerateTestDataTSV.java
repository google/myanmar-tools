package com.google.i18n.myanmar;

import java.io.IOException;

/** A Java binary used for creating test data from a corpus. */
public final class GenerateTestDataTSV {

  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      throw new RuntimeException(
          "\n# # # # # # # #\n"
              + "Call this binary with one argument, the path to your CorpusCrawler output directory.\n"
              + "# # # # # # # #");
    }
    BurmeseData.DATA_DIRECTORY = args[0];

    for (String str : BurmeseData.getUnicodeTestingData()) {
      System.out.print("U\t");
      System.out.println(str);
    }
    for (String str : BurmeseData.getZawgyiTestingData()) {
      System.out.print("Z\t");
      System.out.println(str);
    }
    System.out.flush();
  }
}
