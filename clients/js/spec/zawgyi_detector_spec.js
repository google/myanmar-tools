const ZawgyiDetector = require("../build/zawgyi_detector").ZawgyiDetector;

describe("ZawgyiDetector Behavior Tests", function () {
    it("strong", function () {
        const detector = new ZawgyiDetector();
        const strong_unicode = "အပြည်ပြည်ဆိုင်ရာ လူ့အခွင့်အရေး ကြေညာစာတမ်း";
        const strong_zawgyi = "အျပည္ျပည္ဆိုင္ရာ လူ႔အခြင့္အေရး ေၾကညာစာတမ္း";
        expect(detector.getZawgyiProbability(strong_unicode)).toBeLessThan(0.001);
        expect(detector.getZawgyiProbability(strong_zawgyi)).toBeGreaterThan(0.999);
    });
});
