/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul, King's College London, and University of Warwick
 */

import jaspr.domain.Term
import scala.collection.mutable

/** A log of past agent interactions */
class Records (config: Configuration) {
  import config.recordsPerProvider

  /** The past interactions logged, indexed by the provider */
  var log = mutable.Map[Provider, List[Interaction]] ()

  /** Record that a provider provided a service to a client in a given round, with the client giving ratings per term */
  def recordInteraction (provider: Provider, client: Client, round: Int, ratings: Map[Term, Double]): Unit =
    log (provider) = (new Interaction (client, provider, round, ratings) :: log.getOrElse (provider, Nil)).take (recordsPerProvider)

  /** Retrieve records about a provider as a tuple: (client's own interactions, other witnesses' interactions) */
  def getInteractionRecords (provider: Provider, client: Client): (List[Interaction], List[Interaction]) =
    log.getOrElse (provider, Nil).partition (_.client == client)
}
