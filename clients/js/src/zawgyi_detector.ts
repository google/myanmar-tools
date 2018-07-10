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

const DATA = "<BASE64_DATA_PLACEHOLDER>";

// Standard Myanmar code point range before digits
const STD_CP0 = 0x1000;
const STD_CP1 = 0x103F;

// Standard Myanmar code point range after digits
const AFT_CP0 = 0x104A;
const AFT_CP1 = 0x109F;

// Extended Myanmar code point range A
const EXA_CP0 = 0xAA60;
const EXA_CP1 = 0xAA7F;

// Extended Myanmar code point range B
const EXB_CP0 = 0xA9E0;
const EXB_CP1 = 0xA9FF;

// Unicode space characters
const SPC_CP0 = 0x2000;
const SPC_CP1 = 0x200B;

// Indices into Markov nodes
const STD_OFFSET = 1;
const AFT_OFFSET = STD_OFFSET + STD_CP1 - STD_CP0 + 1;
const EXA_OFFSET = AFT_OFFSET + AFT_CP1 - AFT_CP0 + 1;
const EXB_OFFSET = EXA_OFFSET + EXA_CP1 - EXA_CP0 + 1;
const SPC_OFFSET = EXB_OFFSET + EXB_CP1 - EXB_CP0 + 1;
const NUM_STATES = SPC_OFFSET + SPC_CP1 - SPC_CP0 + 1;

/**
 * SSV: An ID representing which Unicode code points to include in the model:
 *
 * <p>SSV_STD_EXA_EXB_SPC - include Myanmar, Extended A, Extended B, and space-like
 * <p>STD_EXA_EXB - same as above but no space-like code points
 *
 * <p>"SSV" originally stands for State Set Version.
 */
const SSV_STD_EXA_EXB_SPC = 0;
const SSV_STD_EXA_EXB = 1;
const SSV_COUNT = 2;

/**
 * Returns the index of the state in the Markov chain corresponding to the given code point.
 *
 * <p>Code points in the standard Myanmar range, Myanmar Extended A, Myanmar Extended B, and
 * Unicode Whitespace each have a unique state assigned to them. All other code points are mapped
 * to state 0.
 *
 * @param cp The code point to convert to a state index.
 * @return The index of the state in the Markov chain. 0 <= state < getSize()
 */
function getIndexForCodePoint(cp: number, ssv: number): number {
    if (STD_CP0 <= cp && cp <= STD_CP1) {
        return cp - STD_CP0 + STD_OFFSET;
    }
    if (AFT_CP0 <= cp && cp <= AFT_CP1) {
        return cp - AFT_CP0 + AFT_OFFSET;
    }
    if (EXA_CP0 <= cp && cp <= EXA_CP1) {
        return cp - EXA_CP0 + EXA_OFFSET;
    }
    if (EXB_CP0 <= cp && cp <= EXB_CP1) {
        return cp - EXB_CP0 + EXB_OFFSET;
    }
    if (ssv == SSV_STD_EXA_EXB_SPC && SPC_CP0 <= cp && cp <= SPC_CP1) {
        return cp - SPC_CP0 + SPC_OFFSET;
    }
    return 0;
}

function checkMagicNumberAndVersion(stream: DataView, offset: number, expectedBinaryTagLead: number,
    expectedBinaryTagTrail: number, expectedBinaryVersion: number): number {
    const binaryTagLead = stream.getUint32(offset);
    offset += 4;
    if (binaryTagLead !== expectedBinaryTagLead) {
        throw new Error("Unexpected magic number lead; expected "
            + expectedBinaryTagLead.toString(16)
            + " but got "
            + binaryTagLead.toString(16));
    }
    const binaryTagTrail = stream.getUint32(offset);
    offset += 4;
    if (binaryTagTrail !== expectedBinaryTagTrail) {
        throw new Error("Unexpected magic number trail; expected "
            + expectedBinaryTagTrail.toString(16)
            + " but got "
            + binaryTagTrail.toString(16));
    }
    // If expectedBinaryVersion is -1, don't check or consume it.
    if (expectedBinaryVersion !== -1) {
        const binaryVersion = stream.getUint32(offset);
        offset += 4;
        if (binaryVersion !== expectedBinaryVersion) {
            throw new Error("Unexpected serial version number; expected "
                + expectedBinaryVersion.toString(16)
                + " but got "
                + binaryVersion.toString(16));
        }
    }
    return offset;
}

// This is different on Node vs Browser.
let getArrayBuffer: () => ArrayBuffer;

// @if NODEJS
getArrayBuffer = () => {
    // Buffer is defined on Node
    // @ts-ignore
    const nodeBuffer = new Buffer(DATA, "base64");
    const arrayBuffer = new ArrayBuffer(nodeBuffer.length);
    const u8view = new Uint8Array(arrayBuffer);
    for (let i = 0; i < nodeBuffer.length; i++) {
        u8view[i] = nodeBuffer[i];
    }
    return arrayBuffer;
};
// @endif

// @if !NODEJS
getArrayBuffer = () => {
    const binaryString = atob(DATA);
    const arrayBuffer = new ArrayBuffer(binaryString.length);
    const u8view = new Uint8Array(arrayBuffer);
    for (let i = 0; i < binaryString.length; i++) {
        u8view[i] = binaryString.charCodeAt(i);
    }
    return arrayBuffer;
};
// @endif

class BinaryMarkov {

    // JavaScript doesn't have the concept of 64-bit integers, so split the tag into two 32-bit ints.
    private static BINARY_TAG_LEAD = 0x424D4152;
    private static BINARY_TAG_TRAIL = 0x4B4F5620;

    // Current serial format version number, used in association with the magic number.
    private static BINARY_VERSION = 0;

    private logProbabilityDifferences: number[][];

    constructor(stream: DataView, offset: number) {
        // @if NODEJS
        offset = checkMagicNumberAndVersion(stream,
            offset,
            BinaryMarkov.BINARY_TAG_LEAD,
            BinaryMarkov.BINARY_TAG_TRAIL,
            BinaryMarkov.BINARY_VERSION);
        // @endif
        // @if !NODEJS
        // Don't include the magic number checking code in a minified build
        offset += 12;
        // @endif

        const size = stream.getInt16(offset);
        offset += 2;
        const logProbabilityDifferences: number[][] = [];
        for (let i1 = 0; i1 < size; i1++) {
            logProbabilityDifferences[i1] = [];
            let entries = stream.getInt16(offset);
            offset += 2;
            let fallback: number;
            if (entries === 0) {
                fallback = 0.0;
            } else {
                fallback = stream.getFloat32(offset);
                offset += 4;
            }
            let next = -1;
            for (let i2 = 0; i2 < size; i2++) {
                if (entries > 0 && next < i2) {
                    next = stream.getInt16(offset);
                    offset += 2;
                    entries--;
                }
                if (next === i2) {
                    logProbabilityDifferences[i1][i2] = stream.getFloat32(offset);
                    offset += 4;
                } else {
                    logProbabilityDifferences[i1][i2] = fallback;
                }
            }
        }
        this.logProbabilityDifferences = logProbabilityDifferences;
    }

    getLogProbabilityDifference(i1: number, i2: number): number {
        return this.logProbabilityDifferences[i1][i2];
    }
}

class ZawgyiUnicodeMarkovModel {

    // JavaScript doesn't have the concept of 64-bit integers, so split the tag into two 32-bit ints.
    private static BINARY_TAG_LEAD = 0x555A4D4F;
    private static BINARY_TAG_TRAIL = 0x44454C20;

    private classifier: BinaryMarkov;
    private ssv: number;

    constructor(stream: DataView, offset: number) {
        // @if NODEJS
        offset = checkMagicNumberAndVersion(stream,
            offset,
            ZawgyiUnicodeMarkovModel.BINARY_TAG_LEAD,
            ZawgyiUnicodeMarkovModel.BINARY_TAG_TRAIL,
            -1);
        // @endif
        // @if !NODEJS
        // Don't include the magic number checking code in a minified build
        offset += 8;
        // @endif

        const binaryVersion = stream.getUint32(offset);
        offset += 4;
        if (binaryVersion === 1) {
            // No SSV field
            this.ssv = 0;
        } else if (binaryVersion === 2) {
            this.ssv = stream.getUint32(offset);
            offset += 4;
        } else {
            throw new Error("Serial version: expected 1 or 2 but got "
                + binaryVersion.toString(16));
        }

        this.classifier = new BinaryMarkov(stream, offset);
    }

    predict(input: string): number {
        // Start at the base state
        let prevState = 0;

        let totalDelta = 0.0;
        let seenTransition = false;
        for (let offset = 0; offset <= input.length; offset++) {
            let currState;
            if (offset === input.length) {
                currState = 0;
            } else {
                // Note: All interesting characters are in the BMP.
                const cp = input.charCodeAt(offset);
                currState = getIndexForCodePoint(cp, this.ssv);
            }
            // Ignore 0-to-0 transitions
            if (prevState !== 0 || currState !== 0) {
                const delta = this.classifier.getLogProbabilityDifference(prevState, currState);
                totalDelta += delta;
                seenTransition = true;
            }
            prevState = currState;
        }

        // Special case: if there is no signal, return -Infinity,
        // which will get interpreted by users as strong Unicode.
        // This happens when the input string contains no Myanmar-range code points.
        if (!seenTransition) {
            return Number.NEGATIVE_INFINITY;
        }

        // result = Pz/(Pu+Pz)
        //        = exp(logPz)/(exp(logPu)+exp(logPz))
        //        = 1/(1+exp(logPu-logPz))
        return 1.0 / (1.0 + Math.exp(totalDelta));
    }
}

export class ZawgyiDetector {
    private model: ZawgyiUnicodeMarkovModel;

    /** Loads the model from the resource and returns a ZawgyiDetector instance. */
    constructor() {
        // Convert the Base64 to an ArrayBuffer.
        const arrayBuffer = getArrayBuffer();
        this.model = new ZawgyiUnicodeMarkovModel(new DataView(arrayBuffer, 0), 0);
    }

    /**
     * Performs detection on the given string. Returns the probability that the string is Zawgyi given
     * that it is either Unicode or Zawgyi. Values approaching 1 are strong Zawgyi; values approaching
     * 0 are strong Unicode; and values close to 0.5 are toss-ups.
     *
     * <p>If the string does not contain any Myanmar range code points, -Infinity is returned.
     *
     * @param input The string on which to run detection.
     * @return The probability that the string is Zawgyi (between 0 and 1), or -Infinity if the string
     *     contains no Myanmar range code points.
     */
    getZawgyiProbability(input: string): number {
        return this.model.predict(input);
    }
}
