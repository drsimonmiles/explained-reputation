import jaspr.domain.Term
import Chooser.{distributionSummingToOne, ifHappens, randomDouble, randomInt}
import Explanations.explain
import Utilities.toMap
import jaspr.explanation.ijcai.IJCAIExplanation

/** A client agent */
class Client (val group: Int, config: Configuration, network: Network, records: Records) {
  import config._
  import assessor.calculateTrust
  import network.providers
  import records.recordInteraction

  /** Preferences across service quality terms, each term mapped to a weight from 0.0 to 1.0, with weights summing to 1.0 */
  val termPreferences: Map[Term, Double] = distributionSummingToOne (terms)
  /** The weight the client gives to its own past interactions in reputation assessment */
  val ownInteractionsWeight = randomDouble (0.5, 1.0)
  /** The weight the client gives to past interactions of peers in reputation assessment */
  val peerInteractionsWeight = 1.0 - ownInteractionsWeight
  /** The weight the client gives to match of their preferences to the advertised service in selecting a provider */
  // val offerMatchWeight = 0.0// randomDouble (0.0, 1.0)
  /** The weight the client gives to reputation in selecting a provider */
  // val reputationWeight = 1.0 - offerMatchWeight
  /** A wrapper for this agent used for the explanation generation library */
  val agentID = new ClientAgentIdentifier (this, config, records)
  /** The number of rounds remembered by this client, from which recency weighting is also calculated */
  val recentMemoryLength = randomInt (MinimumRecentMemoryLength, MaximumRecentMemoryLength)
  /** FIRE's recency scaling factor for interaction ratings (lambda) */
  val recencyScalingFactor = calculateRecencyWeight (recentMemoryLength)

  /** Select a provider to request a service from in the given round, along with explanations of reputation assessments */
  /*def selectProvider (round: Int): (Provider, List[IJCAIExplanation]) = {
    // Calculate the match of the given provider's offer to the client's preferences (i.e. the utility the client
    // expects to receive should the provider be perfectly competent)
    def calculateOfferMatch (provider: Provider): Double = {
      val offer = provider.getOffer (this)
      terms.map (term => offer (term) * termPreferences (term)).sum
    }
    // Current reputation assessed for this client for each provider
    val reputations = toMap (providers) (calculateTrust (this, _, round, records))
    // Suitability of offer for this client from each provider
    val offerMatches = toMap (providers) (calculateOfferMatch)
    // Calculate a recommendation rating for the given provider
    def calculateFit (provider: Provider): Double =
      (reputationWeight * reputations (provider)) + (offerMatchWeight * offerMatches (provider))
    // Choose the provider with the best recommendation rating
    val selected = providers.sortBy (calculateFit).last
    // Generate explanations of each provider
    val explanations = providers.withFilter (_ != selected).map { rejected =>

      new SelectionExplanation (this, selected, rejected, explain (this, selected, rejected, round))
    }

    (selected, explanations)
  }*/

  /** Select a provider to request a service from in the given round, along with explanations of reputation assessments */
  def selectProvider (round: Int): (Provider, List[IJCAIExplanation]) = {
    val sorted = providers.sortBy (calculateTrust (this, _, round, records)).reverse
    def explore (list: List[Provider]): Option[Provider] = list match {
      case head :: tail => ifHappens[Option[Provider]] (ExplorationProbability) (Some (head)) (explore (tail))
      case Nil => None
    }
    val selected = explore (sorted).getOrElse (sorted.head)
    val explanations = sorted.tail.map { rejected =>
      explain (this, selected, rejected, round)
    }
    (selected, explanations)
  }

  /** Request a service from the given provider in the given round, and record ratings on how the interaction went */
  def requestService (provider: Provider, round: Int) {
    val service = provider.provideService (this)
    val ratings = toMap (terms) (term => service (term) * termPreferences (term))
    recordInteraction (provider, this, round, ratings)
  }
}
