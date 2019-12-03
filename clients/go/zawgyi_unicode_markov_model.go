package myanmartools

import (
	"bufio"
	"fmt"
	"math"
)

const (
	//ModelBinaryTag Magic number used to identify this object in byte streams. (Reads in ASCII as "UZMODEL")
	ModelBinaryTag = 0x555A4D4F44454C20
	//StdCp0 Standard Myanmar code point range before digits
	StdCp0 int = '\u1000'
	//StdCp1 Standard Myanmar code point range before digits
	StdCp1 int = '\u103F'
	//AftCp0 standard myanmar code point range after digits
	AftCp0 int = '\u104A'
	//AftCp1 standard myanmar code point range after digits
	AftCp1 int = '\u109F'
	//ExaCp0 extended myanmar code point range A
	ExaCp0 int = '\uAA60'
	//ExaCp1 extended myanmar code point range A
	ExaCp1 int = '\uAA7F'
	//ExbCp0 extended Myanmar code point range B
	ExbCp0 int = '\uA9E0'
	//ExbCp1 extended Myanmar code point range B
	ExbCp1 int = '\uA9FF'
	//SpcCp0 unicode space character
	SpcCp0 int = '\u2000'
	//SpcCp1 unicode space character
	SpcCp1 int = '\u200B'

	//StdOffset index into Markov Nodes
	StdOffset int = 1
	//AftOffset index into Markov Nodes
	AftOffset int = StdOffset + StdCp1 - StdCp0 + 1
	//ExaOffset index into Markov Nodes
	ExaOffset int = AftOffset + AftCp1 - AftCp0 + 1
	//ExbOffset index into Markov Nodes
	ExbOffset int = ExaOffset + ExaCp1 - ExaCp0 + 1
	//SpcOffset index into Markov Nodes
	SpcOffset int = ExbOffset + ExbCp1 - ExbCp0 + 1
	//EndOffset index into Markov Nodes
	EndOffset int = SpcOffset + SpcCp1 - SpcCp0 + 1

	//Ssv originally stands for state set version

	//SsvStdExaExbSpc include Myanmar, Extended A, Extended B and space-like
	SsvStdExaExbSpc int = 0
	//SsvStdExaExb same as above but no space-like code points
	SsvStdExaExb int = 1
	//SsvCount ssv count
	SsvCount int = 2
)

//ZawgyiUnicodeMarkovModel interface
type ZawgyiUnicodeMarkovModel interface {
	GetIndexForCodePoint(cp int, ssv int) int
	GetSize(ssv int) int
	Predict(input string) float64
}
type zawgyiUnicodeMarkovModel struct {
	classifier BinaryMarkov
	ssv        int
}

func (*zawgyiUnicodeMarkovModel) GetIndexForCodePoint(cp int, ssv int) int {
	// fmt.Printf("Getting Index for code point %d\n", cp)
	if StdCp0 <= cp && cp <= StdCp1 {
		// fmt.Printf("Code point %d between StdCp0 and StdCp1\n", cp)
		return cp - StdCp0 + StdOffset
	}

	if AftCp0 <= cp && cp <= AftCp1 {
		// fmt.Printf("Code point %d between AftCp0 and AftCp1\n", cp)
		return cp - AftCp0 + AftOffset
	}

	if ExaCp0 <= cp && cp <= ExaCp1 {
		// fmt.Printf("Code point %d between ExaCp0 and ExaCp1\n", cp)
		return cp - ExaCp0 + ExaOffset
	}

	if ExbCp0 <= cp && cp <= ExbCp1 {
		// fmt.Printf("Code point %d between ExbCp0 and ExbCp1\n", cp)
		return cp - ExbCp0 + ExbOffset
	}

	if ssv == SsvStdExaExbSpc && SpcCp0 <= cp && cp <= SpcCp1 {
		// fmt.Printf("Code point %d between SpcCp0 and SpcCp1 and code point == SsvStdExaExbSpc\n", cp)
		return cp - SpcCp0 + SpcOffset
	}
	// fmt.Printf("Code point is %d\n", cp)
	return 0
}

func (*zawgyiUnicodeMarkovModel) GetSize(ssv int) int {
	if ssv == SsvStdExaExbSpc {
		return EndOffset
	}
	return SpcOffset
}

func (zumm *zawgyiUnicodeMarkovModel) Predict(input string) float64 {

	// Start at the base state
	var prevState int = 0

	var totalDelta float64 = 0.0
	seenTransition := false

	for offset := 0; offset <= len([]rune(input)); offset++ {
		var currState int
		var cp int
		if offset == len([]rune(input)) {
			currState = 0
			cp = 0
		} else {
			cp = int([]rune(input)[offset])
			// fmt.Printf("Char code %d\n", cp)
			currState = zumm.GetIndexForCodePoint(cp, zumm.ssv)
			// fmt.Printf("Index for code point %d\n", currState)
		}

		//Ignore 0-to-0 transitions
		if prevState != 0 || currState != 0 {
			delta := zumm.classifier.GetLogProbabilityDifference(prevState, currState)
			// fmt.Printf("Log Probability Difference: %.20f\n", delta)
			totalDelta += float64(delta)
			seenTransition = true
		}
		prevState = currState
	}

	// Special case: if there is no signal, return -Infinity,
	// which will get interpreted by users as strong Unicode.
	// This happens when the input string contains no Myanmar-range code points.
	if !seenTransition {
		return math.Inf(-1)
	}

	// result = Pz/(Pu+Pz)
	//        = exp(logPz)/(exp(logPu)+exp(logPz))
	//        = 1/(1+exp(logPu-logPz))

	return 1.0 / (1.0 + math.Exp(totalDelta))
}

//NewZawgyiUnicodeMarkovModel creates and
//returns ZawgyiUnicodeMarkovModel implementation for classifier and ssv
func NewZawgyiUnicodeMarkovModel(classifier BinaryMarkov, ssv int) ZawgyiUnicodeMarkovModel {
	return &zawgyiUnicodeMarkovModel{classifier: classifier, ssv: ssv}
}

//NewZawgyiUnicodeMarkovModelFromReader creates and
//returns ZawgyiUnicodeMarkovModel from specified dat file
func NewZawgyiUnicodeMarkovModelFromReader(datReader *bufio.Reader) ZawgyiUnicodeMarkovModel {

	modelBinaryTag := readLong(datReader)
	var ssv int
	var classifier BinaryMarkov
	if ModelBinaryTag != modelBinaryTag {
		panic(fmt.Errorf("Unexpected magic number: expected %X but got %X", ModelBinaryTag, modelBinaryTag))
	}
	binaryVersion := readInt(datReader)
	if binaryVersion == 1 {
		ssv = SsvStdExaExbSpc
	} else if binaryVersion == 2 {
		ssv = readInt(datReader)
	} else {
		panic(fmt.Errorf("Unexpected serial version number; expected 1 or 2 but got %X", binaryVersion))
	}

	if ssv < 0 || ssv >= SsvCount {
		panic(fmt.Errorf("Unexpected value in ssv position; expected 0 or 1 but %X", ssv))
	}
	classifier = NewBinaryMarkov(datReader)
	return &zawgyiUnicodeMarkovModel{classifier: classifier, ssv: ssv}
}
