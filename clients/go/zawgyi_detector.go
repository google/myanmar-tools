package myanmartools

import (
	"bufio"
	"bytes"
	"github.com/google/myanmar-tools/clients/go/internal/resources"
)

//go:generate go-bindata -o ./internal/resources/resources.go -pkg resources ./resources

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
	binaryMarkovData := resources.MustAsset("resources/zawgyiUnicodeModel.dat")
	datReader := bufio.NewReader(bytes.NewBuffer(binaryMarkovData))
	model := NewZawgyiUnicodeMarkovModelFromReader(datReader)
	return &ZawgyiDetector{
		model: model,
	}
}
