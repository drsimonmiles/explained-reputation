import java.lang.Math.log
import jaspr.domain.Term

/** Options for a configuration of a simulation */
case class Configuration (
  /** Number of groups of clients in a network */
  numberOfGroups: Int,
  /** Number of clients per group */
  numberOfClientsPerGroup: Int,
  /** Number of providers in a network */
  numberOfProviders: Int,
  /** Probability (roughly, the proportion) of providers that adapt based on reputation explanations */
  smartProviderProbability: Double,
  /** Number of service quality terms */
  numberOfTerms: Int,
  /** Number of possible values between 0.0 and 1.0 that a provider's competence may take */
  possibleCompetencies: List[Double],
  /** Bias in client's weighting of own interactions over peers': 0.0 means no bias, 1.0 means peer weighting always zero */
  ownInteractionsBias: Double,
  /** Minimum period for which a client remembers recent interactions */
  minimumRecentMemoryLength: Int,
  /** Maximum period for which a client remembers recent interactions */
  maximumRecentMemoryLength: Int,
  /** Number of ratings kept per provider, to limit memory usage and processing time */
  recordsPerProvider: Int,
  /** Probability that the most reputable provider is not chosen, then the next most reputation etc, */
  explorationProbability: Double,
  /** The probability that a provider will change competence in a round */
  competenceChangeProbability: Double,
  /** Number of rounds in the simulation */
  numberOfRounds: Int,
  /** The number of old clients leaving and new clients entering a network each round */
  clientChangesPerRound: Int) {

  // Values derived from configuration options

  /** The terms on which a service's quality is judged */
  val terms = List.fill (numberOfTerms) (new Term (Explanations.name))
  /** The client groups */
  val groups = List.fill (numberOfGroups) (new Group (this))
  /** A FIRE reputation assessor component configured on the basis of the above options */
  val assessor = new FIRE (this)
  /** Calculate the recency weighting from a client's memory length */
  def calculateRecencyWeight (recentMemoryLength: Int) =
    -(recentMemoryLength / 2) / log (0.5)
}
