# Myanmar Tools Java Documentation

This documentation is for Java specific usage of *Myanmar Tools*.  For general documentation, see [the top-level README](../../README.md).

Add this project as a dependency in your build.gradle file in Android Studio:

```
allprojects {
    repositories {
        mavenCentral()
    }
}

dependencies {
    compile 'com.google.myanmartools:myanmar-tools:1.0.1'
}
```

See the [Android Studio documentation](https://developer.android.com/studio/build/dependencies.html) for more information on adding dependencies.

If you are using a pom.xml:

```xml
<dependency>
  <groupId>com.google.myanmartools</groupId>
  <artifactId>myanmar-tools</artifactId>
  <version>1.0.1</version>
</dependency>
```

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
