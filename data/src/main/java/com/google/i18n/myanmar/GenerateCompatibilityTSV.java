package com.google.i18n.myanmar;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.common.io.Resources;
import com.google.i18n.myanmar.ZawgyiDetector;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Generates compatibility_test_data.tsv. Should be run whenever training data is updated. See
 * README.md for details.
 */
public class GenerateCompatibilityTSV {

  private static InputStream getResourceAsStream(String path) throws IOException {
    // Near-equivalent: GenerateCompatibilityTSV.class.getResourceAsStream(path);
    // Guava is better because it throws an exception if the resource does not exist.
    return Resources.asByteSource(Resources.getResource(path)).openStream();
  }

  public static void main(String[] args) throws IOException {
    BufferedReader tsvReader =
        new BufferedReader(new InputStreamReader(getResourceAsStream("compatibility.tsv"), UTF_8));
    ZawgyiDetector detector = new ZawgyiDetector(getResourceAsStream("zawgyiUnicodeModel.dat"));
    String line;
    while ((line = tsvReader.readLine()) != null) {
      String input = line.split("\t")[1].trim();
      double newScore = detector.getZawgyiProbability(input);
      System.out.print(Double.toString(newScore));
      System.out.print("\t");
      System.out.println(input);
    }
    tsvReader.close();
  }
}
