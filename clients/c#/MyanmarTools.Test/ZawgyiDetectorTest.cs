using Xunit;
using System;
using System.IO;
using System.Text;

namespace MyanmarTools.Test
{

    public class ZawgyiDetectorTest
    {
        private ZawgyiDetector detector;
        public ZawgyiDetectorTest()
        {
            detector = new ZawgyiDetector();
        }

        [Fact(DisplayName = "Sanity")]
        public void Sanity()
        {
            double actual = detector.GetZawgyiProbability("hello world");
            Assert.Equal(actual, double.NegativeInfinity);
        }

        [Fact(DisplayName = "testIgnoreNonMyanmarCodePoints")]
        public void testIgnoreNonMyanmarCodePoints()
        {
            var allASCII = "blah blah blah blah blah";
            var mixedUnicode = "<span>blah blah ဒဂုန်ဦးစန်းငွေ </span> blah blah blah blah";
            string mixedZawgyi = "blah blah blah blah blah သို႔သြားပါ။ blah blah blah";

            //All ASCII
            var Expected = double.NegativeInfinity;
            var Actual = detector.GetZawgyiProbability(allASCII);
            Assert.True(Actual == Expected, $"Error:All ASCII; Expected:{Expected} == Actual:{Actual}");

            //Mixed Unicode
            Expected = 0.01;
            Actual = detector.GetZawgyiProbability(mixedUnicode);
            Assert.True(Actual < Expected, $"Error:Mixed Unicode; Expected:{Expected} < Actual:{Actual}");

            //Mixed Zawgyi
            Expected = 0.99;
            Actual = detector.GetZawgyiProbability(mixedZawgyi);
            Assert.True(Actual > 0.99, $"Error:Mixed Zawgyi; Expected:{Expected} > Actual:{Actual}");

        }
        [Fact(DisplayName = "strongUnicodeReturnsLowScore")]
        public void strongUnicodeReturnsLowScore()
        {
            var strongUnicode = "အပြည်ပြည်ဆိုင်ရာ လူ့အခွင့်အရေး ကြေညာစာတမ်း";

            var Expected = 0.001;
            var Actual = detector.GetZawgyiProbability(strongUnicode);
            Assert.True(Actual < Expected, $"Error:Strong Unicode; Expected:{Expected} < Actual:{Actual}");

        }

        [Fact(DisplayName = "strongZawgyiReturnsHighScore")]
        public void strongZawgyiReturnsHighScore()
        {
            var strongZawgyi = "အျပည္ျပည္ဆိုင္ရာ လူ႔အခြင့္အေရး ေၾကညာစာတမ္း";
            var Expected = 0.999;
            var Actual = detector.GetZawgyiProbability(strongZawgyi);
            Assert.True(Actual > Expected, $"Error:Strong Zawgyi; Expected:{Expected} > Actual:{Actual}");

        }

        [Theory(DisplayName = "testDifficult")]
        // STRINGS IDENTICAL IN UNICODE/ZAWGYI
        // TODO: These should return scores in the middle range.
        [InlineData("အသံကို အစားထိုးလိုပါသလား။", 0.995)] // Truth: Identical in U/Z
        [InlineData("နမူနာ", 0.26)] // Truth: Identical in U/Z
        [InlineData(" ဦး", 0.35)] // Truth: Identical in U/Z
        // UNICODE STRINGS WITH HIGH ZAWGYI SCORES
        // TODO: Fix detection so that these get low Zawgyi scores.
        [InlineData("အစားထိုး အထူးအက္ခရာ", 0.995)] // Truth: Unicode
        [InlineData("ယခု မိုးရွာနေပါသလား။", 0.995)] // Truth: Unicode
        [InlineData("အခြား", 0.74)] // Truth: Unicode
        // DIFFICULT STRINGS THAT DETECT CORRECTLY
        // Changes to the detector should not significantly change these scores.
        [InlineData("ကာမစာအုပ္မ်ား(ေစာက္ပတ္စာအုပ္မ်ား)", 1.0)] // Truth: Zawgyi
        [InlineData("ညႇပ္စရာမလို", 0.82)] // Truth: Zawgyi
        public void testDifficult(string Input, double Expected)
        {
            var Actual = detector.GetZawgyiProbability(Input);
            var ExpectedDiff = 0.005;
            var ActualDiff = Math.Abs(Actual - Expected);
            Assert.True(ActualDiff <= ExpectedDiff, $"Error:{Input}; Expected:{ExpectedDiff} Different Actual:{ActualDiff}");
        }

        [Fact(DisplayName = "testIgnoreNumerals")]
        public void testIgnoreNumerals()
        {
            // Digits/numerals are ignored and treated like non-Myanmar code points.
            Assert.Equal(detector.GetZawgyiProbability("၉၆.၀ kHz"), double.NegativeInfinity);
            Assert.Equal(detector.GetZawgyiProbability("၂၄၀၉ ဒဂုန်"), detector.GetZawgyiProbability("ဒဂုန်"));

        }

        [Fact(DisplayName = "testCompatibility")]
        public void testCompatibility()
        {
            var line = "";
            using (var steam = File.Open(Path.Combine("resources", "compatibility.tsv"), FileMode.Open, FileAccess.Read, FileShare.Read))
            {
                using (var streamReader = new StreamReader(steam, Encoding.UTF8, true, 1024))
                {
                    while (streamReader.Peek() != -1)
                    {
                        line = streamReader.ReadLine();
                        int tabIdx = line.IndexOf('\t');
                        var strValue=line.Substring(0, tabIdx);
                        double Expected = double.NegativeInfinity;
                        if (strValue != "-Infinity")
                        {
                           Expected= double.Parse(strValue);
                        }
                        var input = line.Substring(tabIdx + 1);
                        double Actual = detector.GetZawgyiProbability(input);

                        if (Expected == double.NegativeInfinity) { 
                          Assert.True(Actual==Expected,$"Error:{input}; Expected:{strValue} Different Actual:{Actual}");
                        }else { 
                          var ActualDiff = Math.Abs(Actual - Expected);
                           Assert.True(ActualDiff<=1e-6,$"Error:{input}; Expected:{strValue} Different Actual:{Actual}");
                        }
                    }

                }
            }

        }



    }


}


