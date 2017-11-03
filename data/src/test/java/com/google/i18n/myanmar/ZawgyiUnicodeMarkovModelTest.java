package com.google.i18n.myanmar;

import static com.google.common.truth.Truth.assertWithMessage;

import com.google.common.testing.EqualsTester;
import com.google.i18n.myanmar.ZawgyiUnicodeMarkovModelBuilder.Category;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ZawgyiUnicodeMarkovModelTest {

  private static ZawgyiUnicodeMarkovModel basicModel;

  @BeforeClass
  public static void trainModel() {
    basicModel =
        new ZawgyiUnicodeMarkovModelBuilder()
            .trainOnString("\u1000\u1001\u1002", Category.UNICODE)
            .trainOnString("\u1000\u1003\u1004", Category.ZAWGYI)
            .trainOnString("\u1005\u1006", Category.UNICODE)
            .trainOnString("\u1007", Category.ZAWGYI)
            .buildObject();
  }

  @Test
  public void unicodeInputReturnsUnicode() {
    // Strings sharing at least one transition with Unicode should be Unicode:
    assertUnicode("Case 1A", basicModel, "\u1000\u1001\u1002");
    assertUnicode("Case 1B", basicModel, "\u1000\u1001");
    assertUnicode("Case 1C", basicModel, "\u1001\u1002");
    assertUnicode("Case 1D", basicModel, "\u1002");
    assertUnicode("Case 1E", basicModel, "\u1005\u1006");
    assertUnicode("Case 1F", basicModel, "\u1005");
    assertUnicode("Case 1G", basicModel, "\u1006");
    assertUnicode("Case 1H", basicModel, "\u1000\u1006");
  }

  @Test
  public void zawgyiInputReturnsZawgyi() {
    // Strings sharing at least one transition with Zawgyi should be Zawgyi:
    assertZawgyi("Case 2A", basicModel, "\u1000\u1003\u1004");
    assertZawgyi("Case 2B", basicModel, "\u1000\u1003");
    assertZawgyi("Case 2C", basicModel, "\u1003\u1004");
    assertZawgyi("Case 2D", basicModel, "\u1004");
    assertZawgyi("Case 2E", basicModel, "\u1007");
    assertZawgyi("Case 2G", basicModel, "\u1000\u1007");
  }

  @Test
  public void ambiguousReturnsTie() {
    // The transition empty-to-\u1000 is ambiguous:
    assertTie("Case 3A", basicModel, "\u1000");
    // Test a case with equal number of Unicode and Zawgyi transitions:
    assertTie("Case 4A", basicModel, "\u1005\u1007");
  }

  @Test
  public void lackOfDataReturnsTie() {
    // Test cases where we have seen no data:
    assertTie("Case 5A", basicModel, "\u1008\u1009\u100A");
    assertUnicode("Case 5B", basicModel, "hello ASCII");
  }

  @Test
  public void asciiDoesNotAffectClassification() {
    // Test transition from ASCII into Myanmar and back:
    assertUnicode("Case 6A", basicModel, "hello\u1006world");
    assertZawgyi("Case 6B", basicModel, "hello\u1007world");
    // If there is no signal, Unicode should be returned:
    assertUnicode("Case 7A", basicModel, "hello ASCII");
  }

  @Test
  public void testEquals() {
    ZawgyiUnicodeMarkovModel likeBasic =
        new ZawgyiUnicodeMarkovModelBuilder()
            .trainOnString("\u1000\u1001\u1002", Category.UNICODE)
            .trainOnString("\u1000\u1003\u1004", Category.ZAWGYI)
            .trainOnString("\u1005\u1006", Category.UNICODE)
            .trainOnString("\u1007", Category.ZAWGYI)
            .buildObject();
    ZawgyiUnicodeMarkovModel unlikeBasic =
        new ZawgyiUnicodeMarkovModelBuilder()
            .trainOnString("\u1001\u1002\u1003", Category.UNICODE)
            .trainOnString("\u1001\u1004\u1005", Category.ZAWGYI)
            .trainOnString("\u1006\u1007", Category.UNICODE)
            .trainOnString("\u1008", Category.ZAWGYI)
            .buildObject();

    // Test #equals() and #hashCode() using Guava EqualsTester
    new EqualsTester()
        .addEqualityGroup(basicModel, likeBasic)
        .addEqualityGroup(unlikeBasic)
        .testEquals();
  }

  @Test
  public void testBinaryFormat() throws IOException {
    ZawgyiUnicodeMarkovModelBuilder builder =
        new ZawgyiUnicodeMarkovModelBuilder()
            .trainOnString("\u1000\u1001", Category.UNICODE)
            .trainOnString("\u1000\u1003\u1004", Category.ZAWGYI);
    ZawgyiUnicodeMarkovModel object = builder.buildObject();

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    builder.buildToStream(outputStream);
    byte[] bytes = outputStream.toByteArray();
    assertWithMessage("Serialized Markov model should be 526 bytes long")
        .that(bytes.length)
        .isEqualTo(526);
    ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
    ZawgyiUnicodeMarkovModel copy = new ZawgyiUnicodeMarkovModel(inputStream);
    assertWithMessage("Deserialized Markov model should equal the original")
        .that(copy)
        .isEqualTo(object);
  }

  private static void assertUnicode(String message, ZawgyiUnicodeMarkovModel model, String input) {
    double result = model.predict(input);
    assertWithMessage(message).that(result).isLessThan(0.5);
  }

  private static void assertZawgyi(String message, ZawgyiUnicodeMarkovModel model, String input) {
    double result = model.predict(input);
    assertWithMessage(message).that(result).isGreaterThan(0.5);
  }

  private static void assertTie(String message, ZawgyiUnicodeMarkovModel model, String input) {
    double result = model.predict(input);
    assertWithMessage(message).that(result).isWithin(1e-6).of(0.5);
  }
}
