//
//  ZawgyiUnicodeMarkovModel.swift
//  myanmartools
//
//  Created by La Win Ko on 5/6/20.

import Foundation

internal class ZawgyiUnicodeMarkovModel{
    
    // Standard Myanmar code point range before digits
    private let STD_CP0: Character = "\u{1000}"
    private let STD_CP1: Character = "\u{103F}"

    // Standard Myanmar code point range after digits
    private let AFT_CP0: Character = "\u{104A}"
    private let AFT_CP1: Character = "\u{109F}"

    // Extended Myanmar code point range A
    private let EXA_CP0: Character = "\u{AA60}"
    private let EXA_CP1: Character = "\u{AA7F}"

    // Extended Myanmar code point range B
    private let EXB_CP0: Character = "\u{A9E0}"
    private let EXB_CP1: Character = "\u{A9FF}"

    // Unicode space characters
    private let SPC_CP0: Character = "\u{2000}"
    private let SPC_CP1: Character = "\u{200B}"
    
    /** Magic number used to identify this object in byte streams. (Reads in ASCII as "UZMODEL") */
    private let binaryUZMODEL: String = "555A4D4F44454C20"
    private let rangeForbinaryUZMODEL: NSRange = NSRange(location: 0, length: 8)
    
    private let binaryBMARKOV: String = "424D41524B4F5620"
    private var rangeForbinaryBMARKOV: NSRange = NSRange(location: 12, length: 8) //default range unless ssv version is 2
    private let versionOfBMARKOV: Int = 0
    private var rangeForversionOfBMARKOV: NSRange = NSRange(location: 20, length: 4) //default range unless ssv version is 2
    
    private let rangeForSSVTag: NSRange = NSRange(location: 8, length: 4)
    
    private var dataURL: URL
    
    init(dataURL: URL){
        self.dataURL = dataURL
    }
    
    //get parameters
    internal func getParams() -> [[Float32]]{
        
        do{
            if let data = NSData(contentsOf: self.dataURL){
                
                //check UZMODEL
                if binaryUZMODEL != data.getBytes(range: rangeForbinaryUZMODEL)!.hexa{
                    throw NSError(domain: String(format: "Unexpected magic number; expected %@ but got %@", binaryUZMODEL, data.getBytes(range: rangeForbinaryUZMODEL)!.hexa), code: NSFileReadCorruptFileError, userInfo: nil)
                }
                
                var ssv: Int = 0
                
                //check SSV
                ssv = Int(data.getBytes(range: rangeForSSVTag)!.hexa)!
                if ssv == 1{
                    ssv = 0
                }else if ssv == 2{
                    ssv = Int(data.getBytes(range: NSRange(location: 12, length: UInt32.bitWidth))![0])
                }else{
                    throw NSError(domain: String(format: "Unexpected serial version number; expected 1 or 2 but got %i",Int(data.getBytes(range: rangeForSSVTag)![0])), code: NSFileReadUnknownError, userInfo: nil)
                }
                
                //change range start location when SSV version is 2
                if Int(data.getBytes(range: rangeForSSVTag)!.hexa)! != 1{
                    rangeForbinaryBMARKOV = NSRange(location: 16, length: 8)
                }
                
                //check BMARKOV
                if binaryBMARKOV != data.getBytes(range: rangeForbinaryBMARKOV)!.hexa{
                    throw NSError(domain: String(format: "Unexpected magic number; expected %@ but got %@", binaryUZMODEL, data.getBytes(range: rangeForbinaryBMARKOV)!.hexa), code: NSFileReadCorruptFileError, userInfo: nil)
                }
                
                //change range start location when SSV version is 2
                if Int(data.getBytes(range: rangeForSSVTag)!.hexa)! != 1{
                    rangeForversionOfBMARKOV = NSRange(location: 24, length: 4)
                }
                
                //check BMARKOV version
                let tempVersionOfBMARKOV = Int(data.getBytes(range: rangeForversionOfBMARKOV)!.hexa)!
                if tempVersionOfBMARKOV != self.versionOfBMARKOV{
                    throw NSError(domain: String(format: "Unexpected serial version number; expected %i but got %i", self.versionOfBMARKOV, tempVersionOfBMARKOV), code: NSFileReadCorruptFileError, userInfo: nil)
                }
                
                var sizeRange: NSRange
                var size: UInt16 = 0
                
                //change range start location when SSV version is 2
                if Int(data.getBytes(range: rangeForSSVTag)!.hexa)! != 1{
                    sizeRange = NSRange(location: 28, length: 2)
                }else{
                    sizeRange = NSRange(location: 24, length: 2)
                }
                
                //get data size
                let sizeData = NSData(bytes: data.getBytes(range: sizeRange)!, length: 2)
                sizeData.getBytes(&size, length: 2)
                size = UInt16(bigEndian: size)
                
                //read logProbabilityDifferences
                var location = sizeRange.location
                location = location + 2
                var logProbabilityDifferences: [[Float32]] = [[Float32]](repeating: [Float32](repeating: 0, count: Int(size)), count: Int(size))
                
                for i in 0..<size{
                    var entries: Int16 = 0
                    entries = int16Value(bytesArr: data.getBytes(range: NSRange(location: location, length: 2))!)
                    location = location + 2
                    
                    var fallBack: Float32
                    if entries == 0{
                        fallBack = 0
                    }else{
                        fallBack = float32Value(bytesArr: data.getBytes(range: NSRange(location: location, length: 4))!)
                        location = location + 4
                    }

                    var next: Int16 = -1
                    
                    for i2 in 0..<size{
                        if entries > 0 && next < i2{
                            next = int16Value(bytesArr: data.getBytes(range: NSRange(location: location, length: 2))!)
                            location = location + 2
                            entries -= 1
                        }
                        if next == i2{
                            logProbabilityDifferences[Int(i)][Int(i2)] = float32Value(bytesArr: data.getBytes(range: NSRange(location: location, length: 4))!)
                            location = location + 4
                        }else{
                            logProbabilityDifferences[Int(i)][Int(i2)] = fallBack
                        }
                        
                    }
                }
                
                return logProbabilityDifferences

            }else{
                throw NSError(domain: "File not found error", code: NSFileNoSuchFileError, userInfo: nil)
            }
        }catch (let e as NSError){
            print(e.domain)
        }
        
        return [[]]
    }
    
    //mapping myanmar standard unicode indices
    internal func mapping() -> [Character: Int]{
        let beforeDigits: [UInt32] = getChars(start: STD_CP0, end: STD_CP1)
        let afterDigits: [UInt32] = getChars(start: AFT_CP0, end: AFT_CP1)
        let rangeA: [UInt32] = getChars(start: EXA_CP0, end: EXA_CP1)
        let rangeB: [UInt32] = getChars(start: EXB_CP0, end: EXB_CP1)
        let spaceChars: [UInt32] = getChars(start: SPC_CP0, end: SPC_CP1)
        
        //adding all characters into an array
        let myanmarCodePoints: [UInt32] = beforeDigits + afterDigits + rangeA + rangeB + spaceChars
        
        var mapping: [Character: Int] = [:]
        var index = 1
        
        for codePoint in myanmarCodePoints{
            let unicodeScalar = UnicodeScalar(codePoint)
            mapping[Character(unicodeScalar!)] = index
            index += 1
        }
        
        return mapping
    }
    
    /**
     * Runs and get the array of code points between two character range
     *
     * @param start Starting character
     * @param end ending character
     * @return The code points array of UInt32
     */
    internal func getChars(start: Character, end: Character) -> [UInt32]{
        var codePoints: [UInt32] = []
        
        for codePoitnt in start.unicodeScalarCodePoint()...end.unicodeScalarCodePoint(){
            codePoints.append(codePoitnt)
        }
        
        return codePoints
    }
    
    //return float32 value from bytes array
    internal func float32Value(bytesArr: [UInt8]) -> Float32 {
        let data = NSData(bytes: bytesArr, length: 4) as Data
        return Float32(bitPattern: UInt32(bigEndian: data.withUnsafeBytes { $0.load(as: UInt32.self) }))
    }
    
    //return int16 value from bytes array
    internal func int16Value(bytesArr: [UInt8]) -> Int16{
        let data = NSData(bytes: bytesArr, length: 2) as Data
        return Int16(bitPattern: UInt16(bigEndian: data.withUnsafeBytes { $0.load(as: UInt16.self) }))
    }
}
