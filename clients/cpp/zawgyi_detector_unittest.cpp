// Copyright 2017 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
