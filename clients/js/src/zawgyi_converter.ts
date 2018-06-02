/* Copyright 2017 Google LLC
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

//
type TranslitRule = {
    p: RegExp;
    s: string;
    matchOnStart?: boolean;
    revisit?: number;
};


function runPhase(rules: TranslitRule[], inString: string): string {
    let outString = "";
    let midString = inString;
    let startOfString = true;
    while (midString.length > 0) {
        let foundRule = false;
        for (let rule of rules) {
            if (rule.matchOnStart == null || startOfString) {
                let m = midString.match(rule.p);
                // Matching uses only unnamed groups
                if (m != null) {
                    foundRule = true;
                    let rightPartSize = midString.length - m[0].length;
                    midString = midString.replace(rule.p, rule.s);
                    let newStart = midString.length - rightPartSize;

                    if (rule.revisit == null) {
                        // New location is reset unless "revisit" is set to beginning of replacement.
                        outString += midString.substring(0, newStart);
                        midString = midString.substring(newStart);
                    }
                }
            }
        }
        if (!foundRule) {
            outString += midString[0];
            midString = midString.substring(1);
        }
        startOfString = false;
    }
    return outString;
    // End of phase
}


function runAllPhases(allRules: TranslitRule[][], inString: string): string {
    let outString = inString;
    for (let rules of allRules) {
        outString = runPhase(rules, outString);
    }
    return outString;
}


// declare function getAllRulesZ2U(): TranslitRule[][];
<Z2U_CONVERSION_RULES_PLACEHOLDER>

// declare function getAllRulesU2Z(): TranslitRule[][];
<U2Z_CONVERSION_RULES_PLACEHOLDER>

// declare function getAllRulesZNorm(): TranslitRule[][];
<ZNORM_CONVERSION_RULES_PLACEHOLDER>


export class ZawgyiConverter {

    public constructor() {
    }

    public zawgyiToUnicode(inString: string) {
        return runAllPhases(getAllRulesZ2U(), inString);
    }

    public unicodeToZawgyi(inString: string) {
        return runAllPhases(getAllRulesU2Z(), inString);
    }

    public normalizeZawgyi(inString: string) {
        return runAllPhases(getAllRulesZNorm(), inString);
    }
}
