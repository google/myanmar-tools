#ifndef CPP_LIBRARY_H
#define CPP_LIBRARY_H

#include <cstddef>

namespace google_myanmar_tools {

// Forward-declaration
class ZawgyiUnicodeMarkovModel;

class ZawgyiDetector {
 public:
  ZawgyiDetector();
  ~ZawgyiDetector();

  double GetZawgyiProbability(const char* input_utf8, size_t length) const;

 private:
  const ZawgyiUnicodeMarkovModel* model_;
};

}  // namespace google_myanmar_tools

#endif
