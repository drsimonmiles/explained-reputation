/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul, King's College London, and University of Warwick
 */

import Chooser.shuffle
import List.fill

/** A network of client and provider agents */
class Network (config: Configuration, records: Records) {
  import config._

  /** The client agents in the network, allocated in groups */
  var clients: List[Client] =
    groups.map (group => fill (numberOfClientsPerGroup) (new Client (group, config, this, records))).flatten.toList

  /** The provider agents in the network */
  val providers: List[Provider] =
    fill (numberOfProviders) (new Provider (config, this))

  /** A map from strategy type (smart or dumb) to the providers using that strategy */
  val getProviders: Map[Strategy, List[Provider]] =
    providers.groupBy (_.strategy)

  /** Remove a random subset of clients and replace them with a new set */
  def changeClients () {
    val shuffled = shuffle (clients)
    val (removed, retained) = shuffled.splitAt (clientChangesPerRound)
    clients = retained ::: removed.map (old => new Client (old.group, config, this, records))
  }
}
