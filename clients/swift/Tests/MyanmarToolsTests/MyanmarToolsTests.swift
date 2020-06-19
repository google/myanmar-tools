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

  func testCompatibility() throws {
      let detector = ZawgyiDetector()
      
      // Until Swift 5.3 is released with support for resources in Package.swift,
      // guess the path to `compatibility.tsv` by walking the tree from this
      // source file.

      // clients/swift/Tests/MyanmarToolsTests/MyammarToolsTests.swift
      let testSourcePath = URL(fileURLWithPath: #file)
      // clients/swift
      let clientsSwiftPath = testSourcePath.deletingLastPathComponent().deletingLastPathComponent().deletingLastPathComponent()
      // clients/swift/resources/compatibility.tsv
      let compatibilityPath = clientsSwiftPath.appendingPathComponent("resources", isDirectory: true).appendingPathComponent("compatibility.tsv", isDirectory: false)

      let compatibilityData = try XCTUnwrap(try Data(contentsOf: compatibilityPath))
      let string = try XCTUnwrap(String(data: compatibilityData, encoding: .utf8))
      let epsilon = 1e-6

      for line in string.components(separatedBy: "\n") {
          let elements = line.components(separatedBy: "\t")
          if elements.count != 2 {
              continue
          }
          let expectedProbability = try XCTUnwrap(Double(elements[0]))
          let input = elements[1]
          print("Checking getZawgyiProbability(\(input)) = \(expectedProbability)")
          // Work around for https://bugs.swift.org/browse/SR-13047
          // (XCTAssertEqual with `accuracy` fails to compare infinity).
          if (expectedProbability.isInfinite) {
              XCTAssertEqual(detector.getZawgyiProbability(input), expectedProbability)
          } else {
              XCTAssertEqual(detector.getZawgyiProbability(input), expectedProbability, accuracy: epsilon)
          }
      }
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
    (
      "testCompatibility",
      testCompatibility
    ),
  ]
}
