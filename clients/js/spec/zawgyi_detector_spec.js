const ZawgyiDetector = require("../build/zawgyi_detector").ZawgyiDetector;

describe("ZawgyiDetector Behavior Tests", function() {
    it("hello world", function() {
        expect(true).toBe(true);
        expect(new ZawgyiDetector().greeting()).toBe("test");
    });
});
