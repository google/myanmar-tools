# Myanmar Tools Golang Documentation

This documentation is for Golang specific usage of *Myanmar Tools*.  For general documentation.

## Installation
```bash
$ go get -u github.com/google/myanmar-tools/clients/go
```
## Usage

To detect Zawgyi, create an instance of ZawgyiDetector and call `GetZawgyiProbability` with your string.

```go
import (
    "fmt"
    "github.com/google/myanmar-tools/clients/go"
)

func main() {
    zgDetector := myanmartools.NewZawgyiDetector()
    score := zgDetector.GetZawgyiProbability("မ္း")
    fmt.Printf("Score: %f\n", score) 
    // print Score: 0.9999772 to the console
    // it is very likely to be typed in zawgyi
}
```

To convert from Zawgyi to Unicode, you can use [Rabbit Converter Go version](https://github.com/Rabbit-Converter/Rabbit-Go).  You can install it like this:

```bash
$ go get -u github.com/Rabbit-Converter/Rabbit-Go
```

You can now convert text Between Zawgyi <=> Unicode like this:

```go
package main

import (
    "fmt"
    "github.com/Rabbit-Converter/Rabbit-Go"
)

func main() {
    fmt.Println(rabbit.Zg2uni(("သီဟိုဠ္မွ ဉာဏ္ႀကီးရွင္သည္ အာယုဝဍ္ဎနေဆးၫႊန္းစာကို ဇလြန္ေဈးေဘးဗာဒံပင္ထက္ အဓိ႒ာန္လ်က္ ဂဃနဏဖတ္ခဲ့သည္။"))
    // output is now "သီဟိုဠ်မှ ဉာဏ်ကြီးရှင်သည် အာယုဝဍ်ဎနဆေးညွှန်းစာကို ဇလွန်ဈေးဘေးဗာဒံပင်ထက် အဓိဋ္ဌာန်လျက် ဂဃနဏဖတ်ခဲ့သည်။")
    fmt.Println(rabbit.Unizg("သီဟိုဠ်မှ ဉာဏ်ကြီးရှင်သည် အာယုဝဍ်ဎနဆေးညွှန်းစာကို ဇလွန်ဈေးဘေးဗာဒံပင်ထက် အဓိဋ္ဌာန်လျက် ဂဃနဏဖတ်ခဲ့သည်။"))
    // output is now "သီဟိုဠ္မွ ဉာဏ္ႀကီးရွင္သည္ အာယုဝဍ္ဎနေဆးၫႊန္းစာကို ဇလြန္ေဈးေဘးဗာဒံပင္ထက္ အဓိ႒ာန္လ်က္ ဂဃနဏဖတ္ခဲ့သည္။")

}
```
