import Utilities.createMutableMap

class Records (network: Network) {
  var log = createMutableMap (network.providers) (List[Double] ())
}
