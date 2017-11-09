# Myanmar Tools C++ Documentation

This documentation is for C++ specific usage of *Myanmar Tools*.  For general documentation, see [the top-level README](../../README.md).

To detect Zawgyi, create a singleton instance of ZawgyiDetector, and call `GetZawgyiProbability` with your `char*` UTF-8 string.

```cpp
static const auto* const detector = new google_myanmar_tools::ZawgyiDetector();
double score = detector->GetZawgyiProbability(u8"မ္း");
// score is now 0.999772 (very likely Zawgyi)
```

To convert Zawgyi to Unicode, create a singleton instance of ICU Transliterator with the transform ID "Zawgyi-my", and call `Transliterate` with your `UnicodeString`.

```cpp
static const auto* const converter = // create transliterator instance (see below)
icu::UnicodeString output(u"မ္း");
converter->transliterate(output); // in-place conversion
// output is now u"မ်း"
```

To create a singleton ICU Transliterator instance, you can use the static initializer lambda function pattern in C++11:

```cpp
static const auto* const converter = [] {
    icu::ErrorCode status;
    auto* converter = Transliterator::createInstance(
        "Zawgyi-my", UTRANS_FORWARD  , status);
    CHECK(status.isSuccess()) << ": " << status.errorName();
    return converter;
}();
```

For a complete working example, see [samples/cpp/demo.cpp](../../samples/cpp/demo.cpp).
