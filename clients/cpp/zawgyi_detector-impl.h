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

#ifndef MYANMAR_TOOLS_ZAWGYI_DETECTOR_IMPL_H
#define MYANMAR_TOOLS_ZAWGYI_DETECTOR_IMPL_H

#include <cstdint>

namespace google_myanmar_tools {

class BigEndian {
 public:
  static int64_t loadInt64(const void* ptr);
  static int32_t loadInt32(const void* ptr);
  static int16_t loadInt16(const void* ptr);
  static float loadFloat(const void* ptr);
};

class BinaryMarkovClassifier {
 public:
  explicit BinaryMarkovClassifier(const uint8_t* binary_ptr);
  virtual ~BinaryMarkovClassifier();

  float GetLogProbabilityDifference(int i1, int i2);

 private:
  static const int64_t BINARY_TAG = 0x424D41524B4F5620L;
  static const int32_t VERSION = 0;
  // Values for storage of model:
  int16_t model_size_;  // Number of nodes in the Markov chain
  // Array of values in 2-D expanded matrix, model_size_ x model_size_.
  float* model_array_;  // log_probability differences
};


// Stores two Markov chains, one for Unicode and one for Zawgyi Burmese.
class ZawgyiUnicodeMarkovModel {
 public:
  // Two models in binary data
  explicit ZawgyiUnicodeMarkovModel(const uint8_t* data_models);

  virtual ~ZawgyiUnicodeMarkovModel();

  double Predict(const char* input_utf8, int32_t length) const;

 private:
  static const int64_t BINARY_TAG = 0x555A4D4F44454C20L;

  BinaryMarkovClassifier* classifier_;
  int32_t ssv_;

  int16_t GetIndexForCodePoint(char32_t cp) const;

  // Standard Myanmar code point range before digits
  static const char32_t STD_CP0 = 0x1000;  // Unicode code points
  static const char32_t STD_CP1 = 0x103F;

  // Standard Myanmar code point range after digits
  static const char32_t AFT_CP0 = 0x104A;
  static const char32_t AFT_CP1 = 0x109F;

  // Extended Myanmar code point range A
  static const char32_t EXA_CP0 = 0xAA60;
  static const char32_t EXA_CP1 = 0xAA7F;

  // Extended Myanmar code point range B
  static const char32_t EXB_CP0 = 0xA9E0;
  static const char32_t EXB_CP1 = 0xA9FF;

  // Unicode space characters
  static const char32_t SPC_CP0 = 0x2000;
  static const char32_t SPC_CP1 = 0x200B;

  // Indices into Markov nodes
  static const int STD_OFFSET = 1;
  static const int AFT_OFFSET = STD_OFFSET + STD_CP1 - STD_CP0 + 1;
  static const int EXA_OFFSET = AFT_OFFSET + AFT_CP1 - AFT_CP0 + 1;
  static const int EXB_OFFSET = EXA_OFFSET + EXA_CP1 - EXA_CP0 + 1;
  static const int SPC_OFFSET = EXB_OFFSET + EXB_CP1 - EXB_CP0 + 1;
  static const int NUM_STATES = SPC_OFFSET + SPC_CP1 - SPC_CP0 + 1;

  /**
   * SSV: An ID representing which Unicode code points to include in the model:
   *
   * <p>SSV_STD_EXA_EXB_SPC - include Myanmar, Extended A, Extended B, and space-like
   * <p>STD_EXA_EXB - same as above but no space-like code points
   *
   * <p>"SSV" originally stands for State Set Version.
   */
  static const int SSV_STD_EXA_EXB_SPC = 0;
  static const int SSV_STD_EXA_EXB = 1;
  static const int SSV_COUNT = 2;
};

}

#endif //MYANMAR_TOOLS_ZAWGYI_DETECTOR_IMPL_H
