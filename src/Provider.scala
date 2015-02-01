import jaspr.domain.{Agent, ReputationType, Term}
import jaspr.explanation.ijcai.IJCAIExplanation
import scala.collection.mutable
import Chooser.{chooseFrom, flip}
import Utilities.{createMutableMap, toMap}

/** A service provider agent */
class Provider (config: Configuration, network: Network) {
  import config.{NumberOfTerms, PossibleCompetencies, Terms, RecencyScalingPeriodToHalf, ProbabilitySmartProvider}

  /** True if the provider takes into account explanations of past reputation assessments, false otherwise */
  val smart = flip (ProbabilitySmartProvider)
  /** Competence of agent in providing service (varies over time) */
  var competency = chooseFrom (PossibleCompetencies)
  /** Last round at which the provider's competency decreased */
  var lastCompetencyDecrease = Long.MinValue
  /** The standard offer of the provider, initialised to be equal balance across all terms */
  val offer = createMutableMap (Terms) (1.0 / NumberOfTerms)
  /** Tailoring applied by the provider for each service quality term for each client, initially 0.0 for all
    * (read this variable using the getTailoring method, not directly) */
  val tailoring = mutable.Map[(Client, Term), Double] ()
  /** How much to improve a term of the standard offer or a tailored offer in reaction to reputation explanations */
  val offerImprovement = 0.1
  /** How much to reduce other terms in the standard offer when improving one */
  val offerCompensation = offerImprovement / (NumberOfTerms - 1)
  /** The cumulative utility gained by this provider from service provisions */
  var utility = 0.0
  /** A wrapper for this agent used for the explanation generation library */
  val agentID = new ProviderAgentIdentifier (this)

  /** Calculate the adjustments to make to the standard offer in tailoring to a given client */
  def getAdjustments (client: Client): Map[Term, Double] =
    toMap (Terms) (term => (client.termPreferences (term) - offer (term)) * getTailoring (client, term))

  /** Calculate the offer that is made to the given client accounting for client-specific tailoring */
  def getOffer (client: Client): Map[Term, Double] = {
    val adjusts = getAdjustments (client)
    toMap (Terms) (term => adjusts (term) + offer (term))
  }

  /** Retrieve the current tailoring for the given client and term */
  def getTailoring (client: Client, term: Term) =
    tailoring.getOrElse ((client, term), 0.0)

  /** Provide a service to a client, accumulating the utility from doing so */
  def provideService (client: Client): Map[Term, Double] = {
    utility += 1.0 - getAdjustments (client).values.map (_.abs).sum
    getOffer (client).mapValues (_ * competency)
  }

  def asClient (agent: Agent) =
    agent.asInstanceOf[ClientAgentIdentifier].client

  def improve (explanation: SelectionExplanation) {
    if (explanation.rejected == this)

  }

  def improveFromFailure (explanation: IJCAIExplanation, round: Long) {
    import explanation._
    for (term <- getDecisiveCriteria.getPros)
      increaseStandardOffer (term)
    for (term <- Terms) {
      val rtExplanations = getReputationType (term)
      if (rtExplanations != null)
        for (repType <- rtExplanations.getPositiveAttributes)
          repType match {
            case ReputationType.I =>
              increaseTailoring (asClient (getAssessor), term)
            case ReputationType.W =>
              increaseStandardOffer (term)
          }
      if (getRecencyTK (term, ReputationType.I) != null && !capabilityRecentlyDecreased (round))
        increaseTailoring (asClient (getAssessor), term)
      if (getRecencyTK (term, ReputationType.W) != null && !capabilityRecentlyDecreased (round))
        increaseStandardOffer (term)
      if (getRecency != null && !capabilityRecentlyDecreased (round))
        Terms.foreach (increaseTailoring (asClient (getAssessor), _))
    }
  }

  def improveFromSuccess (explanation: IJCAIExplanation, round: Long) {
    import explanation._
    for (term <- getDecisiveCriteria.getPros)
      decreaseTailoring (asClient (getAssessor), term)
    for (term <- Terms)
      if (getReputationType (term) != null || getRecencyTK (term, ReputationType.I) != null || getRecencyTK (term, ReputationType.W) != null)
        decreaseTailoring (asClient (getAssessor), term)
  }

  /** Increase the standard offer for a given term */
  def increaseStandardOffer (term: Term) {
    if (offer (term) <= 1.0 - offerImprovement && Terms.forall (t => t == term || offer (term) >= offerCompensation)) {
      offer (term) += offerImprovement
      for (other <- Terms if other != term)
        offer (term) -= offerCompensation
    }
  }

  /** Increase the tailoring for a given client and term */
  def increaseTailoring (client: Client, term: Term) {
    if (getTailoring (client, term) <= 1.0 - offerImprovement)
      tailoring ((client, term)) = getTailoring (client, term) + offerImprovement
  }

  /** Decrease the tailoring for a given client and term */
  def decreaseTailoring (client: Client, term: Term) {
    if (tailoring ((client, term)) >= offerImprovement)
      tailoring ((client, term)) = getTailoring (client, term) + offerImprovement
  }

  /** Calculate whether the provider's competence has recently decreased */
  def capabilityRecentlyDecreased (round: Long) =
    lastCompetencyDecrease >= round - RecencyScalingPeriodToHalf

  /** Change the competence of the provider to a new random value */
  def changeCompetency (round: Int) {
    val lastCompetency = competency
    competency = chooseFrom (PossibleCompetencies)
    if (competency < lastCompetency)
      lastCompetencyDecrease = round
  }
}
