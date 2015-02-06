object Main extends App {
  val config = new Configuration (
    NumberOfGroups = 5,
    NumberOfClientsPerGroup = 10,
    NumberOfProviders = 50,
    SmartProviderProbability = 0.5,
    PossibleCompetencies = List (-0.9, -0.3, 0.3, 0.9),
    NumberOfTerms = 20,
    MinimumRecentMemoryLength = 6,
    MaximumRecentMemoryLength = 30,
    RecordsPerProvider = 40,
    CompetenceChangeProbability = 0.05,
    ExplorationProbability = 0.2,
    NumberOfRounds = 300,
    ClientChangesPerRound = 0)
  val results = new Results ()
  val numberOfSimulations = 20

  (1 to numberOfSimulations).par.foreach (_ => new Simulation (config).run (results))

  val resultsFile = "results1.csv"
  results.writeAverages (resultsFile)
}
