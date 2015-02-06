import Chooser.shuffle
import List.fill

/** A network of client and provider agents */
class Network (config: Configuration, records: Records) {
  import config._

  /** The client agents in the network, allocated in groups */
  var clients: List[Client] =
    groups.map (group => fill (NumberOfClientsPerGroup) (new Client (group, config, this, records))).flatten.toList

  /** The provider agents in the network */
  val providers: List[Provider] =
    fill (NumberOfProviders) (new Provider (config, this))

  /** Remove a random subset of clients and replace them with a new set */
  def changeClients () {
    val shuffled = shuffle (clients)
    val (removed, retained) = shuffled.splitAt (ClientChangesPerRound)
    clients = retained ::: removed.map (old => new Client (old.group, config, this, records))
  }
}
