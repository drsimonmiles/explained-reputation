import jaspr.domain.Term
import java.lang.Math.{E, pow}

/** The FIRE reputation assessment strategy */
class FIRE (config: Configuration) {
  import config.terms

  /** Calculates the trust in a provider for a service */
  def calculateTrust (client: Client, provider: Provider, round: Int, records: Records): Double = {
    /** Gather all relevant interaction records */
    val (ownInteractions, peerInteractions) = records.getInteractionRecords (provider, client)

    /** Per term, calculate the weighted trust value based on the interactions */
    def calculateTermTrust (term: Term) = {
      /** Per reputation type and term, calculate the weighted sum */
      def calculateTypedTermTrust (interactions: List[Interaction]): Double = {
        val weightsAndRatings = interactions.map (interaction => (calculateRecency (interaction), interaction.ratings (term)))
        val weightedRatings = weightsAndRatings.map (x => x._1 * x._2).sum
        val weightsSum = weightsAndRatings.map (_._1).sum
        weightedRatings / weightsSum
      }
      /** Calculate the recency weighting of an interaction in the given current round */
      def calculateRecency (interaction: Interaction): Double =
        pow (E, -((round - interaction.round) / client.recencyScalingFactor))

      /** Calculate the weighted sum of ratings from own and witness interactions */
      calculateTypedTermTrust (ownInteractions) * client.ownInteractionsWeight +
        calculateTypedTermTrust (peerInteractions) * client.peerInteractionsWeight
    }
    /** Weighted sum over the term-specific trust values */
    terms.map (term => calculateTermTrust (term) * client.termPreferences (term)).sum
  }
}
