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
