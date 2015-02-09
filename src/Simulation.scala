import Explanations.asProvider
import Strategy.strategies

/** One simulation run with a given parameter configuration */
class Simulation (config: Configuration) {
  import config._

  /** The records of client-provider interactions */
  val records = new Records (config)
  /** The simulated network of clients and providers */
  val network = new Network (config, records)

  /** Run the simulation, recording the results for the given x-axis value (or per-round if none) */
  def run (results: Results, x: Option[Int], y: (Network, Strategy) => Double): Unit = {
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
      if (!x.isDefined)
        strategies.map (strategy => results.record (strategy, round, y (network, strategy)))
      (1 to clientChangesPerRound).foreach (_ => network.changeClients ())
    }
    if (x.isDefined)
      strategies.map (strategy => results.record (strategy, x.get, y (network, strategy)))
  }
}
