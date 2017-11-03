# Copyright 2017 Google LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


cmake_minimum_required(VERSION 3.0)
set(CMAKE_CXX_STANDARD 11)

file(
        DOWNLOAD "https://raw.githubusercontent.com/hunter-packages/gate/v0.8.1/cmake/HunterGate.cmake" CMakeFiles/HunterGate.cmake
        EXPECTED_HASH SHA1=b07c64067aebe94ba07d375c74b1bd1d7a696c67
)
include("CMakeFiles/HunterGate.cmake")
HunterGate(
        URL "https://github.com/ruslo/hunter/archive/v0.19.87.tar.gz"
        SHA1 "1b0041a06357d2c9f1f1a7e93ff3132c36411719"
)

project(myanmar-tools)

hunter_add_package(GTest)
find_package(GTest CONFIG REQUIRED)

hunter_add_package(glog)
find_package(glog CONFIG REQUIRED)

set(SOURCE_FILES zawgyi_detector.cpp zawgyi_detector.h zawgyi_detector-impl.h zawgyi_model_data.h)
add_library(zawgyi_detector ${SOURCE_FILES})
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
