using MyanmarTools.Transliterate;

namespace MyanmarTools
{
    public class ZawgyiConverter
    {

        private TransliterateZ2U TransliterateZ2U;
        private TransliterateU2Z TransliterateU2Z;
        private TransliterateZNorm TransliterateZNorm;

        /// <summary>
        ///  <para>Convert Zawgyi to Unicode </para>
        ///  <para>ဇော်ဂျီ မှ ယူနီကုတ် သို့ပြောင်းရန်။ </para>
        /// </summary>
        /// <param name="InString"> Zawgyi string. ဇော်ဂျီ အချက်အလက်စာ။</param>
        /// <returns>Unicode string. ယူနီကုတ် သို့‌ပြောင်းထားသော အချက်အလက်စာ။</returns>
        public string ZawgyiToUnicode(string InString)
        {
            TransliterateZ2U ??= new TransliterateZ2U("Z2U");
            return TransliterateZ2U.Convert(InString);
        }

        /// <summary>
        ///  <para>Convert Unicode To Zawgyi</para>
        ///  <para>ယူနီကုတ် မှ ဇော်ဂျီ သို့ပြောင်းရန်။</para>
        /// </summary>
        /// <param name="InString">Unicode string. ယူနီကုတ် အချက်အလက်စာ။</param>
        /// <returns>Zawgyi string.ဇော်ဂျီ သို့‌ပြောင်းထားသော အချက်အလက်စာ။ </returns>
        public string UnicodeToZawgyi(string InString)
        {
            TransliterateU2Z ??= new TransliterateU2Z("U2Z");
            return TransliterateU2Z!.Convert(InString);
        }

        /// <summary>
        /// <para>Convert normalize Zawgyi</para>
        /// <para>ရှုပ်ထွေးသော ဇော်ဂျီ အချက်အလက်စာကြမ်းများ ကို ရှင်းလင်းခြင်း။</para>
        /// </summary>
        /// <param name="InString">Zawgyi string.  ရှုပ်ထွေးသော ဇော်ဂျီ အချက်အလက်စာကြမ်း။</param>
        /// <returns>Normalized Zawgyi string.  ရှင်းလင်း ထားသော ဇော်ဂျီ အချက်အလက်စာ။</returns>
        public string NormalizeZawgyi(string InString)
        {
            TransliterateZNorm ??= new TransliterateZNorm("ZNorm");
            return TransliterateZNorm!.Convert(InString);
        }
    }
}