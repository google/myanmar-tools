"""Helper functions for reading parameters of the model file."""

from array import array
from itertools import chain, repeat
import struct
from typing import BinaryIO, cast, Final, Iterator, Tuple

# Myanmar Unicode characters before digits
STD: Final = range(0x1000, 0x103F + 1)
# Myanmar Unicode characters after digits
AFT: Final = range(0x104A, 0x109F + 1)
# Myanmar Extended-A Unicode characters
EXA: Final = range(0xAA60, 0xAA7F + 1)
# Myanmar Extended-B Unicode characters
EXB: Final = range(0xA9E0, 0xA9FF + 1)
# Unicode space characters
SPC: Final = range(0x2000, 0x200B + 1)


def check_signature(stream: BinaryIO) -> str:
    """
    Check signature of the model file and return characters used by the model.

    The characters returned are sorted in lexicographical order.
    """
    uzmodel_tag = stream.read(8)
    if uzmodel_tag != b'UZMODEL ':
        raise IOError('invalid uzmodel_tag')
    uzmodel_version = read_int(stream)

    if uzmodel_version == 1:
        ssv = 0
    elif uzmodel_version == 2:
        ssv = read_int(stream)
    else:
        raise IOError('invalid uzmodel_version')

    if ssv == 0:
        chars = ''.join(map(chr, chain(STD, AFT, EXA, EXB, SPC)))
    elif ssv == 1:
        chars = ''.join(map(chr, chain(STD, AFT, EXA, EXB)))
    else:
        raise ValueError('invalid ssv')

    bmarkov_tag = stream.read(8)
    if bmarkov_tag != b'BMARKOV ':
        raise IOError('invalid bmarkov_tag')
    bmarkov_version = read_int(stream)
    if bmarkov_version != 0:
        raise IOError('invalid bmarkov_version')

    return chars


def read_params(stream: BinaryIO) -> 'array[float]':
    """Read parameters in the model file."""
    size = read_short(stream)
    params = array('f', repeat(0, size * size))
    for i in range(size):
        count = read_short(stream)
        if count != 0:
            offset = i * size
            # set default value
            value = read_float(stream)
            for index in range(size):
                params[offset + index] = value
            # set special values
            for index, value in read_pairs(stream, count):
                params[offset + index] = value
    return params


def read_short(stream: BinaryIO) -> int:
    """Read a short integer value in big-endian order."""
    return cast(int, struct.unpack('>h', stream.read(2))[0])


def read_int(stream: BinaryIO) -> int:
    """Read an integer value in big-endian order."""
    return cast(int, struct.unpack('>i', stream.read(4))[0])


def read_float(stream: BinaryIO) -> float:
    """Read a float value in big-endian order."""
    return cast(float, struct.unpack('>f', stream.read(4))[0])


def read_pairs(stream: BinaryIO, n: int) -> Iterator[Tuple[int, float]]:
    """Read n int-float value pairs in big-endian order."""
    return cast(
        Iterator[Tuple[int, float]],
        struct.iter_unpack('>hf', stream.read(6 * n))
    )
