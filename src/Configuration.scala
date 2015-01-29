import java.lang.Math._

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
  val NumberOfCompetencies: Int,
  /** In recency scaling, the number of rounds before an interaction rating should be half that of the current round */
  val RecencyScalingPeriodToHalf: Int,
  /** The number of old clients leaving and new clients entering a network each round */
  val ClientChangesPerRound: Int) {

  // Values derived from configuration options

  /** Possible competence values of a provider */
  val PossibleCompetencies = (0 until NumberOfCompetencies).map (_.toDouble / (NumberOfCompetencies - 1))
  /** The terms on which a service's quality is judged */
  val Terms = List.fill (NumberOfTerms) (new Term)
  /** FIRE's recency scaling factor for interaction ratings (lambda) */
  val RecencyScalingFactor = -RecencyScalingPeriodToHalf / log (0.5)
  /** A FIRE reputation assessor component configured on the basis of the above options */
  val Assessor = new FIRE (this)
}
