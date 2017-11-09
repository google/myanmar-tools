# Myanmar Tools Java Documentation

This documentation is for Java specific usage of *Myanmar Tools*.  For general documentation, see [the top-level README](../../README.md).

To detect Zawgyi, create a static final instance of ZawgyiDetector, and call `getZawgyiProbability` with your string.

```java
private static final ZawgyiDetector detector = new ZawgyiDetector();
double score = detector.getZawgyiProbability("မ္း");
// score is now 0.999772 (very likely Zawgyi)
```

To convert Zawgyi to Unicode, create a singleton instance of ICU Transliterator with the transform ID "Zawgyi-my", and call `transliterate` with your string.

```java
private static final Transliterator converter = Transliterator.getInstance("Zawgyi-my");
String output = converter.transliterate("မ္း");
// output is now "မ်း"
```

For a complete working example, see [samples/java/demo.java](../../samples/java/demo.java).
