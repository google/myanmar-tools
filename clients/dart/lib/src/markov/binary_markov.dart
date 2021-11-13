import 'dart:typed_data';


/// A class that behaves as if it were two Markov chains, called Chain A and Chain B. Whereas a
/// normal Markov chain would expose the log probability of a transition, this class exposes the
/// difference between the log probabilities of the two Markov chains. When training, you can specify
/// which Markov chain a transition should be added to.
///
/// The reasoning behind this class is that it has a smaller data footprint than two separate
/// Markov chain objects.
///
class BinaryMarkov {


  // Dart Web JavaScript doesn't have the concept of 64-bit integers, so split the tag into two 32-bit ints.
  static const _binaryTagLead = 0x424D4152;
  static const _binaryTagTrail =0x4B4F5620;

  // Current serial format version number, used in association with the magic number.
  static const _binaryVersion = 0;

  late List<List<double>> logProbabilityDifferences=[];


  /// Creates an instance from a binary data stream.
  ///
  /// @throws Exception If there was a problem reading the data.
  BinaryMarkov(ByteData stream,int offset){

    final binaryTagLead = stream.getUint32(offset);
    offset += 4;

    if (binaryTagLead != _binaryTagLead) {
      throw Exception('Unexpected magic number lead; expected 0x${_binaryTagLead.toRadixString(16)}  but got  0x${binaryTagLead.toRadixString(16)}');
    }
    final binaryTagTrail = stream.getUint32(offset);
    offset += 4;
    if (binaryTagTrail != _binaryTagTrail) {
      throw Exception('Unexpected magic number trail; expected 0x${_binaryTagTrail.toRadixString(16)} but got0x${binaryTagTrail.toRadixString(16)}');
    }

    final binaryVersion= stream.getUint32(offset);
    offset+=4;

    if (binaryVersion != _binaryVersion) {
      throw Exception('Unexpected serial version number; expected ${_binaryVersion.toString()} but got ${binaryVersion.toString()}');
    }
     int size = stream.getInt16(offset);
     offset += 2;
     var logProbabilityDifferences = List.generate(size, (_) =>List.filled(size, 0.0) );
     for (int i1 = 0; i1 < size; i1++) {
       int entries = stream.getInt16(offset);
       offset += 2;
       double fallback=0;
       if(entries!=0){
         fallback = stream.getFloat32(offset);
         offset += 4;
       }
       int next = -1;
       for (int i2 = 0; i2 < size; i2++) {
         if (entries > 0 && next < i2) {
           next = stream.getInt16(offset);
           offset += 2;
           entries--;
         }
         if (next == i2) {
           logProbabilityDifferences[i1][i2] = stream.getFloat32(offset);
           offset += 4;
         } else {
           logProbabilityDifferences[i1][i2] = fallback;
         }
       }
     }
     this.logProbabilityDifferences = logProbabilityDifferences;
  }


  /// Gets the difference in log probabilities between chain A and chain B. This behaves as if you
  /// had two Markov chains and called <code>
  /// chainA.getLogProbability(i1, i2) - chainB.getLogProbability(i1, i2)</code>.
  ///
  /// @param i1 The index of the source node to transition from.
  /// @param i2 The index of the destination node to transition to.
  /// @return The difference between A and B in log probability of transitioning from i1 to i2.
  double getLogProbabilityDifference(int i1, int i2) {
    return logProbabilityDifferences[i1][i2];
  }

}