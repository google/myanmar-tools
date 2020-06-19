import CMyanmarTools

public final class ZawgyiDetector {
  let detector = GMTOpenZawgyiDetector()
  deinit {
    GMTCloseZawgyiDetector(detector)
  }
  public func getZawgyiProbability(_ str: String) -> Double {
    return GMTGetZawgyiProbabilityWithLength(
      detector, str, Int32(truncatingIfNeeded: str.utf8.count))
  }
}
