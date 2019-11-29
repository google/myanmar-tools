package myanmartools

import (
	"bufio"
	"bytes"
)

//go:generate go-bindata -o resources.go -pkg myanmartools ./resources

//ZawgyiDetector struct
type ZawgyiDetector struct {
	model ZawgyiUnicodeMarkovModel
}

//GetZawgyiProbability method
func (zd *ZawgyiDetector) GetZawgyiProbability(input string) float64 {
	return zd.model.Predict(input)
}

//NewZawgyiDetector return *NewZawgyiDetector
func NewZawgyiDetector() *ZawgyiDetector {
	binaryMarkovData := MustAsset("resources/zawgyiUnicodeModel.dat")
	datReader := bufio.NewReader(bytes.NewBuffer(binaryMarkovData))
	model := NewZawgyiUnicodeMarkovModelFromReader(datReader)
	return &ZawgyiDetector{
		model: model,
	}
}
