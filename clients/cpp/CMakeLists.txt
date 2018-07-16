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
include("${CMAKE_BINARY_DIR}/CMakeFiles/HunterGate.cmake")
set(HUNTER_TLS_VERIFY ON)
HunterGate(
        URL "https://github.com/ruslo/hunter/archive/v0.21.19.tar.gz"
        SHA1 "5ead1e069b437930d0de8a21824b20fb52b37b50"
)

project(myanmar-tools)

hunter_add_package(GTest)
find_package(GTest CONFIG REQUIRED)

hunter_add_package(glog)
find_package(glog CONFIG REQUIRED)

message(STATUS "Downloading and extracting ICU4C...")
file(
        DOWNLOAD "http://download.icu-project.org/files/icu4c/61.1/icu4c-61_1-src.tgz" CMakeFiles/icu4c-61_1-src.tgz
        EXPECTED_HASH SHA1=06ca7b1e64c28e07d5633a2e0257380884ea486b
)
execute_process(COMMAND ${CMAKE_COMMAND} -E chdir _3rdParty tar xfz ../CMakeFiles/icu4c-61_1-src.tgz)

add_executable(build_model_inc build_model_inc.cpp resources/zawgyiUnicodeModel.dat)
add_custom_command(
        OUTPUT zawgyi_model_data.inc
        COMMAND build_model_inc ${CMAKE_SOURCE_DIR}/resources/zawgyiUnicodeModel.dat zawgyi_model_data.inc
)

set(SOURCE_FILES zawgyi_detector.cpp public/myanmartools.h zawgyi_detector-impl.h zawgyi_model_data.inc)
include_directories(myanmartools ${CMAKE_BINARY_DIR} ${CMAKE_BINARY_DIR}/_3rdParty/icu/source/common)
add_library(myanmartools SHARED ${SOURCE_FILES})
target_link_libraries(myanmartools glog::glog)

set(TEST_FILES zawgyi_detector_unittest.cpp resources/compatibility.tsv)
add_executable(myanmartools_test ${TEST_FILES})
target_link_libraries(myanmartools_test myanmartools GTest::main)

enable_testing()
add_test(
        NAME ZawgyiDetectorTest
        COMMAND myanmartools_test resources/compatibility.tsv
)

install(TARGETS myanmartools DESTINATION lib)
install(FILES public/myanmartools.h DESTINATION include)
