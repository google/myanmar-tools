from importlib.resources import open_binary
import numpy as np
import struct

def get_mapping():
    '''
    Generates a mapping of Myanmar Unicode characters to their corresponding
    indices in the parameter array.

    Returns
    -------
    dict
        A mapping from Myanmar Unicode characters to indices.
    '''
    def get_chars(start, end):
        return [chr(char) for char in range(ord(start), ord(end) + 1)]

    chars = (get_chars('\u1000', '\u103F')
            + get_chars('\u104A', '\u109F')
            + get_chars('\uAA60', '\uAA7F')
            + get_chars('\uA9E0', '\uA9FF')
            + get_chars('\u2000', '\u200B'))

    return {char: i + 1 for i, char in enumerate(chars)}

def load_params():
    '''
    Loads parameters as a 2d array, which are log likelihood ratios of
    Unicode to Zawgyi.

    Returns
    -------
    numpy.ndarray
        Parameters as a 2d array.
    '''
    def read_char_array(f, size):
        return struct.unpack(f'{size}s', f.read(size))[0].decode('utf-8')

    def read_float(f):
        return struct.unpack('>f', f.read(4))[0]

    def read_int(f):
        return struct.unpack('>i', f.read(4))[0]

    def read_short(f):
        return struct.unpack('>h', f.read(2))[0]

    with open_binary('myanmartools.resources', 'zawgyiUnicodeModel.dat') as f:
        # check signature
        uzmodel_tag = read_char_array(f, 8)
        if uzmodel_tag != 'UZMODEL ':
            raise IOError('incorrect uzmodel_tag')
        uzmodel_version = read_int(f)
        if uzmodel_version == 1:
            ssv = 0
        elif uzmodel_version == 2:
            ssv = read_int(f)
        else:
            raise IOError('incorrect uzmodel_version')
        bmarkov_tag = read_char_array(f, 8)
        if bmarkov_tag != 'BMARKOV ':
            raise IOError('incorrect bmarkov_tag')
        bmarkov_version = read_int(f)
        if bmarkov_version != 0:
            raise IOError('incorrect bmarkov_version')

        # read params
        size = read_short(f)
        params = np.empty((size, size))
        for row in range(size):
            count = read_short(f)
            if count != 0:
                params[row] = read_float(f)
                for i in range(count):
                    col = read_short(f)
                    params[row, col] = read_float(f)
            else:
                params[row] = 0

        return params
