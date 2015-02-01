import jaspr.domain.Term

/** Options for a configuration of a simulation */
class Configuration (
  /** Number of clients in a network */
  val NumberOfClients: Int,
  /** Number of providers in a network */
  val NumberOfProviders: Int,
  /** Probability (roughly, the proportion) of providers that adapt based on reputation explanations */
  val ProbabilitySmartProvider: Double,
  /** Number of service quality terms */
  val NumberOfTerms: Int,
  /** Number of possible values between 0.0 and 1.0 that a provider's competence may take */
  val PossibleCompetencies: List[Double],
  /** Minimum period for which a client remembers recent interactions */
  val MinimumRecentMemoryLength: Int,
  /** Maximum period for which a client remembers recent interactions */
  val MaximumRecentMemoryLength: Int,
  /** The number of old clients leaving and new clients entering a network each round */
  val ClientChangesPerRound: Int) {

  // Values derived from configuration options

  /** The terms on which a service's quality is judged */
  val Terms = List.fill (NumberOfTerms) (new Term (Explanations.name))
  /** A FIRE reputation assessor component configured on the basis of the above options */
  val Assessor = new FIRE (this)
}
