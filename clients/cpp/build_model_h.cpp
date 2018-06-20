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

#include <fstream>
#include <iomanip>
#include <iostream>
#include <iterator>

int main(int argc, char* argv[]) {
  if (argc != 3) {
    std::cerr << "Expected 2 arguments, the paths to the input "
      << "(zawgyiUnicodeModel.dat) and output (zawgyi_model_data.h)"
      << std::endl;
    return 1;
  }

  std::fstream fsIn;
  fsIn.open(argv[1],
            std::fstream::in | std::fstream::binary);

  std::fstream fsOut;
  fsOut.open(argv[2], std::fstream::out | std::fstream::trunc);

  // Header
  fsOut << "#include <cstdint>\nnamespace google_myanmar_tools {\nconst uint8_t kModelData[] = {";
  uint64_t modelSize = 0;

  for (std::istreambuf_iterator<char> it(fsIn), end; it != end; it++) {
    char c = *it;
    fsOut << "0x";
    fsOut << std::setw(2) << std::setfill('0') << std::hex
          << (+c & 0xFF);
    fsOut << ",";
    modelSize++;
  }

  // Footer
  fsOut << "};\nconst size_t kModelSize = 0x";
  fsOut << std::hex << modelSize;
  fsOut << ";\n}\n";

  fsOut.close();
  fsIn.close();
}
