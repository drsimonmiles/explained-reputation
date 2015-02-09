import jaspr.domain.{ReputationType, Term}
import jaspr.explanation.ijcai.IJCAIExplanation
import scala.collection.mutable
import scala.collection.JavaConverters._
import Chooser.{chooseFrom, flip, ifHappens}
import Explanations.asClient
import Utilities.{createMap, createMutableMap, toMap}

/** A service provider agent */
class Provider (config: Configuration, network: Network) {
  import config._

  /** True if the provider takes into account explanations of past reputation assessments, false otherwise */
  val strategy = ifHappens[Strategy] (smartProviderProbability) (SmartStrategy) (DumbStrategy)
  /** Competence of agent in providing service (varies over time) */
  var competence = chooseFrom (possibleCompetencies)
  /** Change in competence since previous round */
  var competenceChange = 0.0
  /** The offers of the provider per group, initialised to be equal balance across all terms */
  val offers = createMap (groups) (createMutableMap (terms) (1.0 / numberOfTerms))
  /** How much to improve a term of the standard offer or a tailored offer in reaction to reputation explanations */
  val offerImprovement = 0.1
  /** How much to reduce other terms in the standard offer when improving one */
  val offerCompensation = offerImprovement / (numberOfTerms - 1)
  /** The cumulative utility gained by this provider from service provisions */
  var utility = 0.0
  /** The number of improvements made on the basis of explanations this round */
  var improvements = 0
  /** A wrapper for this agent used for the explanation generation library */
  val agentID = new ProviderAgentIdentifier (this)

  def getOffer (client: Client): Map[Term, Double] =
    offers (client.group).toMap

  /** Provide a service to a client, accumulating the utility from doing so */
  def provideService (client: Client): Map[Term, Double] = {
    utility += 1.0
    getOffer (client).mapValues (_ * competence)
  }

  def improveFromFailures (explanations: List[IJCAIExplanation], round: Long): Unit = {
    def improveFromFailure (explanation: IJCAIExplanation): Map[Group, mutable.Map[Term, Int]] = {
      import explanation._

      val client = asClient (getAssessor)
      val changes = createMap (groups)(createMutableMap (terms)(0))
      for (term <- getDecisiveCriteria.getPros.asScala) {
        changes (client.group)(term) += 1
        improvements += 1
      }
      for (term <- terms) {
        val rtExplanations = getReputationType (term)
        if (rtExplanations != null)
          for (repType <- rtExplanations.getPositiveAttributes.asScala)
            repType match {
              case ReputationType.I =>
                changes (client.group)(term) += 1
                improvements += 1
              case ReputationType.W =>
                groups.foreach (group => changes (group)(term) += 1)
                improvements += 1
              case _ =>
            }
        /*if (getRecencyTK (term, ReputationType.I) != null)
        selfIncrement += ((competenceChange + 1.0) * 2 * offerImprovement)
      if (getRecencyTK (term, ReputationType.W) != null && !capabilityRecentlyDecreased (round))
        increaseStandardOffer (term)
      if (getRecency != null && !capabilityRecentlyDecreased (round))
        terms.foreach (increaseTailoring (asClient (getAssessor), _))*/
      }

      changes
    }

    if (strategy == SmartStrategy) {
      // votes will contain, for each group, the votes to improve each term
      val votes = createMap (groups)(mutable.Map[Term, Int] ())
      for {explanation <- explanations
           additions = improveFromFailure (explanation)
           group <- groups
           term <- terms} {
        votes (group)(term) = votes (group).getOrElse (term, 0) + additions (group)(term)
      }
      for (group <- groups) {
        // Improvements to a term in an offer will be proportional to the amount of improvement possible for that term as well as the votes
        val fractionalImprovements = toMap (terms)(term => votes (group)(term) * (1.0 - offers (group)(term)))
        // The total improvements to be made to the offer
        val totalImprovements = fractionalImprovements.values.sum
        // Add the improvements, then normalise so that the total offer equals 1.0
        for (term <- terms)
          offers (group)(term) = (offers (group)(term) + fractionalImprovements (term)) / (totalImprovements + 1.0)
      }
    }
  }

  /** Mark the end of this round, possibly changing the competency of the provider */
  def tick (round: Int): Unit =
    if (flip (competenceChangeProbability)) {
      val lastCompetency = competence
      competence = chooseFrom (possibleCompetencies)
      competenceChange = competence - lastCompetency
    } else {
      competenceChange = 0.0
    }
}
