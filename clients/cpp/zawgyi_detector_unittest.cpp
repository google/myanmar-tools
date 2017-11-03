#include <cstring>

#include <gtest/gtest.h>
#include "zawgyi_detector.h"

using namespace google_myanmar_tools;

class ZawgyiDetectorTest : public testing::Test {
 protected:
  const ZawgyiDetector detector_;
};

TEST_F (ZawgyiDetectorTest, strongTests) {
  const char* strong_unicode = u8"အပြည်ပြည်ဆိုင်ရာ လူ့အခွင့်အရေး ကြေညာစာတမ်း";
  const char* strong_zawgyi = u8"အျပည္ျပည္ဆိုင္ရာ လူ႔အခြင့္အေရး ေၾကညာစာတမ္း";
  EXPECT_LT(
      detector_.GetZawgyiProbability(strong_unicode, strlen(strong_unicode)),
      0.001);
  EXPECT_GT(
      detector_.GetZawgyiProbability(strong_zawgyi, strlen(strong_zawgyi)),
      0.999);
}
