/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul, King's College London, and University of Warwick
 */

import jaspr.domain.Term
import Chooser.{distributionSummingToOne, ifHappens, randomDouble, randomInt}
import Explanations.explain
import Utilities.toMap
import jaspr.explanation.ijcai.IJCAIExplanation

/** A client agent */
class Client (val group: Group, config: Configuration, network: Network, records: Records) {
  import config._
  import assessor.calculateTrust
  import network.providers
  import records.recordInteraction

  /** Preferences across service quality terms, each term mapped to a weight from 0.0 to 1.0, with weights summing to 1.0 */
  val termPreferences: Map[Term, Double] = distributionSummingToOne (terms, group.bias)
  /** The weight the client gives to its own past interactions in reputation assessment */
  val ownInteractionsWeight = randomDouble (ownInteractionsBias, 1.0)
  /** The weight the client gives to past interactions of peers in reputation assessment */
  val peerInteractionsWeight = 1.0 - ownInteractionsWeight
  /** The number of rounds remembered by this client, from which recency weighting is also calculated */
  val recentMemoryLength = randomInt (minimumRecentMemoryLength, maximumRecentMemoryLength)
  /** FIRE's recency scaling factor for interaction ratings (lambda) */
  val recencyScalingFactor = calculateRecencyWeight (recentMemoryLength)
  /** A wrapper for this agent used for the explanation generation library */
  val agentID = new ClientAgentIdentifier (this, config, records)

  /** Select a provider to request a service from in the given round, along with explanations of reputation assessments */
  def selectProvider (round: Int): (Provider, List[IJCAIExplanation]) = {
    val sorted = providers.sortBy (calculateTrust (this, _, round, records)).reverse
    def explore (list: List[Provider]): Option[Provider] = list match {
      case head :: tail => ifHappens[Option[Provider]] (explorationProbability) (Some (head)) (explore (tail))
      case Nil => None
    }
    val selected = explore (sorted).getOrElse (sorted.head)
    val explanations = sorted.tail.map (explain (this, sorted.head, _, round))
    (selected, explanations)
  }

  /** Request a service from the given provider in the given round, and record ratings on how the interaction went */
  def requestService (provider: Provider, round: Int): Unit = {
    val service = provider.provideService (this)
    val ratings = toMap (terms) (term => service (term) * termPreferences (term))
    recordInteraction (provider, this, round, ratings)
  }
}
