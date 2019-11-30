package myanmartools

import (
	"bufio"
	"bytes"
	"math"
	"math/big"
	"strings"
	"testing"
	"github.com/google/myanmar-tools/clients/go/internal/resources"
)

const float64EqualityThreshold = 1E-9

func almostEqual(a, b float64) bool {
	if b == math.Inf(-1) && a == math.Inf(-1) {
		return true
	}
	return math.Abs(a-b) <= float64EqualityThreshold
}

func TestZawgyiDetectorBehavior(t *testing.T) {
	t.Run("should produce expected results on strings with strong signals", func(t *testing.T) {
		detector := NewZawgyiDetector()
		strongUnicode := "အပြည်ပြည်ဆိုင်ရာ လူ့အခွင့်အရေး ကြေညာစာတမ်း"
		strongZawgyi := "အျပည္ျပည္ဆိုင္ရာ လူ႔အခြင့္အေရး ေၾကညာစာတမ္း"

		up := detector.GetZawgyiProbability(strongUnicode)
		if up >= 0.001 {
			t.Errorf("Expected strong unicode text probability to be less then 0.001, gotten %f", up)
		}
		zp := detector.GetZawgyiProbability(strongZawgyi)

		if zp < 0.999 {
			t.Errorf("Expected strong zawgyi text probability to be greater then 0.999, gotten %f", up)
		}
	})
}

func TestCompatibility(t *testing.T) {
	t.Run("should produce the same resuls as in compatibility.csv", func(t *testing.T) {
		detector := NewZawgyiDetector()
		data, err := resources.Asset("resources/compatibility.tsv")
		if err != nil {
			t.Fatalf("could not load resources/compatibility.tsv file %#v", err)
		}

		buf := bytes.NewBuffer(data)
		scanner := bufio.NewScanner(buf)
		for scanner.Scan() {
			line := scanner.Text()
			entries := strings.Split(line, "\t")
			floatString := entries[0]
			var expectedProbability float64
			if floatString == "-Infinity" {
				expectedProbability = math.Inf(-1)
			} else {
				bigFloat, _, err := big.ParseFloat(floatString, 10, 30, big.ToNearestEven)
				expectedProbability, _ = bigFloat.Float64()
				if err != nil {
					t.Errorf("Error while parsing floatString: %#v", err)
				}
			}
			testInput := entries[1]
			result := detector.GetZawgyiProbability(testInput)
			if !almostEqual(result, expectedProbability) {
				t.Errorf("Expected probability to be %.10f gotten %.10f", expectedProbability, result)
			}
		}
	})
}
