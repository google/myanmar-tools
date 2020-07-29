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

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * An internal class to read Zawgyi/Unicode training and test data from the data files and expose it
 * via an API.
 */
public final class BurmeseData {

  static volatile String DATA_DIRECTORY;

  private static final long SEED = 2017L;
  private static final double TRAINING_FRACTION = 0.9;

  private static volatile ImmutableList<String> UNICODE_STRINGS = null;
  private static volatile ImmutableList<String> ZAWGYI_STRINGS = null;

  // For readability, expand the getData() function to four functions with better names.

  public static List<String> getZawgyiTrainingData() throws IOException {
    return getData(true, true);
  }

  public static List<String> getUnicodeTrainingData() throws IOException {
    return getData(false, true);
  }

  public static List<String> getZawgyiTestingData() throws IOException {
    return getData(true, false);
  }

  public static List<String> getUnicodeTestingData() throws IOException {
    return getData(false, false);
  }

  /**
   * Returns a list of strings to be used as training or testing data for either Unicode or Zawgyi,
   * depending on the arguments.
   */
  private static List<String> getData(boolean zawgyi, boolean training) throws IOException {
    if (ZAWGYI_STRINGS == null || UNICODE_STRINGS == null) {
      loadAllStrings();
    }
    List<String> strings = zawgyi ? ZAWGYI_STRINGS : UNICODE_STRINGS;

    Random rnd = new Random(SEED);
    Predicate<String> predicate =
        s ->
            training
                ? (rnd.nextDouble() < TRAINING_FRACTION)
                : (rnd.nextDouble() > TRAINING_FRACTION);

    return strings.stream().filter(predicate).collect(toImmutableList());
  }

  private static void loadAllStrings() throws IOException {
    // Goal: 50% Zawgyi, 25% Burmese Unicode, and 25% Minority Unicode.
    ZAWGYI_STRINGS = loadStrings("my-t-d0-zawgyi.txt");
    int zawgyiCharCount = ZAWGYI_STRINGS.stream().mapToInt(line -> line.length()).sum();
    ImmutableList.Builder<String> unicodeBuilder = ImmutableList.builder();
    unicodeBuilder.addAll(loadStringsToLength("my.txt", zawgyiCharCount / 2));
    unicodeBuilder.addAll(loadStringsToLength("shn.txt", zawgyiCharCount / 8));
    unicodeBuilder.addAll(loadStringsToLength("mnw.txt", zawgyiCharCount / 8));
    unicodeBuilder.addAll(loadStringsToLength("kar.txt", zawgyiCharCount / 8));
    unicodeBuilder.addAll(loadStringsToLength("pi-Mymr.txt", zawgyiCharCount / 8));
    UNICODE_STRINGS = unicodeBuilder.build();
    int unicodeCharCount = UNICODE_STRINGS.stream().mapToInt(line -> line.length()).sum();
    // System.err.println(zawgyiCharCount + " " + unicodeCharCount);
  }

  private static ImmutableList<String> loadStringsToLength(String path, int maxLength) throws IOException {
    AtomicInteger ai = new AtomicInteger();
    return loadStrings(path)
        .stream()
        .filter(line -> {
          int length = ai.addAndGet(line.length());
          return length < maxLength;
        })
        .collect(toImmutableList());
  }

  private static ImmutableList<String> loadStrings(String path) throws IOException {
    path = DATA_DIRECTORY + "/" + path;
    try (Stream<String> lines = Files.asCharSource(new File(path), Charsets.UTF_8).lines()) {
      return lines
          .map(line -> line.trim())
          .filter(line -> !line.isEmpty())
          .filter(line -> line.charAt(0) != '#')
          .collect(toImmutableList());
    }
  }
}
