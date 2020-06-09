//
//  myanmartoolsTests.swift
//  myanmartoolsTests
//
//  Created by La Win Ko on 6/6/20.

import XCTest
@testable import myanmartools

class myanmartoolsTests: XCTestCase {

    var detector: ZawgyiDetector = ZawgyiDetector.shared
    
    override class func setUp() {
        super.setUp()
    }
        
    func testIgnoreNonMyanmarCodePoints() throws{
        let allAscii: String = "blah blah blah blah blah"
        let mixedUnicode: String = "<span>blah blah ဒဂုန်ဦးစန်းငွေ </span> blah blah blah blah"
        let mixedZawgyi: String = "blah blah blah blah blah သို႔သြားပါ။ blah blah blah"
        XCTAssertTrue(detector.predict(input: allAscii) == -1 * Double.infinity, "ALL ASCII")
        XCTAssertTrue(detector.predict(input: mixedUnicode) < 0.01, "Mixed Unicode")
        XCTAssertTrue(detector.predict(input: mixedZawgyi) > 0.99, "Mixed Zawgyi")
    }
        
    func testStrongUnicodeReturnsLowScore() throws{
        let strongUnicode: String = "အပြည်ပြည်ဆိုင်ရာ လူ့အခွင့်အရေး ကြေညာစာတမ်း"
        XCTAssertTrue(detector.predict(input: strongUnicode) < 0.01, "Strong Unicode")
    }
    
    func testStrongZawgyiReturnsHighScore() throws{
        let strongZawgyi: String = "အျပည္ျပည္ဆိုင္ရာ လူ႔အခြင့္အေရး ေၾကညာစာတမ္း"
        XCTAssertTrue(detector.predict(input: strongZawgyi) > 0.999, "Strong Zawgyi")
    }

    func testIgnoreNumerals() throws{
        XCTAssertTrue(detector.predict(input: "၉၆.၀ kHz") == -1 * Double.infinity)
        XCTAssertTrue(detector.predict(input: "၂၄၀၉ ဒဂုန်") == detector.predict(input: "ဒဂုန်"))
    }
        
    func testCompatibility() throws{
        do{
            let data = try Data(contentsOf: Bundle(for: myanmartoolsTests.self).url(forResource: "compatibility", withExtension: "tsv")!)
            let string = String(data: data, encoding: .utf8)
            let nextLineSplit = string!.components(separatedBy: "\n")
            for nextLine in nextLineSplit{
                let split = nextLine.components(separatedBy: "\t")
                if split.count ==  2{
                    XCTAssertTrue(detector.predict(input: split[1]) == Double(split[0]))
                }
            }
        }catch (_){
            throw NSError(domain: "File not found", code: NSFileNoSuchFileError, userInfo: nil)
        }
    }
        
    static var allTests = [
        ("testIgnoreNonMyanmarCodePoints", testIgnoreNonMyanmarCodePoints),
        ("testStrongUnicodeReturnsLowScore", testStrongUnicodeReturnsLowScore),
        ("testStrongZawgyiReturnsHighScore", testStrongZawgyiReturnsHighScore),
        ("testIgnoreNumerals", testIgnoreNumerals),
        ("testCompatibility", testCompatibility),
    ]
}
