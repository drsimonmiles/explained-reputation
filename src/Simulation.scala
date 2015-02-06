import Explanations.asProvider

class Simulation (config: Configuration) {
  import config._

  val records = new Records (config)
  val network = new Network (config, records)

  def run (results: Results): Unit = {
    for (round <- 0 until numberOfRounds) {
      println ("Round " + round)
      val allExplanations =
        network.clients.map { client =>
          val (provider, explanations) = client.selectProvider (round)
          client.requestService (provider, round)
          explanations
        }.flatten
      val failures = allExplanations.groupBy (explanation => asProvider (explanation.getWorstAgent))
      failures.keys.map (provider => provider.improveFromFailures (failures (provider), round))
      network.providers.foreach { provider =>
        results.record (provider.strategyName, round, provider.utility)
        provider.tick (round)
      }
      (1 to clientChangesPerRound).foreach (_ => network.changeClients ())
    }
  }
}
