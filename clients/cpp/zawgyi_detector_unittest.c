// Copyright 2020 Google LLC
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

#include <stdio.h>

#include "public/myanmartools.h"

int RunAllTests(void) {
  const char* strong_unicode = "အပြည်ပြည်ဆိုင်ရာ လူ့အခွင့်အရေး ကြေညာစာတမ်း";
  const char* strong_zawgyi =
    "\xe1\x80\xa1\xe1\x80\xbb\xe1\x80\x95\xe1\x80\x8a\xe1\x80\xb9"
    "\xe1\x80\xbb\xe1\x80\x95\xe1\x80\x8a\xe1\x80\xb9\xe1\x80\x86"
    "\xe1\x80\xad\xe1\x80\xaf\xe1\x80\x84\xe1\x80\xb9\xe1\x80\x9b"
    "\xe1\x80\xac \xe1\x80\x9c\xe1\x80\xb0\xe1\x82\x94\xe1\x80"
    "\xa1\xe1\x80\x81\xe1\x80\xbc\xe1\x80\x84\xe1\x80\xb7\xe1\x80"
    "\xb9\xe1\x80\xa1\xe1\x80\xb1\xe1\x80\x9b\xe1\x80\xb8 \xe1\x80"
    "\xb1\xe1\x81\xbe\xe1\x80\x80\xe1\x80\x8a\xe1\x80\xac\xe1\x80"
    "\x85\xe1\x80\xac\xe1\x80\x90\xe1\x80\x99\xe1\x80\xb9\xe1\x80\xb8";
  GMTZawgyiDetector* detector = GMTOpenZawgyiDetector();
  double zawgyi_probability_unicode = GMTGetZawgyiProbability(detector, strong_unicode);
  int result = 0;
  if (zawgyi_probability_unicode < 0.001) {
    // Success.
  } else {
    fprintf(stderr, "Unicode detection failed (expected 0.001, got %f)\n", zawgyi_probability_unicode);
    result = 1;
  }
  double zawgyi_probability_zawgyi = GMTGetZawgyiProbability(detector, strong_zawgyi);
  if (zawgyi_probability_zawgyi > 0.999) {
    // Success.
  } else {
    fprintf(stderr, "Zawgyi detection failed (expected 0.999, got %f)\n", zawgyi_probability_zawgyi);
    result = 1;
  }
  GMTCloseZawgyiDetector(detector);
  return result;
}

int main(int argc, char** argv) {
  return RunAllTests();
}
