using System.IO;
using MyanmarTools.Markov;

namespace MyanmarTools
{
    /// <summary>
    /// Converting Zawgyi to Unicode or Unicode to Zawgiy.
    /// </summary>
    public class ZawgyiDetector
    {
        private ZawgyiUnicodeMarkovModel Model;

        /// <summary>
        /// Initializes a new instance of the ZawgyiDetector.
        /// </summary>
        /// <exception cref="IOException">
        /// Can't find zawgyiUnicodeModel.dat under resources or invalid model file.
        /// </exception>
        public ZawgyiDetector()
        {
            var ModelPath = Path.Combine("resources", "zawgyiUnicodeModel.dat");

            if (!File.Exists(ModelPath))
            {
                throw new IOException("Model file zawgyiUnicodeModel.dat not found");
            }

            try
            {
                using (var Steam = File.Open(ModelPath, FileMode.Open, FileAccess.Read, FileShare.Read))
                {
                    this.Model = new ZawgyiUnicodeMarkovModel(Steam);
                }
            }
            catch (IOException ex)
            {
                throw new IOException("Could not load Markov model from resource file", ex);

            }

        }

        /// <summary>
        /// <para>Performs detection on the given string. Returns the probability that the string is Zawgyi given
        /// that it is either Unicode or Zawgyi. Values approaching 1 are strong Zawgyi; values approaching
        /// 0 are strong Unicode; and values close to 0.5 are toss-ups.</para>
        /// If the string does not contain any Myanmar range code points, double.negativeInfinity is returned.
        /// <para>ပေးထားသော စာသားအား ယူနီကုတ် သို့မဟုတ် ဇော်ဂျီ ဖြစ်ကြောင်းစစ်ဆေးရန်။
        /// ဇော်ဂျီ စာသား ဖြစ်ပါက ၁ နှင့် အနီးပတ်ဝန်းကျင် ဂဏန်း ကိုရရှိပြီး ၊ ယူနီကုတ် စာသား ဖြစ်ပါက ၀ နှင့် အနီးပတ်ဝန်းကျင် ဂဏန်း ကိုရရှိမည် ဖြစ်ပါသည်။
        /// ၀.၅ အနီးပတ်ဝန်းကျင် ဂဏန်း ဖြစ်ပါက ယူနီကုတ်/ဇော်ဂျီ  နှစ်ခုစလုံး ဖြစ်နိုင်ပြီး လိုအပ်သလိုချိန်ဆ သုံးသွားနိုင်ပါသည်။
        /// ပေးထားသော စာသားတွင် ယူနီကုတ် နှင့် ဇော်ဂျီ  နှစ်ခုစလုံး မပါရှိပါက double.negativeInfinity ကိုရရှိမည် ဖြစ်ပါသည်။</para>
        /// </summary>
        /// <param name="Input">
        /// <para>The string on which to run detection.</para>
        /// <para>ယူနီကုတ် သို့မဟုတ် ဇော်ဂျီ ဖြစ်ကြောင်းစစ်ဆေးရန် စာသား</para>
        /// </param>
        /// <returns>
        ///   <para>The probability that the string is Zawgyi (between 0 and 1), or -Infinity if the string contains no Myanmar range code points.
        ///    requested if that many bytes are not available, or it might be zero if the end
        ///    of the stream is reached.</para>
        ///  <para>ဇော်ဂျီ စာသား ဖြစ်နိုင်ခြေ (၀ နှင့် ၁ ကြား) ၊ သို့မဟုတ် ယူနီကုတ် နှင့် ဇော်ဂျီ  နှစ်ခုစလုံး မပါရှိပါက double.negativeInfinity</para>
        /// </returns>
        public double GetZawgyiProbability(string Input)
        {
            return Model.Predict(Input);
        }
    }
}