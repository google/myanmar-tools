import '../phase.dart';



/// A engine for running the phases of transliteration, similar to the ICU4J transliterator.
abstract class Transliterate {

  final String name;  // For identification
  late List<Phase> _transliteratePhases;

  Transliterate( this.name) {
    _transliteratePhases = [];
  }
  Phase addPhase() {
    Phase newPhase =  Phase();
    newPhase.setInfo(' ${_transliteratePhases.length}');
    _transliteratePhases.add(newPhase);
    return newPhase;
  }


  String convert(String inString) {
    return _runAllPhases(inString);
  }

  String _runAllPhases(String inString) {
    String outString = inString;

    for (final phase in _transliteratePhases) {
      outString = phase.runPhase(outString);
    }
    return outString;
  }

}