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
    List<String> strings = zawgyi ? loadZawgyiStrings() : loadUnicodeStrings();

    Random rnd = new Random(SEED);
    Predicate<String> predicate =
        s ->
            training
                ? (rnd.nextDouble() < TRAINING_FRACTION)
                : (rnd.nextDouble() > TRAINING_FRACTION);

    return strings.stream().filter(predicate).collect(toImmutableList());
  }

  private static ImmutableList<String> loadUnicodeStrings() throws IOException {
    // TODO: Cache the result? This is only a tooling script, not runtime code.
    ImmutableList.Builder<String> builder = ImmutableList.builder();
    builder.addAll(loadStrings(DATA_DIRECTORY + "/my.txt"));
    builder.addAll(loadStrings(DATA_DIRECTORY + "/shn.txt"));
    return builder.build();
  }

  private static ImmutableList<String> loadZawgyiStrings() throws IOException {
    // TODO: Cache the result? This is only a tooling script, not runtime code.
    return loadStrings(DATA_DIRECTORY + "/my-t-d0-zawgyi.txt");
  }

  private static ImmutableList<String> loadStrings(String path) throws IOException {
    try (Stream<String> lines = Files.asCharSource(new File(path), Charsets.UTF_8).lines()) {
      return lines
          .map(line -> line.trim())
          .filter(line -> !line.isEmpty())
          .collect(toImmutableList());
    }
  }
}
