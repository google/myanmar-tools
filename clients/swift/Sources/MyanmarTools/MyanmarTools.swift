import CMyanmarTools

func getZawgyiProbability(_ str: String) -> Double {
    return GMTGetZawgyiProbabilityWithLength(str, Int32(truncatingIfNeeded: str.utf8.count))
}
