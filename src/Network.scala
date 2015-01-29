import Chooser.shuffle
import List.fill

/** A network of client and provider agents */
class Network (config: Configuration, records: Records) {
  import config._

  /** The client agents in the network */
  var clients: List[Client] = fill (NumberOfClients) (new Client (config, this, records))
  /** The provider agents in the network */
  val providers: List[Provider] = fill (NumberOfProviders) (new Provider (config, this))

  /** Remove a random subset of clients and replace them with a new set */
  def changeClients () {
    clients = shuffle (clients).take (ClientChangesPerRound) :::
      fill (ClientChangesPerRound) (new Client (config, this, records))
  }
}
