// After installing myanmar-tools, compile this file with -lmyanmartoolsd

#include <iostream>
#include <myanmartools.h>

int main() {
    const char* input1 = u8"အပြည်ပြည်ဆိုင်ရာ လူ့အခွင့်အရေး ကြေညာစာတမ်း";
    const char* input2 = u8"အျပည္ျပည္ဆိုင္ရာ လူ႔အခြင့္အေရး ေၾကညာစာတမ္း";

    static const auto* detector = new google_myanmar_tools::ZawgyiDetector();
    double score1 = detector->GetZawgyiProbability(input1);
    double score2 = detector->GetZawgyiProbability(input2);

    std::cout << "unicode: " << score1 << " vs zawgyi " << score2 << std::endl;
}
