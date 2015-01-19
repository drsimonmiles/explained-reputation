import Chooser.chooseFrom
import Utilities.{createMutableMap, percentEqual, percentMatching, toMap}

class Provider (configuration: Configuration, network: Network) {
  import configuration._
  import network._

  var competency = chooseFrom (PossibleCompetencies)
  var offer = createMutableMap (Qualities) (1.0 / NumberOfQualities)
  var tailoring = createMutableMap (clients) (0.0)

  def improve (explanations: Seq[Explanation]) {
    val percentSuccess = percentEqual (explanations, Successful)
    val percentQuality = toMap (Qualities) (q => percentMatching (explanations) {case x: DueToQuality if x.quality == q => x})
    val percentPeers = percentMatching (explanations) {case x: DueToPeerRecords => x}
    val improveOffer = toMap (Qualities) ()
  }
}
