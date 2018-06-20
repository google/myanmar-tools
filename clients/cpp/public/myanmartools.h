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

#ifndef CPP_LIBRARY_H
#define CPP_LIBRARY_H

#include <cstdint>

namespace google_myanmar_tools {

// Forward-declaration
class ZawgyiUnicodeMarkovModel;

class ZawgyiDetector {
 public:
  ZawgyiDetector();
  ~ZawgyiDetector();

  double GetZawgyiProbability(const char* input_utf8, int32_t length = -1) const;

 private:
  const ZawgyiUnicodeMarkovModel* model_;
};

}  // namespace google_myanmar_tools

#endif
