package myanmartools

import (
	"bufio"
	"encoding/binary"
	"fmt"
)

const (
	//BinaryTag Magic number used to identify
	//this object in byte slice
	BinaryTag = 0x424D41524B4F5620

	//BinaryVersion current serial format
	//version number, used in assiciation with magic number
	BinaryVersion = 0
)

//BinaryMarkov interface
type BinaryMarkov interface {
	GetLogProbabilityDifference(i1 int, i2 int) float32
}

type binaryMarkovImpl struct {
	logProbabilityDifferences [][]float32
}

func (bm *binaryMarkovImpl) GetLogProbabilityDifference(i1 int, i2 int) float32 {
	return bm.logProbabilityDifferences[i1][i2]
}

//NewBinaryMarkov return new instance of BinaryMarkov implementation
func NewBinaryMarkov(datReader *bufio.Reader) BinaryMarkov {

	binaryTag := readLong(datReader)
	if BinaryTag != binaryTag {
		panic(fmt.Errorf("Unexpected magic number; expected %X but got %X", BinaryTag, binaryTag))
	}
	binaryVersion := readInt(datReader)

	if BinaryVersion != binaryVersion {
		panic(fmt.Errorf("Unexpected serial version number; expected %X but got %X", BinaryVersion, binaryVersion))
	}
	size := readShort(datReader)

	var logProbabilityDifferences [][]float32
	var i int16
	for i = 0; i < size; i++ {
		logProbabilityDifferences = append(logProbabilityDifferences, make([]float32, size, size))
	}
	var i1 int16
	for i1 = 0; i1 < size; i1++ {
		entries := readShort(datReader)
		var fallback float32 = 0.0
		if entries != 0 {
			fallback = readFloat(datReader)
		}
		var next int16 = -1
		var i2 int16
		for i2 = 0; i2 < size; i2++ {
			if entries > 0 && next < i2 {
				next = readShort(datReader)
				entries--
			}
			if next == i2 {
				logProbabilityDifferences[i1][i2] = readFloat(datReader)
			} else {
				logProbabilityDifferences[i1][i2] = fallback
			}
		}
	}
	return &binaryMarkovImpl{logProbabilityDifferences: logProbabilityDifferences}
}

func readLong(reader *bufio.Reader) int64 {
	var result int64
	err := binary.Read(reader, binary.BigEndian, &result)
	panicOnError(err)
	return result
}

func readShort(reader *bufio.Reader) int16 {
	var result int16
	err := binary.Read(reader, binary.BigEndian, &result)
	panicOnError(err)
	return result
}

func readInt(reader *bufio.Reader) int {
	var result int32
	err := binary.Read(reader, binary.BigEndian, &result)
	panicOnError(err)
	return int(result)
}

func readFloat(reader *bufio.Reader) float32 {
	var result float32
	err := binary.Read(reader, binary.BigEndian, &result)
	panicOnError(err)
	return result

}

func readFloat64(reader *bufio.Reader) float64 {
	var result float64
	err := binary.Read(reader, binary.BigEndian, &result)
	panicOnError(err)
	return result
}

func panicOnError(err error) {
	if err != nil {
		panic(err)
	}
}
