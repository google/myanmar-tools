import 'dart:math';
import 'dart:typed_data';

import 'binary_markov.dart';



/// A Markov model to predict whether a string is more likely Unicode or Zawgyi.
///
/// Internally, this class maintains two Markov chains, one representing Unicode and the other
/// representing Zawgyi. An input string is evaluated against both chains, and the chain that returns
/// the higher probability becomes the prediction.
///
/// A string is evaluated as a sequence of transitions between states, including transitions to
/// the edges of the string. For example, the string "ABC" contains 4 transitions: NULL to A, A to B,
/// B to C, and C to NULL.
///
/// For the purposes of Unicode/Zawgyi detection, all characters are treated as the NULL state
/// except for characters in the Myanmar script or characters in the Unicode whitespace range U+2000
/// through U+200B.

class ZawGyiUnicodeMarkovModel{


  // Dart Web JavaScript doesn't have the concept of 64-bit integers, so split the tag into two 32-bit ints.
  static const _binaryTagLead = 0x555A4D4F;
  static const _binaryTagTrail =0x44454C20;

  // Standard Myanmar code point range before digits
  static const int _stdCP0 = 0x1000;
  static const int _stdCP1 = 0x103F;

  // Standard Myanmar code point range after digits
  static const int _aftCP0 = 0x104A;
  static const int _aftCP1 = 0x109F;

  // Extended Myanmar code point range A
  static const int _exaCP0 =0xAA60;
  static const int _exaCP1 = 0xAA7F;

  // Extended Myanmar code point range B
  static const int _exbCP0 = 0xA9E0;
  static const int _exbCP1 =0xA9FF;

  // Unicode space characters
  static const int _spcCP0 = 0x2000;
  static const int _spcCP1 = 0x200B;

  // Indices into Markov nodes
  static const int _stdOffset = 1;
  static const int _aftOffset = _stdOffset + _stdCP1 - _stdCP0 + 1;
  static const int _exaOffset = _aftOffset + _aftCP1 - _aftCP0 + 1;
  static const int _exbOffset = _exaOffset + _exaCP1 - _exaCP0 + 1;
  static const int _spcOffset = _exbOffset + _exbCP1 - _exbCP0 + 1;
  static const int _endOffset = _spcOffset + _spcCP1 - _spcCP0 + 1;

  static const int _ssvStdExaExbSpc = 0;



  late BinaryMarkov _classifier;
  late int _ssv;


  /// Creates an instance from a binary data stream.
  ///
  /// @throws Exception If there was a problem reading the data.
  ZawGyiUnicodeMarkovModel(ByteData stream) {

    int offset=0;
    final binaryTagLead = stream.getUint32(offset);
    offset += 4;

    if (binaryTagLead != _binaryTagLead) {
      throw Exception('Unexpected magic number lead; expected 0x${_binaryTagLead.toRadixString(16)} but got 0x${binaryTagLead.toRadixString(16)}');
    }
    final binaryTagTrail = stream.getUint32(offset);
    offset += 4;
    if (binaryTagTrail != _binaryTagTrail) {
      throw Exception('Unexpected magic number trail; expected 0x${_binaryTagTrail.toRadixString(16)} but got  0x${binaryTagTrail.toRadixString(16)}');
    }

    final binaryVersion= stream.getUint32(offset);
    offset+=4;

    if (binaryVersion == 1) {
      // Binary version 1 has no SSV field; SSV_STD_EXA_EXB_SPC is always used
      _ssv = _ssvStdExaExbSpc;
    } else if (binaryVersion == 2) {
      // Binary version 2 adds the SSV field
      _ssv = stream.getUint32(offset);
      offset += 4;
    } else {
    throw Exception('Serial version: expected 1 or 2 but got ${binaryVersion.toString()}');
    }
    _classifier = BinaryMarkov(stream,offset);
  }



  /// Runs the given input string on both internal Markov chains and computes the probability of the
  /// string being unicode or zawgyi.
  ///
  /// @param input The string to evaluate.
  /// @return The probability that the string is Zawgyi given that it is either Unicode or Zawgyi, or
  ///     -Infinity if there are no Myanmar range code points in the string.
  double predict(String input) {

    // Start at the base state
    int prevState = 0;
    double totalDelta = 0;
    bool seenTransition = false;

    for (int offset = 0; offset <= input.length; offset++) {
      int cp;
      int currState;
      if (offset == input.length) {
        cp = 0;
        currState = 0;
      } else {
        cp = input.codeUnitAt(offset);
        currState = getIndexForCodePoint(cp, _ssv);
      }

      // Ignore 0-to-0 transitions
      if (prevState != 0 || currState != 0) {
        double delta = _classifier.getLogProbabilityDifference(
            prevState, currState);
        totalDelta += delta;
        seenTransition = true;
      }
      prevState = currState;
    }

    // Special case: if there is no signal, return -Infinity,
    // which will get interpreted by users as strong Unicode.
    // This happens when the input string contains no Myanmar-range code points.
    if (!seenTransition) {
      return double.negativeInfinity;
    }
    return 1.0 / (1.0 + exp(totalDelta));
  }



  /// The number of states in the Markov chain.
  static int getSize(int ssv) {
    return ssv == _ssvStdExaExbSpc ? _endOffset : _spcOffset;
  }


  /// Returns the index of the state in the Markov chain corresponding to the given code point.
  ///
  /// <p>Code points in the standard Myanmar range, Myanmar Extended A, Myanmar Extended B, and
  /// Unicode Whitespace each have a unique state assigned to them. All other code points are mapped
  /// to state 0.
  ///
  /// <p>Package-private so that the builder can use this method.
  ///
  /// @param cp The code point to convert to a state index.
  /// @param ssv The SSV corresponding to which code points included in the model.
  /// @return The index of the state in the Markov chain. 0 <= state < getSize()
  static int getIndexForCodePoint(int cp, int ssv) {
    if (_stdCP0 <= cp && cp <= _stdCP1) {
      return cp - _stdCP0 + _stdOffset;
    }
    if (_aftCP0 <= cp && cp <= _aftCP1) {
      return cp - _aftCP0 + _aftOffset;
    }
    if (_exaCP0 <= cp && cp <= _exaCP1) {
      return cp - _exaCP0 + _exaOffset;
    }
    if (_exbCP0 <= cp && cp <= _exbCP1) {
      return cp - _exbCP0 + _exbOffset;
    }
    if (ssv == _ssvStdExaExbSpc && _spcCP0 <= cp && cp <= _spcCP1) {
      return cp - _spcCP0 + _spcOffset;
    }
    return 0;
  }


}