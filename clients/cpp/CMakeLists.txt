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
        DOWNLOAD "https://raw.githubusercontent.com/hunter-packages/gate/v0.8.1/cmake/HunterGate.cmake" ${CMAKE_BINARY_DIR}/CMakeFiles/HunterGate.cmake
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
        DOWNLOAD "https://github.com/unicode-org/icu/releases/download/release-66-1/icu4c-66_1-src.tgz" CMakeFiles/icu4c-66_1-src.tgz
        EXPECTED_HASH SHA1=68e87ea2044e92a5d86be6072b0eb3557f252d9f
)
execute_process(COMMAND ${CMAKE_COMMAND} -E chdir ${CMAKE_BINARY_DIR}/_3rdParty tar xfz ../CMakeFiles/icu4c-66_1-src.tgz)

add_executable(build_model_inc build_model_inc.cpp resources/zawgyiUnicodeModel.dat)
target_link_libraries(build_model_inc -lpthread)
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

set(C_TEST_FILES zawgyi_detector_unittest.c)
add_executable(myanmartools_c_test ${C_TEST_FILES})
target_link_libraries(myanmartools_c_test myanmartools)

# Fuzz target; see http://llvm.org/docs/LibFuzzer.html
# Requires CXX=clang++
set(FUZZ_FILES zawgyi_detector_fuzz_target.cpp)
add_executable(myanmartools_fuzz EXCLUDE_FROM_ALL ${FUZZ_FILES})
target_compile_options(myanmartools_fuzz PUBLIC -fsanitize=fuzzer,address,undefined)
target_link_libraries(myanmartools_fuzz myanmartools -fsanitize=fuzzer,address,undefined)

enable_testing()
add_test(
        NAME ZawgyiDetectorTest
        WORKING_DIRECTORY ${CMAKE_SOURCE_DIR}
        COMMAND myanmartools_test resources/compatibility.tsv
)
add_test(
        NAME ZawgyiDetectorCTest
        COMMAND myanmartools_c_test
)

install(TARGETS myanmartools DESTINATION lib)
install(FILES public/myanmartools.h DESTINATION include)
