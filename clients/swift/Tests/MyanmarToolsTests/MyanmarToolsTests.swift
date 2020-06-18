import XCTest

@testable import MyanmarTools

final class MyanmarToolsTests: XCTestCase {
  func testZawgyiProbabilityWithStrongUnicodeShouldBeLow() {
    let strongUnicode = "အပြည်ပြည်ဆိုင်ရာ လူ့အခွင့်အရေး ကြေညာစာတမ်း"
    XCTAssertLessThan(ZawgyiDetector().getZawgyiProbability(strongUnicode), 0.01)
  }

  func testZawgyiProbabilityWithStrongZawgyiShouldBeHigh() {
    let strongZawgyi = """
      \u{1021}\u{103b}\u{1015}\u{100a}\u{1039}\u{103b}\u{1015}\u{100a}
      \u{1039}\u{1006}\u{102d}\u{102f}\u{1004}\u{1039}\u{101b}\u{102c}
       \u{101c}\u{1030}\u{1094}\u{1021}\u{1001}\u{103c}\u{1004}\u{1037}
      \u{1039}\u{1021}\u{1031}\u{101b}\u{1038} \u{1031}\u{107e}\u{1000}
      \u{100a}\u{102c}\u{1005}\u{102c}\u{1010}\u{1019}\u{1039}\u{1038}
      """
    XCTAssertGreaterThan(ZawgyiDetector().getZawgyiProbability(strongZawgyi), 0.999)
  }

  static var allTests = [
    (
      "testZawgyiProbabilityWithStrongUnicodeShouldBeLow",
      testZawgyiProbabilityWithStrongUnicodeShouldBeLow
    ),
    (
      "testZawgyiProbabilityWithStrongZawgyiShouldBeHigh",
      testZawgyiProbabilityWithStrongZawgyiShouldBeHigh
    ),
  ]
}
