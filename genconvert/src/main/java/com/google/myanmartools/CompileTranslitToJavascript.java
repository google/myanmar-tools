package com.google.myanmartools;

import com.google.common.flags.Flags;
import com.google.common.flags.InvalidFlagValueException;

import com.ibm.icu.text.Transliterator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

import java.lang.Exception;
import java.lang.StringBuilder;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


/** A Java binary to generate Javascript transliteration from an ICU4J
 * transliteration file. The output is a Javascript code that takes
 * text and performs the transliterations specified.
 *
 * Note: this does not implement many features of ICU Transliteration, but
 * does support conversions such as Zawgyi to Unicode for Burmese language.
 */

public final class CompileTranslitToJavascript {

  // String from a transliteration file.
  static private String getTranslitFile(String filepath) {

    // TODO: If the input is an XML file, then get the CDATA.
    System.out.println(" filepath substring = " +
        filepath.substring(filepath.length()-4)+ "\n");
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
      String[] ruleParts = rule.split(" > ");

      String [] patternParts = ruleParts[0].split("\n");

      // TODO: Look for literal |, {, and } within single quote.

      if (ruleParts.length > 1) {
        if (patternParts.length <= 1) {
          rulePattern = ruleParts[0];
        } else {
          rulePattern = patternParts[1];
        }

        // Change ":Wspace:" to the Unicode white spaces.
        rulePattern = rulePattern.replace(":WSpace:", "\u0020\u00a0\u1680\u2000-\u200a\u202f\u205f\u3000");

        atStart = false;
        // Special case for matching at first position
        if (rulePattern.substring(0, 1).equals("^")) {
          // Note the special case and remove the "^" marker
          atStart = true;
          rulePattern = rulePattern.substring(1);
        }
        // Clean up the spaces in the output.
        ruleResult = ruleParts[1].replace(" ", "");

        // TODO: handle literal |
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
        // TODO: handle literal }
        afterContext = "";
        int rbracePos = rulePattern.indexOf('}');
        if (rbracePos >= 0) {
          // Count unescaped "(" and ")"
          // Here's where we remove both the left and right context.
          afterContext = rulePattern.substring(rbracePos + 1);
          rulePattern = rulePattern.substring(0, rbracePos);
        }

        // Remove unescaped { (before_context).
        // TODO: handle literal {
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
    private List<TranslitRule> phaseRules;

    public TranslitPhase(String phaseStrings) {
      phaseRules = new ArrayList<TranslitRule>();

      String rules[] = phaseStrings.split(";");

      for (String rule : rules) {
        TranslitRule newRule = new TranslitRule();
        newRule.AddRule(rule);
        phaseRules.add(newRule);
      }
    }
  }

  static private class AllPhases {
    private ArrayList<TranslitPhase> phases;

    private String description;

    public AllPhases(String allPhases) {
      phases = new ArrayList<TranslitPhase>();

      // Handle each phase string
      //   a. split into rules with " > "
      //   b. remove extra spaces in output.
      String inPhases[] = allPhases.split("::Null;");

      for (String phase : inPhases) {
        // Add phase object.
        TranslitPhase newPhase = new TranslitPhase(phase);
        phases.add(newPhase);
      }

    }

    public void SetDescription(String desc) {
      description = desc;
    }

    // The functions reused in the JS implementation.
    public ArrayList<String> javascriptBasicStuff() {
      ArrayList<String> jsOutput = new ArrayList<String>();

      jsOutput.add("function transliterate(inString) {\n");
      jsOutput.add("  var outString = inString;\n");

      return jsOutput;
    }

    public ArrayList<String> generateJS() {
      //For each phase in all phases.

      ArrayList<String> jsOutput = javascriptBasicStuff();
      jsOutput.add("\n");

      int phaseNum = 0;
      for (TranslitPhase phase: phases) {

        jsOutput.add("  // Rules for phase " + phaseNum + "\n");
        jsOutput.add("  var rules" + phaseNum + " = [\n");

        for (TranslitRule rule: phase.phaseRules) {
          if (rule.rulePattern != null && rule.rulePattern.substring(0,1) != "\n") {
            jsOutput.add("    {\n");

            if (rule.afterContext.length() == 0) {
              jsOutput.add("      p: RegExp('^' + '" + rule.rulePattern + "', 'u'),\n");
              jsOutput.add("      s: '" + rule.ruleResult + "',\n");
            } else {
              // Add after context as another unnamed group.
              jsOutput.add("      p: RegExp('^' + '" + rule.rulePattern +
                  "(" + rule.afterContext + ")" + "', 'u'),\n");
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

        // Process the phase
        jsOutput.add("  outString = runPhase(rules" + phaseNum + ", outString);\n");

        phaseNum += 1;
      }

      jsOutput.add("  return outString;\n");
      jsOutput.add("}\n");

      System.out.println("// *** Number of phases found: " + phaseNum);
      return jsOutput;
    }

    public ArrayList<String> generateJava() {
      //For each phase in all phases.

      ArrayList<String> out = new ArrayList<String>();
      out.add("\n");

      // TODO: Add structure for Java data.
      // TODO: Write runPhases for Java.

      int phaseNum = 0;
      for (TranslitPhase phase: phases) {

        out.add("  // Rules for phase " + phaseNum + "\n");
        out.add("   rules" + phaseNum + " = [\n");

        for (TranslitRule rule: phase.phaseRules) {
        }

        phaseNum += 1;
      }
      return out;
    }
  }


  public static void main(String[] args) throws IOException, InvalidFlagValueException {
    args = Flags.parseAndReturnLeftovers(args);
    if (args.length <= 0) {
      // Require an input file.
      System.out.println(
          "Error: Input file path is required. Output pat may be specified.");
      return;
    }

    // Get the Unicode data input:
    System.out.println("// Input path: " + args[0]);

    String translitText = getTranslitFile(args[0]);

    /* Use the ICU4J tra31nsliterator to preprocess rules, removing defines. */
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

    // TODO: add runtime flag for JS or Java output (or ...)
    ArrayList<String> jsOutput = translitInfo.generateJS();

    ArrayList<String> javaOutput = translitInfo.generateJava();

    // Get output file.
    BufferedWriter output = null;

    if (args.length < 2) {
      output = new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8));
    } else {
      // Create a file and dump to that output.
      Path outputPath = Paths.get(args[1]);

      try {
        output = Files.newBufferedWriter(
            outputPath,
            StandardCharsets.UTF_8,
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE);
      } catch (IOException e) {
        System.out.println("Error: cannot open output file " + args[1]);
        System.out.println(e.toString());
        System.exit(-2);
      }
    }

    output.write("// TRANSLITERATION RULES");
    output.newLine();

    for (String jsLine : jsOutput) {
      output.write(jsLine);
    }
    output.write("// END OF TRANSLITERATION RULES");
    output.newLine();

    output.close();
  }

}
