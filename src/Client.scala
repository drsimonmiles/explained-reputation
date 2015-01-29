import Chooser.{distributionSummingToOne, randomDouble}
import Utilities.toMap

/** A client agent */
class Client (config: Configuration, network: Network, records: Records) {
  import config.Terms
  import network.providers
  import records.recordInteraction
  import config.Assessor.calculateTrust

  /** Preferences across service quality terms, each term mapped to a weight from 0.0 to 1.0, with weights summing to 1.0 */
  val termPreferences: Map[Term, Double] = distributionSummingToOne (Terms)
  /** The weight the client gives to its own past interactions in reputation assessment */
  val ownInteractionsWeight = randomDouble (0.5, 1.0)
  /** The weight the client gives to past interactions of peers in reputation assessment */
  val peerInteractionsWeight = 1.0 - ownInteractionsWeight
  /** The weight the client gives to reputation in selecting a provider */
  val reputationWeight = randomDouble (0.0, 1.0)
  /** The weight the client gives to match of their preferences to the advertised service in selecting a provider */
  val offerMatchWeight = 1.0 - reputationWeight

  /** Select a provider to request a service from in the given round */
  def selectProvider (round: Int): Provider = {
    /** Calculate a recommendation rating for the given provider */
    def calculateFit (provider: Provider): Double =
      (reputationWeight * calculateTrust (this, provider, round, records)) + (offerMatchWeight * calculateOfferMatch (provider))
    /** Calculate the match of the given provider's offer to the client's preferences (i.e. the utility the client
      * expects to receive should the provider be perfectly competent */
    def calculateOfferMatch (provider: Provider): Double = {
      val offer = provider.getOffer (this)
      Terms.map (term => offer (term) * termPreferences (term)).sum
    }

    /** Choose the provider with the best recommendation rating */
    providers.sortBy (calculateFit).last
  }

  /** Request a service from the given provider in the given round, and record ratings on how the interaction went */
  def requestService (provider: Provider, round: Int) {
    val service = provider.provideService (this)
    val ratings = toMap (Terms) (term => service (term) * termPreferences (term))
    recordInteraction (provider, this, round, ratings)
  }
}
