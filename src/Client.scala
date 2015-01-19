import Chooser.randomDouble
import Utilities.createMap

class Client (config: Configuration, network: Network) {
  import config._
  import network._

  val OwnRecordsExplanation = DueToOwnRecords (this)
  val PeerRecordsExplanation = DueToPeerRecords (this)

  val preferences = createMap (Qualities) (randomDouble (0.0, 1.0))
}
