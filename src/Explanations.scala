import jaspr.domain.{Agent, AgentPreferences, AgentRating, ReputationType, Term}
import jaspr.explanation.ijcai.{IJCAIExplanation, IJCAIExplanationGenerator}
import jaspr.fire.TrustScore
import scala.collection.JavaConverters._

/** Primary access point for the explanation generation library functions */
object Explanations extends IJCAIExplanationGenerator {
	def explain (client: Client, agentA: Provider, agentB: Provider, round: Long): IJCAIExplanation = {
		val agentAScore = new TrustScore (client.agentID, agentA.agentID, round)
		val agentBScore = new TrustScore (client.agentID, agentB.agentID, round)

		generateExplanation (agentAScore, agentBScore).asInstanceOf[IJCAIExplanation]
	}

	/** Generate a new name for a named entity */
	var counter = 0
  def name = {
    counter += 1
    counter.toString
  }

  def asClient (agent: Agent) =
    agent.asInstanceOf[ClientAgentIdentifier].client

  def asProvider (agent: Agent) =
    agent.asInstanceOf[ProviderAgentIdentifier].provider
}

/** Wraps the library agent preferences to access a client's preferences */
class WrappedPreferences (client: Client, config: Configuration) extends AgentPreferences {
  override val getLambda = double2Double (client.recencyScalingFactor)
  override val getReputationTypePreferences = {
		val map = new java.util.HashMap[ReputationType, java.lang.Double] ()
		map.put (ReputationType.I, client.ownInteractionsWeight)
		map.put (ReputationType.W, client.peerInteractionsWeight)
		map.put (ReputationType.C, 0.0)
		map.put (ReputationType.R, 0.0)
		map
	}
	override val getTermPreferences = {
    val map = new java.util.HashMap[Term, java.lang.Double] ()
		for ((t, p) <- client.termPreferences) map.put (t, p)
		map
	}
}

/** Singleton for ratings by reputation types that are unused */
object NoRatings extends java.util.LinkedList[AgentRating] ()

/** Translation for a client agent to the explanations framework */
class ClientAgentIdentifier (val client: Client, config: Configuration, records: Records) extends Agent (Explanations.name) {
	import records.getInteractionRecords

  override val getPreferences = new WrappedPreferences (client, config)
	override def getRatings (target: Agent, term: Term, reputationType: ReputationType) = {
	  val provider = target.asInstanceOf[ProviderAgentIdentifier].provider
		val (ownInteractions, peerInteractions) = getInteractionRecords (provider, client)
    reputationType match {
			case ReputationType.I => ownInteractions.map (_.asRatings (term)).asJava
			case ReputationType.W => peerInteractions.map (_.asRatings (term)).asJava
			case _ => NoRatings
		}
  }
}

/** Translation for a provider agent to the explanations framework */
class ProviderAgentIdentifier (val provider: Provider) extends Agent (Explanations.name) {
}
