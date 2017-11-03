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
