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


# All of the following must be installed on your system. Example commands:
# $ sudo gem install bundle rake
# $ sudo apt install cmake make maven nodejs

BUNDLE=bundle
CMAKE=cmake
MAKE=make
MVN=mvn
NPM=npm
RAKE=rake

training/target: $(wildcard training/src/**/*)
	$(MVN) -f training/pom.xml -q compile

zawgyiUnicodeModel.dat: training/target
	TMP=`mktemp`; $(MVN) -f training/pom.xml -q -e exec:java -Dexec.args="'$(CORPUS)'" > $$TMP; if [ $$? -ne 0 ]; then cat $$TMP; rm $$TMP; exit 1; else mv $$TMP training/src/main/resources/zawgyiUnicodeModel.dat; exit 0; fi
	$(MVN) -f training/pom.xml -q process-resources

compatibility.tsv: zawgyiUnicodeModel.dat training/target
	TMP=`mktemp`; $(MVN) -f training/pom.xml -q -e exec:java -Dexec.mainClass=com.google.i18n.myanmar.GenerateCompatibilityTSV > $$TMP; if [ $$? -ne 0 ]; then cat $$TMP; rm $$TMP; exit 1; else mv $$TMP training/src/main/resources/compatibility.tsv; exit 0; fi
	$(MVN) -f training/pom.xml -q process-resources

testData.tsv: training/target
	TMP=`mktemp`; $(MVN) -f training/pom.xml -q -e exec:java -Dexec.mainClass=com.google.i18n.myanmar.GenerateTestDataTSV -Dexec.args="'$(CORPUS)'" > $$TMP; if [ $$? -ne 0 ]; then cat $$TMP; rm $$TMP; exit 1; else mv $$TMP training/src/test/resources/testData.tsv; exit 0; fi
	$(MVN) -f training/pom.xml -q process-resources

copy-resources:
	cp training/src/main/resources/zawgyiUnicodeModel.dat clients/java/src/main/resources
	cp training/src/main/resources/zawgyiUnicodeModel.dat clients/cpp/resources
	cp training/src/main/resources/zawgyiUnicodeModel.dat clients/js/resources
	cp training/src/main/resources/zawgyiUnicodeModel.dat clients/ruby/lib/myanmar-tools/resources
	cp training/src/main/resources/compatibility.tsv clients/java/src/test/resources
	cp training/src/main/resources/compatibility.tsv clients/cpp/resources
	cp training/src/main/resources/compatibility.tsv clients/js/resources
	cp training/src/main/resources/compatibility.tsv clients/ruby/lib/myanmar-tools/resources

train: zawgyiUnicodeModel.dat compatibility.tsv testData.tsv copy-resources

clients: $(wildcard clients/**/*)

client-cpp: $(wildcard clients/cpp/**/*)
	cd clients/cpp && $(CMAKE) CMakeLists.txt
	cd clients/cpp && $(MAKE) all

client-js: $(wildcard clients/js/**/*)
	cd clients/js && $(NPM) install

client-ruby: $(wildcard clients/ruby/**/*)
	cd clients/ruby && $(BUNDLE) install --path vendor/bundle

test: clients client-cpp client-js client-ruby
	cd clients/cpp && $(MAKE) test
	cd clients/java && $(MVN) test
	cd clients/js && $(NPM) test
	cd clients/ruby && $(RAKE) test
