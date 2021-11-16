using System;
using System.IO;

namespace MyanmarTools.Utils
{
    /// <summary>
    /// To read Big-endian stream. zawgyiUnicodeModel.dat is written in Big Endian.
    /// </summary>
    public class BigEndianBinaryReader : BinaryReader
    {
        public BigEndianBinaryReader(Stream stream) : base(stream) { }

        private byte[] _ReadBytes(int count)
        {

            byte[] b = new byte[count];
            for (var i = count - 1; i >= 0; i--)
            {
                b[i] = this.ReadByte();
            }
            return b;
        }
        public override int ReadInt32()
        {
            return BitConverter.ToInt32(this._ReadBytes(4), 0);
        }

        public override Int16 ReadInt16()
        {
            return BitConverter.ToInt16(this._ReadBytes(2), 0);
        }

        public override Int64 ReadInt64()
        {
            return BitConverter.ToInt64(this._ReadBytes(8), 0);
        }

        public override float ReadSingle()
        {
            return BitConverter.ToSingle(this._ReadBytes(4), 0);
        }

    }
}