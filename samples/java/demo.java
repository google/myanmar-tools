/* Copyright 2017 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/////////////////////////////////////////////////////////////////////////////////
// IMPORTANT: Make sure that Myanmar Tools and ICU are both in your classpath! //
// Build and run this file like this:                                          //
// $ javac -cp <fill-me-in> demo.java && java -cp <fill-me-in>:. demo          //
/////////////////////////////////////////////////////////////////////////////////

import com.google.myanmartools.ZawgyiDetector;
import com.google.myanmartools.TransliterateZ2U;

public class demo {
    private static final ZawgyiDetector detector = new ZawgyiDetector();
    private static final TransliterateZ2U converter = new TransliterateZ2U("Zawgyi to Unicode");

    public static void main(String[] args) {
        // Unicode string:
        String input1 = "အပြည်ပြည်ဆိုင်ရာ လူ့အခွင့်အရေး ကြေညာစာတမ်း";
        // Zawgyi string:
        String input2 = "အျပည္ျပည္ဆိုင္ရာ လူ႔အခြင့္အေရး ေၾကညာစာတမ္း";

        // Detect that the second string is Zawgyi:
        double score1 = detector.getZawgyiProbability(input1);
        double score2 = detector.getZawgyiProbability(input2);
        assert score1 < 0.001;
        assert score2 > 0.999;
        System.out.format("Unicode Score: %.6f%n", score1);
        System.out.format("Zawgyi Score: %.6f%n", score2);

        // Convert the second string to Unicode:
        String input2converted = converter.convert(input2);
        assert input1.equals(input2converted);
        System.out.format("Converted Text: %s%n", input2converted);
    }
}
