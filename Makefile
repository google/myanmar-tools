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


# Path to the maven binary
MVN=mvn

data/target: $(wildcard data/src/**/*)
	$(MVN) -f data/pom.xml -q compile

zawgyiUnicodeModel.dat: data/target
	TMP=`mktemp`; $(MVN) -f data/pom.xml -q -e exec:java -Dexec.args="'$(CORPUS)'" > $$TMP; if [ $$? -ne 0 ]; then cat $$TMP; rm $$TMP; exit 1; else mv $$TMP data/src/main/resources/zawgyiUnicodeModel.dat; exit 0; fi
	$(MVN) -f data/pom.xml -q process-resources

compatibility.tsv: zawgyiUnicodeModel.dat data/target
	TMP=`mktemp`; $(MVN) -f data/pom.xml -q -e exec:java -Dexec.mainClass=com.google.i18n.myanmar.GenerateCompatibilityTSV > $$TMP; if [ $$? -ne 0 ]; then cat $$TMP; rm $$TMP; exit 1; else mv $$TMP data/src/main/resources/compatibility.tsv; exit 0; fi
	$(MVN) -f data/pom.xml -q process-resources

testData.tsv: data/target
	TMP=`mktemp`; $(MVN) -f data/pom.xml -q -e exec:java -Dexec.mainClass=com.google.i18n.myanmar.GenerateTestDataTSV -Dexec.args="'$(CORPUS)'" > $$TMP; if [ $$? -ne 0 ]; then cat $$TMP; rm $$TMP; exit 1; else mv $$TMP data/src/test/resources/testData.tsv; exit 0; fi
	$(MVN) -f data/pom.xml -q process-resources

copy-resources:
	cp data/src/main/resources/zawgyiUnicodeModel.dat clients/java/src/main/resources
	cp data/src/main/resources/zawgyiUnicodeModel.dat clients/cpp/resources
	cp data/src/main/resources/compatibility.tsv clients/java/src/test/resources
	cp data/src/main/resources/compatibility.tsv clients/cpp/resources

train: zawgyiUnicodeModel.dat compatibility.tsv testData.tsv copy-resources
