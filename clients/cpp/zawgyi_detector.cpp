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

#include <cmath>
#include <cstddef>
#include <cstdint>
#include <cstring>
#include <limits>
#include <glog/logging.h>
#include <unicode/utf8.h>

#include "public/myanmartools.h"
#include "zawgyi_detector-impl.h"

namespace {
const uint8_t kModelData[] = {
#include "zawgyi_model_data.inc"
};
constexpr size_t kModelSize = sizeof kModelData;
}  // namespace

using namespace google_myanmar_tools;

#if __BYTE_ORDER == __LITTLE_ENDIAN
#    define BSWAP(dest, bits) __builtin_bswap##bits(dest);
#else
#    define BSWAP(dest, bits) dest;
#endif

/**
 * Loads a big-endian type from ptr to dest. Static-asserts that the number of
 * bytes matches the expected size.
 */
#define BIG_ENDIAN_LOAD(ptr, dest, bits) \
  static_assert(sizeof(dest) == bits / 8, \
    "Expected type to be " #bits " bits"); \
  uint##bits##_t u; \
  memcpy(&u, ptr, bits / 8); \
  u = BSWAP(u, bits); \
  memcpy(&dest, &u, bits / 8);

int64_t BigEndian::loadInt64(const void* ptr) {
  int64_t dest;
  BIG_ENDIAN_LOAD(ptr, dest, 64);
  return dest;
}

int32_t BigEndian::loadInt32(const void* ptr) {
  int32_t dest;
  BIG_ENDIAN_LOAD(ptr, dest, 32);
  return dest;
}

int16_t BigEndian::loadInt16(const void* ptr) {
  int16_t dest;
  BIG_ENDIAN_LOAD(ptr, dest, 16);
  return dest;
}

float BigEndian::loadFloat(const void* ptr) {
  float dest;
  BIG_ENDIAN_LOAD(ptr, dest, 32);
  return dest;
}

// Implement Markov Chain processing.
BinaryMarkovClassifier::BinaryMarkovClassifier(const uint8_t* binary_ptr) {
  // Binary formatted file:
  // magic number: int64
  // version: int32
  // int16 size of model N
  // N entries of form:
  //   int16 entry_count
  //   float default_log_value for row unless entry_count is zero
  //   entry count items of:
  //     byte: index
  //     float: log_value

  const uint8_t* data_ptr = binary_ptr;

  model_size_ = 0;
  model_array_ = nullptr;

  int64_t magic_number;
  int32_t version;

  magic_number = BigEndian::loadInt64(data_ptr);
  data_ptr += sizeof(magic_number);

  CHECK_EQ(BINARY_TAG, magic_number);

  version = BigEndian::loadInt32(data_ptr);
  data_ptr += sizeof(version);

  CHECK_EQ(VERSION, version);

  model_size_ = BigEndian::loadInt16(data_ptr);
  data_ptr += sizeof(model_size_);
  VLOG(2) << "BinaryMarkovClassifier size = " << model_size_;

  model_array_ = new float[model_size_ * model_size_];

  float row_default_value;
  // Read each "row".
  for (int row = 0; row < model_size_; ++row) {
    int16_t row_entry_count;
    row_entry_count = BigEndian::loadInt16(data_ptr);
    data_ptr += sizeof(row_entry_count);

    if (row_entry_count != 0) {
      row_default_value = BigEndian::loadFloat(data_ptr);
      data_ptr += sizeof(row_default_value);
    } else {
      row_default_value = 0.0f;
    }

    int index;
    // Set all the entries in the row to default.
    for (int col = 0; col < model_size_; ++col) {
      index = row * model_size_ + col;
      model_array_[index] = row_default_value;
    }

    int16_t column;
    // Set non-default values.
    for (int entry = 0; entry < row_entry_count; ++entry) {
      column = BigEndian::loadInt16(data_ptr);
      data_ptr += sizeof(column);

      index = row * model_size_ + column;

      model_array_[index] = BigEndian::loadFloat(data_ptr);
      data_ptr += sizeof(float);
    }
  }
}

BinaryMarkovClassifier::~BinaryMarkovClassifier() {
  delete[] model_array_;
}

float BinaryMarkovClassifier::GetLogProbabilityDifference(int i1, int i2) {
  return model_array_[i1 * model_size_ + i2];
}

//----------------------------------------------------------------------------

// Initialize ZawgyiUnicode models from the stream
ZawgyiUnicodeMarkovModel::ZawgyiUnicodeMarkovModel(const uint8_t* data_models) {
  int64_t magic_number;
  const uint8_t* input_ptr = data_models;

  magic_number = BigEndian::loadInt64(input_ptr);
  input_ptr += sizeof(magic_number);

  CHECK_EQ(BINARY_TAG, magic_number);

  int32_t version = BigEndian::loadInt32(input_ptr);
  input_ptr += sizeof(version);

  if (version == 1) {
    // No SSV field
    ssv_ = 0;
  } else {
    CHECK_EQ(2, version);
    ssv_ = BigEndian::loadInt32(input_ptr);
    input_ptr += sizeof(ssv_);
    CHECK_GE(ssv_, 0);
    CHECK_LT(ssv_, SSV_COUNT);
  }

  classifier_ = new BinaryMarkovClassifier(input_ptr);
}

ZawgyiUnicodeMarkovModel::~ZawgyiUnicodeMarkovModel() {
  delete classifier_;
}

double
ZawgyiUnicodeMarkovModel::Predict(const char* input_utf8,
                                  int32_t length) const {
  if (length < 0) {
    size_t length_size = strlen(input_utf8);
    if (length_size > __INT32_MAX__) {
      return -std::numeric_limits<double>::infinity();
    }
    length = static_cast<int32_t>(length_size);
  }

  // Start at the base state
  int prevState = 0;

  double totalDelta = 0.0;
  bool seenTransition = false;
  for (int32_t i = 0; i <= length;) {
    int currState;
    if (i >= length) {
      currState = 0;
      i++;
    } else {
      char32_t cp;
      U8_NEXT(input_utf8, i, length, cp);
      currState = GetIndexForCodePoint(cp);
    }
    // Ignore 0-to-0 transitions
    if (prevState != 0 || currState != 0) {
      float delta =
          classifier_->GetLogProbabilityDifference(prevState, currState);
      totalDelta += delta;
      seenTransition = true;
    }
    prevState = currState;
  }

  // Special case: if there is no signal (both log probabilities are zero),
  // return -Infinity, which will get interpreted by users as strong Unicode.
  // This happens when the input string contains no Myanmar-range code points.
  if (!seenTransition) {
    return -std::numeric_limits<double>::infinity();
  }

  // result = Pz/(Pu+Pz)
  //        = exp(logPz)/(exp(logPu)+exp(logPz))
  //        = 1/(1+exp(logPu-logPz))
  return 1.0 / (1.0 + exp(totalDelta));
}

int16_t ZawgyiUnicodeMarkovModel::GetIndexForCodePoint(char32_t cp) const {
  if (STD_CP0 <= cp && cp <= STD_CP1) {
    return cp - STD_CP0 + STD_OFFSET;
  }
  if (AFT_CP0 <= cp && cp <= AFT_CP1) {
    return cp - AFT_CP0 + AFT_OFFSET;
  }
  if (EXA_CP0 <= cp && cp <= EXA_CP1) {
    return cp - EXA_CP0 + EXA_OFFSET;
  }
  if (EXB_CP0 <= cp && cp <= EXB_CP1) {
    return cp - EXB_CP0 + EXB_OFFSET;
  }
  if (ssv_ == SSV_STD_EXA_EXB_SPC && SPC_CP0 <= cp && cp <= SPC_CP1) {
    return cp - SPC_CP0 + SPC_OFFSET;
  }
  return 0;
}


//----------------------------------------------------------------------------

// Reads standard detection modes from embedded data.
ZawgyiDetector::ZawgyiDetector() {
  CHECK(kModelData) << " null model_data loaded";
  CHECK(kModelSize > 0) << " model size = " << kModelSize;
  VLOG(2) << "model_data size = " << kModelSize;
  // TODO: Check kModelSize when reading the model?
  model_ = new ZawgyiUnicodeMarkovModel(kModelData);
}

ZawgyiDetector::~ZawgyiDetector() {
  delete model_;
}

double ZawgyiDetector::GetZawgyiProbability(const char* input_utf8,
                                            int32_t length) const {
  return model_->Predict(input_utf8, length);
}

// C bindings (declared with extern "C").
GMTZawgyiDetector* GMTOpenZawgyiDetector(void) {
  return reinterpret_cast<GMTZawgyiDetector*>(new ZawgyiDetector());
}

void GMTCloseZawgyiDetector(GMTZawgyiDetector* detector) {
  ZawgyiDetector* cppDetector = reinterpret_cast<ZawgyiDetector*>(detector);
  delete cppDetector;
}

double GMTGetZawgyiProbability(GMTZawgyiDetector* detector, const char* input_utf8) {
  return GMTGetZawgyiProbabilityWithLength(detector, input_utf8, -1);
}

double GMTGetZawgyiProbabilityWithLength(GMTZawgyiDetector* detector, const char* input_utf8, int32_t length) {
  ZawgyiDetector* cppDetector = reinterpret_cast<ZawgyiDetector*>(detector);
  return cppDetector->GetZawgyiProbability(input_utf8, length);
}
