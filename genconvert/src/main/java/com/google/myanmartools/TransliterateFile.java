/* Copyright 2018 Google LLC
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

import static java.nio.charset.StandardCharsets.UTF_8;

import com.ibm.icu.text.Transliterator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


/** A Java binary to using ICU4J's transliteration on input file, producing output file.
 *
 * <p>This is used to generate tests for compatibility of conversion among languages and platforms,
 *  e.g., Java, JavaScript, C++.
 *
 * <p>Command line: translitRulesFile inputPath outputPath
 */

public final class TransliterateFile {

  // String from a transliteration file.
  static private String getTranslitFile(String filepath) {

    // TODO: If the input is an XML file, then get the CDATA.
    if (filepath.substring(filepath.length()-4).equals(".xml")) {
      System.out.println(" XML file found.");
      System.out.println(" Not yet handling this file type.");
      System.exit(-2);
    }

    try {
      String fileString = new String(Files.readAllBytes(
          Paths.get(filepath)), StandardCharsets.UTF_8);
      return fileString;
    } catch (IOException e1) {
      throw new RuntimeException(
          "Could not load transliteration file " + filepath, e1);
    }
  }

  static long writeLine(BufferedWriter output, String line) {
    try {
      output.write(line);
      output.newLine();
    } catch (IOException e) {
      System.out.println("Error: " + e.getMessage());
      System.exit(-2);
    }
    return 1;
  }

  public static void main(String[] args) throws IOException {
    if (args.length < 3) {
      // Require an input file.
      System.out.println(
          "  Error: transform rules file, input file path and output file path are required.\n");
      System.out.println(
          "   transform_rules_file in_path js_out_path");
      return;
    }

    String fileInputPath = "";
    if (args.length >= 2) {
      fileInputPath = args[1];
    }

    String fileOutputPath = null;
    if (args.length >= 3) {
      fileOutputPath = args[2];
    }

    // Get the Unicode data input:
    String translitText = getTranslitFile(args[0]);

    /* Use the ICU4J transliterator to read the rules, creating a transliterator. */
    String labelForTranslit = "label for transliterator";
    Transliterator transliterator =
        Transliterator.createFromRules(labelForTranslit,
            translitText, Transliterator.FORWARD);

    // Open input and output files.
    try (BufferedReader input =
            args[1].isEmpty()
                ? new BufferedReader(new InputStreamReader(System.in, UTF_8))
                : Files.newBufferedReader(Paths.get(fileInputPath), UTF_8);
        BufferedWriter output =
            args[2].isEmpty()
                ? new BufferedWriter(new OutputStreamWriter(System.out, UTF_8))
                : Files.newBufferedWriter(
                    Paths.get(fileOutputPath),
                    UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE)) {

      long linesConverted = input.lines()
                            .map(transliterator::transliterate)
                            .map(line -> TransliterateFile.writeLine(output, line))
                            .count();
      System.err.println("Number lines converted = " + linesConverted);
    }
  }
}
