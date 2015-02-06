import java.lang.Math.log
import jaspr.domain.Term

/** Options for a configuration of a simulation */
class Configuration (
  /** Number of groups of clients in a network */
  val numberOfGroups: Int,
  /** Number of clients per group */
  val numberOfClientsPerGroup: Int,
  /** Number of providers in a network */
  val numberOfProviders: Int,
  /** Probability (roughly, the proportion) of providers that adapt based on reputation explanations */
  val smartProviderProbability: Double,
  /** Number of service quality terms */
  val numberOfTerms: Int,
  /** Number of possible values between 0.0 and 1.0 that a provider's competence may take */
  val possibleCompetencies: List[Double],
  /** Minimum period for which a client remembers recent interactions */
  val minimumRecentMemoryLength: Int,
  /** Maximum period for which a client remembers recent interactions */
  val maximumRecentMemoryLength: Int,
  /** Number of ratings kept per provider, to limit memory usage and processing time */
  val recordsPerProvider: Int,
  /** Probability that the most reputable provider is not chosen, then the next most reputation etc, */
  val explorationProbability: Double,
  /** The probability that a provider will change competence in a round */
  val competenceChangeProbability: Double,
  /** Number of rounds in the simulation */
  val numberOfRounds: Int,
  /** The number of old clients leaving and new clients entering a network each round */
  val clientChangesPerRound: Int) {

  // Values derived from configuration options

  /** The client group numbers */
  val groups = (1 to numberOfGroups).toList
  /** The terms on which a service's quality is judged */
  val terms = List.fill (numberOfTerms) (new Term (Explanations.name))
  /** A FIRE reputation assessor component configured on the basis of the above options */
  val assessor = new FIRE (this)
  /** Calculate the recency weighting from a client's memory length */
  def calculateRecencyWeight (recentMemoryLength: Int) =
    -(recentMemoryLength / 2) / log (0.5)
}
