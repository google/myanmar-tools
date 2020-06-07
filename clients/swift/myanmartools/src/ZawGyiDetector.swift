//
//  ZawGyiDetector.swift
//  testPackage
//
//  Created by La Win Ko on 5/6/20.
//

import Foundation

open class ZawgyiDetector{
    private var params: [[Float32]] = [[]]
    private var mapping: [Character: Int] = [:]
    
    init(){
        do{
            if let dataURL = Bundle(for: ZawgyiDetector.self).url(forResource: "zawgyiUnicodeModel", withExtension: "dat"){
                let markov = ZawgyiUnicodeMarkovModel(dataURL: dataURL)
                self.params = markov.getParams()
                self.mapping = markov.mapping()

            }else{
                throw NSError(domain: "File not found error", code: NSFileNoSuchFileError, userInfo: nil)
            }
        }catch (let e as NSError){
            print(e.domain)
        }
    }
    
    open func predict(input: String,verbose: Bool = false) -> Double{
        if verbose{
            print("Running detector on string: \(input)")
        }
        var prevCp: Int = 0
        var prevState: Int = 0
        
        //to store two characters transition value
        var totalDelta: Double = 0.0
        
        var seenTransition: Bool = false
        
        //covert to utf16 array
        let inputArray = Array(input.utf16)
        
        for offset in 0...inputArray.count{
            
            //current state and code point
            var cp: Int = 0
            var currentState = 0

            if (offset == inputArray.count){
                cp = 0
                currentState = 0
            }else{
                //convert character to code point
                let char: Character = Character(UnicodeScalar(inputArray[offset])!)
                cp = Int(char.unicodeScalarCodePoint())

                if let mapIndex = self.mapping[Character(UnicodeScalar(cp)!)]{
                    currentState = mapIndex
                }else{
                    currentState = 0
                }
            }
            
            // Ignore 0-to-0 transitions
            if prevState != 0 || currentState != 0{
                let delta: Float32 = self.params[prevState][currentState]
                
                if verbose{
                    print("\(prevCp) -> \(cp): delta=\(delta)")
                }
                
                totalDelta = totalDelta + Double(delta)
                seenTransition = true
            }

            prevCp = cp
            prevState = currentState
        }
        
        if (verbose) {
            print("Final: delta=\(totalDelta)")
        }
        
        //return negative infinity when input is not burmese
        if !seenTransition{
            return -1 * Double.infinity
        }
        
        return (1.0 / (1.0 + exp(totalDelta)))
    }
}
