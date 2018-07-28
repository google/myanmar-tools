/* Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.myanmartools;

import com.ibm.icu.text.Transliterator;

import java.io.BufferedWriter;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/** A Java binary to generate Javascript and Java transliterators from an ICU4J transliteration file. The
 * output is a) Javascript code, and b) Java code that take text and perform the transliterations specified.
 *
 * <p>Note: this does not implement many features of ICU Transliteration, but does support
 * conversions such as Zawgyi to Unicode for Burmese language.
 */

public final class CompileTranslit {

  // String from a transliteration file.
  static private String getTranslitFile(String filepath) {

    // TODO: If the input is an XML file, then get the CDATA.
    if (filepath.substring(filepath.length()-4).equals(".xml")) {
      System.out.println(" XML file found.");
      System.out.println(" Not yet handling this file type.");
      System.exit(-2);
    }

    try {
      String fileString = new String(Files.readAllBytes(
          Paths.get(filepath)), StandardCharsets.UTF_8);
      return fileString;
    } catch (IOException e1) {
      throw new RuntimeException(
          "Could not load transliteration file " + filepath, e1);
    }
  }

  static private class TranslitRule {
    public String rulePattern;
    public String ruleResult;
    public int revisitPosition;  // -1 if not used. Otherwise, position in output rule text to put new start
    public String beforeContext; // default "";
    public String afterContext;  // default "";
    public int numGroups;            // Number of groups in the pattern part.
    public boolean atStart;

    public void TranslitRule() {
    }

    public void AddRule(String rule) {
      String[] ruleParts = rule.split(" > ", -1);

      String [] patternParts = ruleParts[0].split("\n", -1);

      // TODO: Look for literal {, and } within single quote.

      if (ruleParts.length > 1) {
        if (patternParts.length <= 1) {
          rulePattern = ruleParts[0];
        } else {
          rulePattern = patternParts[1];
        }

        // Change ":Wspace:" to the Unicode white spaces.
        rulePattern = rulePattern.replace(":WSpace:",
            " \u00a0\u1680\u2000-\u200d\u2060\u202f\u205f\u3000\ufeff");

        // Avoid escaping the ASCII space character.
        rulePattern = rulePattern.replace("\\ ", " ");

        atStart = false;
        // Special case for matching at first position
        if (rulePattern.substring(0, 1).equals("^")) {
          // Note the special case and remove the "^" marker
          atStart = true;
          rulePattern = rulePattern.substring(1);
        }
        // Clean up the spaces in the output.
        ruleResult = ruleParts[1].replace(" ", "");

        // Handle revisit location in the output.
        revisitPosition = -1;
        int vBarPos = ruleResult.indexOf('|');
        if (vBarPos >= 0) {
          // System.out.println("@@@@@ REVISIT:" + ruleResult);

          revisitPosition = vBarPos;
          if (vBarPos == 0) {
            ruleResult = ruleResult.substring(revisitPosition + 1);
          } else {
            // We don't know how to handle this yet.
            // Throw exception to warn the user.
            throw new RuntimeException(
                "Revisit position > 1. This is not implemented yet.\n" +
                "  Found in this rule '" + rule + "'");

            /* ruleResult = ruleResult.substring(0, revisitPosition) +
                         ruleResult.substring(revisitPosition + 1);
            */

          }

          // System.out.println("    position " + revisitPosition);
          // System.out.println("         new output:" + ruleResult);
        }

        // Remove unescaped } (after_context).
        afterContext = "";
        int rbracePos = rulePattern.indexOf('}');
        if (rbracePos >= 0) {
          // Count unescaped "(" and ")"
          // Here's where we remove both the left and right context.
          afterContext = rulePattern.substring(rbracePos + 1);
          rulePattern = rulePattern.substring(0, rbracePos);
        }

        // Remove unescaped { (before_context).
        // TODO: handle literal { at positions other than 0.
        int lbracePos = rulePattern.indexOf('{');
        beforeContext = "";
        if (lbracePos >= 0) {
          beforeContext = rulePattern.substring(0, lbracePos);

          if (lbracePos == 0) {
            rulePattern = rulePattern.substring(lbracePos + 1);
            System.out.println("!!! Removing left brace in " + rulePattern);
          } else {
            System.out.println("!!! Throwing because we don't handle before > ");
            throw new RuntimeException(
                "Cannot handle before_context > 0 in : " + rule);
          }
        }

        // Finally, get the number of groups in the pattern part, saving until output.
        Pattern r = Pattern.compile(rulePattern);
        Matcher m = r.matcher(rulePattern);
        numGroups = m.groupCount();
      }
    }
  }

  static private class TranslitPhase {
    private final List<TranslitRule> phaseRules;

    public TranslitPhase(String phaseStrings) {
      phaseRules = new ArrayList<>();

      String[] rules = phaseStrings.split(";", -1);

      for (String rule : rules) {
        TranslitRule newRule = new TranslitRule();
        newRule.AddRule(rule);
        phaseRules.add(newRule);
      }
    }
  }

  static private class AllPhases {
    private final ArrayList<TranslitPhase> phases;

    public AllPhases(String allPhases) {
      phases = new ArrayList<>();

      // Handle each phase string
      //   a. split into rules with " > "
      //   b. remove extra spaces in output.
      String[] inPhases = allPhases.split("::Null;", -1);

      for (String phase : inPhases) {
        // Add phase object.
        TranslitPhase newPhase = new TranslitPhase(phase);
        phases.add(newPhase);
      }

    }

    // The functions reused in the JS implementation.
    public ArrayList<String> javascriptBasicStuff(String inputFilename, String nameSuffix) {
      ArrayList<String> jsOutput = new ArrayList<>();

      jsOutput.add("// Input path: " + inputFilename + "\n");
      jsOutput.add("function getAllRules" + nameSuffix + "() {\n");
      return jsOutput;
    }

    public ArrayList<String> generateJS(String inputFilename, String nameSuffix) {
      //For each phase in all phases.

      ArrayList<String> jsOutput = javascriptBasicStuff(inputFilename, nameSuffix);
      // Not needed: jsOutput.add("\n");

      int phaseNum = 0;
      for (TranslitPhase phase: phases) {

        // Not needed: jsOutput.add("  // Rules for phase " + phaseNum + "\n");
        jsOutput.add("  var rules" + phaseNum + " = [\n");

        for (TranslitRule rule: phase.phaseRules) {
          if (rule.rulePattern != null && !"\n".equals(rule.rulePattern.substring(0,1))) {
            jsOutput.add("    {\n");

            if (rule.afterContext.length() == 0) {
              // REMOVE jsOutput.add("      p: RegExp('^' + '" + rule.rulePattern + "', 'u'),\n");
              jsOutput.add("      p: RegExp('^' + '" + rule.rulePattern + "'),\n");
              jsOutput.add("      s: '" + rule.ruleResult + "',\n");
            } else {
              // Add after context as another unnamed group.
              jsOutput.add("      p: RegExp('^' + '" + rule.rulePattern +
                  "(" + rule.afterContext + ")" + "'),\n");
              // REMOVE "(" + rule.afterContext + ")" + "', 'u'),\n");
              jsOutput.add("      s: " +
                  "'" + rule.ruleResult + "$" + (rule.numGroups+1) + "',\n");
            }

            // Flags for special cases.
            if (rule.atStart) {
              jsOutput.add("      matchOnStart: 'true',\n");
            }
            if (rule.beforeContext.length() > 0) {
              jsOutput.add("      before: '" + rule.beforeContext + "',\n");
            }

            if (rule.afterContext.length() > 0) {
              jsOutput.add("      after: '" + rule.afterContext + "',\n");
            }

            if (rule.revisitPosition >= 0) {
              jsOutput.add("      revisit: " + rule.revisitPosition + ",\n");
            }

            // Close this rule
            jsOutput.add("    },\n");
          }
        }
        jsOutput.add("  ];\n");
        phaseNum += 1;
      }

      // Return the rules for each phase.
      StringBuilder sb = new StringBuilder();
      sb.append("  return [");
      for (int phaseId = 0; phaseId < phaseNum; phaseId ++) {
        sb.append("rules").append(phaseId);
        if (phaseId < phaseNum - 1) {
          sb.append(", ");
        }
      }
      sb.append("];\n");

      jsOutput.add(sb.toString());

      jsOutput.add("}\n");

      return jsOutput;
    }

    public ArrayList<String> javaBasicStuff(String inputFilename, String nameSuffix) {
      ArrayList<String> jsOutput = new ArrayList<>();

      jsOutput.add("/* Copyright 2018 Google LLC\n");
      jsOutput.add(" *\n");
      jsOutput.add(" * Licensed under the Apache License, Version 2.0 (the \"License\");\n");
      jsOutput.add(" * you may not use this file except in compliance with the License.\n");
      jsOutput.add(" * You may obtain a copy of the License at\n");
      jsOutput.add(" *\n");
      jsOutput.add(" *    http://www.apache.org/licenses/LICENSE-2.0\n");
      jsOutput.add(" *\n");
      jsOutput.add(" * Unless required by applicable law or agreed to in writing, software\n");
      jsOutput.add(" * distributed under the License is distributed on an \"AS IS\" BASIS,\n");
      jsOutput.add(" * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n");
      jsOutput.add(" * See the License for the specific language governing permissions and\n");
      jsOutput.add(" * limitations under the License.\n");
      jsOutput.add(" */\n");
      jsOutput.add("\n");
      jsOutput.add("package com.google.myanmartools;\n");
      jsOutput.add("\n");
      jsOutput.add("/**\n");
      jsOutput.add(" * Transliteration initialization of phases and rules.\n");
      jsOutput.add(" * Note: this is generated by compiling ICU transliteration rule format.\n");
      jsOutput.add(" * Source rule file = " + inputFilename + "\n");
      jsOutput.add(" */\n");
      jsOutput.add("\n");
      jsOutput.add("import com.google.myanmartools.Transliterate;\n");
      jsOutput.add("import com.google.myanmartools.Phase;\n");
      jsOutput.add("import com.google.myanmartools.Rule;\n");
      jsOutput.add("\n");

      String className = "Transliterate" + nameSuffix;
      jsOutput.add("public final class " + className + " extends Transliterate {\n");

      jsOutput.add("  public " + className + "(String id) {\n");
      jsOutput.add("    // Set identifier for this instance\n");
      jsOutput.add("    super(id);\n");

      return jsOutput;
    }

    public ArrayList<String> generateJava(String inputFilename, String nameSuffix) {
      //For each phase in all phases.

      ArrayList<String> out = javaBasicStuff(inputFilename, nameSuffix);

      int phaseNum = 0;
      for (TranslitPhase phase: phases) {
        String phaseName = "phase" + phaseNum;
        out.add("\n    // Rules for phase " + phaseNum + "\n");
        out.add("    Phase " + phaseName + " = addPhase();\n");

        for (TranslitRule rule: phase.phaseRules) {
          // phase0.addRule(new Rule("patternA", "substA"));
          if (rule.rulePattern != null && !"\n".equals(rule.rulePattern.substring(0, 1))) {
            out.add("    " +  phaseName + ".addRule(new Rule(\"");
            out.add(rule.rulePattern);
            out.add("\", \"");
            out.add(rule.ruleResult);
            out.add("\")");

            if (rule.atStart) {
              out.add("\n          .setMatchOnStart()");
            }
            if (rule.revisitPosition >= 0) {
              out.add("\n          .setRevisitPosition(" + rule.revisitPosition + ")");
            }
            if (rule.beforeContext.length() > 0) {
              out.add("\n          .setBeforeContext(\"" + rule.beforeContext + "\")");
            }
            if (rule.afterContext.length() > 0) {
              out.add("\n          .setAfterContext(\"" + rule.afterContext + "\")");
            }
            out.add(");\n");
          }

        }

        phaseNum += 1;
      }

      out.add("  }\n");
      out.add("}\n");

      return out;
    }
  }

  public static void main(String[] args) throws IOException {
    if (args.length < 2) {
      // Require an input file.
      System.out.println(
          "  Error: Input file path and output file path are required.\n  Optional suffix for function");
      System.out.println(
          "   in_path js_out_path [nameSuffix [java_out_path]]");
      return;
    }

    String nameSuffix = "";
    if (args.length >= 3) {
      nameSuffix = args[2];
    }

    Path javaOutputPath = null;
    if (args.length > 3) {
      javaOutputPath = Paths.get(args[3]);
    }

    // Get the Unicode data input:
    String translitText = getTranslitFile(args[0]);

    /* Use the ICU4J transliterator to preprocess rules, removing defines. */
    String labelForTranslit = "label for transliterator";
    Transliterator transliterator =
        Transliterator.createFromRules(labelForTranslit,
            translitText, Transliterator.FORWARD);

    // Get them as escaped strings.
    String compiled_rules = transliterator.toRules(true);

    AllPhases translitInfo = null;
    try {
      translitInfo = new AllPhases(compiled_rules);
    } catch (Exception ex) {
      System.out.println("Problem with handling rules: " + ex.getMessage());
      System.exit(-1);
    }

    ArrayList<String> jsOutput = translitInfo.generateJS(args[0], nameSuffix);

    ArrayList<String> javaOutput = translitInfo.generateJava(args[0], nameSuffix);

    // Get output file.
    BufferedWriter output = null;

    Path outputPath = Paths.get(args[1]);
    try {
      output = Files.newBufferedWriter(
          outputPath,
          StandardCharsets.UTF_8);
    } catch (IOException e) {
      System.out.println("Error: cannot open output file " + args[1]);
      System.out.println(e);
      System.exit(-2);
    }


    output.write("// TRANSLITERATION RULES");
    output.newLine();

    for (String jsLine : jsOutput) {
      output.write(jsLine);
    }
    output.write("// END OF TRANSLITERATION RULES");
    output.newLine();
    output.close();

    if (javaOutputPath != null) {
      // Write to Java output file, specified in args[3].
      System.out.println("*** Writing Java output file: " + args[3]);

      BufferedWriter javaOutputFile = null;
      try {
        javaOutputFile = Files.newBufferedWriter(
            javaOutputPath,
            StandardCharsets.UTF_8);
      } catch (IOException e) {
        System.out.println("Error: cannot open output file " + javaOutputPath);
        System.out.println(e);
        System.exit(-2);
      }
      for (String javaLine : javaOutput) {
        javaOutputFile.write(javaLine);
      }
      javaOutputFile.write("// END OF TRANSLITERATION RULES");
      javaOutputFile.newLine();
      javaOutputFile.close();
    }
  }

}
