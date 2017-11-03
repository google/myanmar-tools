cmake_minimum_required(VERSION 3.0)
include("cmake/HunterGate.cmake")

set(CMAKE_CXX_STANDARD 11)

HunterGate(
        URL "https://github.com/ruslo/hunter/archive/v0.19.87.tar.gz"
        SHA1 "1b0041a06357d2c9f1f1a7e93ff3132c36411719"
)

project(myanmar-tools)

hunter_add_package(GTest)
find_package(GTest CONFIG REQUIRED)

hunter_add_package(glog)
find_package(glog CONFIG REQUIRED)

find_package(ICU)

set(SOURCE_FILES zawgyi_detector.cpp zawgyi_detector.h zawgyi_detector-impl.h zawgyi_model_data.h)
add_library(zawgyi_detector ${SOURCE_FILES})
target_include_directories(zawgyi_detector PRIVATE ${ICU_INCLUDE_DIRS})
target_link_libraries(zawgyi_detector glog::glog)

set(TEST_FILES zawgyi_detector_unittest.cpp)
add_executable(zawgyi_detector_test ${TEST_FILES})
target_link_libraries(zawgyi_detector_test zawgyi_detector GTest::main)

add_executable(build_model_h build_model_h.cpp)

add_custom_command(
        TARGET zawgyi_detector
        PRE_BUILD
        COMMAND build_model_h
)

enable_testing()
add_test(
        NAME ZawgyiDetectorTest
        COMMAND zawgyi_detector_test
)
