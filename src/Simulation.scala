/** The main application, running simulations */
object Simulation {
  val config = new Configuration (
    NumberOfClients = 50,
    NumberOfProviders = 50,
    ProbabilitySmartProvider = 0.5,
    PossibleCompetencies = List (-0.9, -0.3, 0.3, 0.9),
    NumberOfTerms = 5,
    MinimumRecentMemoryLength = 5,
    MaximumRecentMemoryLength = 30,
    ClientChangesPerRound = 1)
  val records = new Records
  val network = new Network (config, records)
}
