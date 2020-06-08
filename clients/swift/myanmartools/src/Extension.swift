//
//  Extension.swift
//  myanmartools
//
//  Created by La Win Ko on 7/6/20.

import Foundation

extension NSData{
    
    struct HexEncodingOptions: OptionSet {
        let rawValue: Int
        static let upperCase = HexEncodingOptions(rawValue: 1 << 0)
    }

    func hexEncodedString(options: HexEncodingOptions = []) -> String {
        let format = options.contains(.upperCase) ? "%02hhX" : "%02hhx"
        return map { String(format: format, $0) }.joined()
    }
    
    func getBytes(range: NSRange) -> [UInt8]? {
        var buffer = [UInt8](repeating: 0, count: range.length)
        self.getBytes(&buffer, range: range)
        return buffer
    }
}


extension String {
    var hexaBytes: [UInt8] {
    var position = startIndex
        
    return (0..<count/2).compactMap { _ in
            defer { position = index(position, offsetBy: 2) }
            return UInt8(self[position...index(after: position)], radix: 16)
        }
    }
    var hexaData: Data { return hexaBytes.data }
}

extension Collection where Element == UInt8 {
    var data: Data {
        return Data(self)
    }
    var hexa: String {
        return map{ String(format: "%02X", $0) }.joined()
    }
}

extension Character
{
    //Return UInt32 of one character
    func unicodeScalarCodePoint() -> UInt32
    {
        let characterString = String(self)
        let scalars = characterString.unicodeScalars

        return scalars[scalars.startIndex].value
    }
}
