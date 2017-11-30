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

// It would be nice to use preprocessor flags here, but that would require
// building this file before Jasmine can run it.

var ZawgyiDetector, compatibilityTSV;
if (typeof process !== "undefined") {
    // NodeJS
    ZawgyiDetector = require("../build_node/zawgyi_detector").ZawgyiDetector;
    compatibilityTSV = require("fs").readFileSync("resources/compatibility.tsv", "utf-8");
} else {
    // Browser
    ZawgyiDetector = window.google_myanmar_tools.ZawgyiDetector;
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "compatibility.tsv", false);
    xhr.send(null);
    compatibilityTSV = xhr.responseText;
}

function compareFloatingPoint(actual, expected, tolerance, info) {
    const message = "Expected " + actual + " to be within " + tolerance + " relative error of " + expected + ": " + info;
    if (actual === expected) {
        return {
            pass: true,
            message: message
        };
    }
    const relativeError = Math.abs((actual - expected) / expected);
    return {
        pass: relativeError <= tolerance,
        message: message
    };
}

describe("ZawgyiDetector Behavior Tests", function () {
    it("should produce expected results on strings with strong signals", function () {
        const detector = new ZawgyiDetector();
        const strongUnicode = "အပြည်ပြည်ဆိုင်ရာ လူ့အခွင့်အရေး ကြေညာစာတမ်း";
        const strongZawgyi = "အျပည္ျပည္ဆိုင္ရာ လူ႔အခြင့္အေရး ေၾကညာစာတမ္း";
        expect(detector.getZawgyiProbability(strongUnicode)).toBeLessThan(0.001);
        expect(detector.getZawgyiProbability(strongZawgyi)).toBeGreaterThan(0.999);
    });
});

describe("ZawgyiDetector Compatibility Tests", function () {

    beforeEach(function () {
        jasmine.addMatchers({
            toEqualFloatingPoint: function () {
                return { compare: compareFloatingPoint }
            }
        });
    });

    it("should produce the same resuls as in compatibility.csv", function () {
        const detector = new ZawgyiDetector();
        compatibilityTSV.split("\n").forEach(function (line) {
            if (line.length === 0) return;
            const tabIndex = line.indexOf("\t");
            const floatString = line.substr(0, tabIndex);
            const expectedProbability = (floatString === "-Infinity")
                ? Number.NEGATIVE_INFINITY
                : parseFloat(floatString);
            const testInput = line.substr(tabIndex + 1);
            expect(detector.getZawgyiProbability(testInput))
                .toEqualFloatingPoint(expectedProbability, 1e-6, testInput);
        }, this);
    });
});
