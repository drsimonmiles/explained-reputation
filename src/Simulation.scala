/** The main application, running simulations */
object Simulation {
  val config = new Configuration (
    NumberOfClients = 50,
    NumberOfProviders = 50,
    ProbabilitySmartProvider = 0.5,
    NumberOfCompetencies = 7,
    NumberOfTerms = 5,
    RecencyScalingPeriodToHalf = 5,
    ClientChangesPerRound = 1)
  val records = new Records
  val network = new Network (config, records)
}
