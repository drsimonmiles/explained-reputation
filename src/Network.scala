class Network (configuration: Configuration) {
  import configuration._
  val clients = List.fill (NumberOfClients) (new Client (configuration, this))
  val providers = List.fill (NumberOfProviders) (new Provider (configuration, this))
}
