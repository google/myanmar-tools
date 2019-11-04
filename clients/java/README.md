# Myanmar Tools Java Documentation

This documentation is for Java specific usage of *Myanmar Tools*.  For general documentation, see [the top-level README](../../README.md).

The project is available on [Maven Central](https://mvnrepository.com/artifact/com.google.myanmartools/myanmar-tools).

Add this project as a dependency in your build.gradle file in Android Studio:

```
allprojects {
    repositories {
        mavenCentral()
    }
}

dependencies {
    compile 'com.google.myanmartools:myanmar-tools:1.1.3'
}
```

See the [Android Studio documentation](https://developer.android.com/studio/build/dependencies.html) for more information on adding dependencies.

If you are using a pom.xml:

```xml
<dependency>
  <groupId>com.google.myanmartools</groupId>
  <artifactId>myanmar-tools</artifactId>
  <version>1.1.3</version>
</dependency>
```

To detect Zawgyi, create a static final instance of ZawgyiDetector, and call `getZawgyiProbability` with your string.

```java
private static final ZawgyiDetector detector = new ZawgyiDetector();
double score = detector.getZawgyiProbability("မ္း");
// score is now 0.999772 (very likely Zawgyi)
```

To convert between Zawgyi and Unicode, use the classes TransliterateZ2U and TransliterateU2Z as shown below.

```java
private static final TransliterateZ2U z2U = new TransliterateZ2U("Zawgyi to Unicode");
String output = z2U.convert("မ္း");
// output is now "မ်း"
```

For a complete working example, see [samples/java/demo.java](../../samples/java/demo.java).
