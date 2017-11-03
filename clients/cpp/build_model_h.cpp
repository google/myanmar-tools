#include <fstream>
#include <iomanip>
#include <iostream>
#include <iterator>

int main() {
  std::fstream fsIn;
  fsIn.open("resources/zawgyiUnicodeModel.dat",
            std::fstream::in | std::fstream::binary);

  std::fstream fsOut;
  fsOut.open("zawgyi_model_data.h", std::fstream::out | std::fstream::trunc);

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
