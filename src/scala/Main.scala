/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul, King's College London, and University of Warwick
 */

import java.io.File

/** Main class, run experiments and record results */
object Main extends App {
  /** Perform one experiment, varying one parameter for the x-axis (time is used as x-axis if no parameter is given),
    * saving accumulated records and then saving accumulated averages
    *
    * @param name Experiment name, used for naming results files
    * @param base The initial configuration for first x value (or for all simulations in per-round experiments)
    * @param xAxis If given, it is a tuple of the range of x values and a function to change the configuration for the next x value
    * @param yAxis: Function for calculating y value from given network state for a given strategy
    */
  def experiment (name: String, base: Configuration, numberOfSimulations: Int,
                  xAxis: Option[(Range, (Configuration, Int) => Configuration)])
                 (yAxis: (Network, Strategy) => Double): Unit = {
    val recordsFile = name + "-records.csv"
    val averagesFile = name + "-averages.csv"
    val results = if (new File (recordsFile).exists)
      Results.load (recordsFile)
    else
      new Results ()

    (1 to numberOfSimulations).par.foreach { _ =>
      xAxis match {
        case Some ((range, next)) =>
          var current = base
          range.foreach { x =>
            current = next (current, x)
            new Simulation (current).run (results, Some (x), yAxis)
          }
        case None =>
          new Simulation (base).run (results, None, yAxis)
      }
    }

    results.writeRecords (recordsFile)
    results.writeAverages (averagesFile)
  }

  /** Base configuration corresponding to minimum x-axis value for all experiments */
  val config = new Configuration (
    numberOfGroups = 5,
    numberOfClientsPerGroup = 10,
    numberOfProviders = 50,
    smartProviderProbability = 0.5,
    possibleCompetencies = List (-0.9, -0.3, 0.3, 0.9),
    numberOfTerms = 20,
    ownInteractionsBias = 0.25,
    minimumRecentMemoryLength = 6,
    maximumRecentMemoryLength = 30,
    recordsPerProvider = 40,
    competenceChangeProbability = 0.05,
    explorationProbability = 0.2,
    numberOfRounds = 100,
    clientChangesPerRound = 0)
  // Which experiment to run
  val experiment = 2

  experiment match {
    case 1 =>
      // Run an experiment with a static network, with round as x-axis, total provider utility as y-axis
      experiment ("static-v3", config, 12, None) {
        (network, strategy) => network.getProviders (strategy).map (_.utility).sum   // y axis values
      }
    case 2 =>
      // Run an experiment with different number of client changes as x-axis, total provider utility as y-axis
      experiment ("client-changes-v2", config, 4,
        Some (0 to 16 by 2, {
          (previous: Configuration, changes: Int) => previous.copy (clientChangesPerRound = changes)  // x axis values
        })) {
        (network, strategy) => network.getProviders (strategy).map (_.utility).sum   // y axis values
      }
  }

  println ("Finished")
}
