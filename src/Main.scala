/** Main class, run simulations and record results */
object Main extends App {
  val config = new Configuration (
    numberOfGroups = 5,
    numberOfClientsPerGroup = 10,
    numberOfProviders = 50,
    smartProviderProbability = 0.5,
    possibleCompetencies = List (-0.9, -0.3, 0.3, 0.9),
    numberOfTerms = 20,
    minimumRecentMemoryLength = 6,
    maximumRecentMemoryLength = 30,
    recordsPerProvider = 40,
    competenceChangeProbability = 0.05,
    explorationProbability = 0.2,
    numberOfRounds = 200,
    clientChangesPerRound = 0)
  val results = new Results ()
  val numberOfSimulations = 16

  (1 to numberOfSimulations).par.foreach (_ => new Simulation (config).run (results))

  val resultsFile = "results2.csv"
  results.writeAverages (resultsFile)
  println ("Finished")
}
