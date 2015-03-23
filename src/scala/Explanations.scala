/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul, King's College London, and University of Warwick
 */

import java.util

import jaspr.domain.{Agent, AgentPreferences, AgentRating, ReputationType, Term}
import jaspr.explanation.ijcai.{IJCAIExplanation, IJCAIExplanationGenerator}
import jaspr.fire.TrustScore
import scala.collection.JavaConverters._

/** Primary access point for the explanation generation library functions */
object Explanations extends IJCAIExplanationGenerator {
  /** Generate explanations of why the given client preferred one provider to another in the current round */
	def explain (client: Client, betterAgent: Provider, worseAgent: Provider, round: Long): IJCAIExplanation = {
		val betterScore = new TrustScore (client.agentID, betterAgent.agentID, round)
		val worseScore = new TrustScore (client.agentID, worseAgent.agentID, round)

		generateExplanation (betterScore, worseScore).asInstanceOf[IJCAIExplanation]
	}

	/** Generate a new name for a named entity */
	var counter = 0
  def name: String = {
    counter += 1
    counter.toString
  }

  /** Get the Client object for the given Agent object */
  def asClient (agent: Agent) =
    agent.asInstanceOf[ClientAgentIdentifier].client

  /** Get the Provider object for the given Agent object */
  def asProvider (agent: Agent) =
    agent.asInstanceOf[ProviderAgentIdentifier].provider
}

/** Wraps the library agent preferences to access a client's preferences */
class WrappedPreferences (client: Client, config: Configuration) extends AgentPreferences {
  override val getLambda = new java.lang.Double (client.recencyScalingFactor)
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

/** Translation for a client agent to the explanations framework */
class ClientAgentIdentifier (val client: Client, config: Configuration, records: Records) extends Agent (Explanations.name) {
	import records.getInteractionRecords

  override val getPreferences = new WrappedPreferences (client, config)

  /** Singleton for ratings by reputation types that are unused */
  val noRatings = new util.LinkedList[AgentRating]()

  /** Return ratings of the target provider for the given term and reputation type */
	override def getRatings (target: Agent, term: Term, reputationType: ReputationType) = {
	  val provider = target.asInstanceOf[ProviderAgentIdentifier].provider
		val (ownInteractions, peerInteractions) = getInteractionRecords (provider, client)
    reputationType match {
			case ReputationType.I => ownInteractions.map (_.asRatings (term)).asJava
			case ReputationType.W => peerInteractions.map (_.asRatings (term)).asJava
			case _ => noRatings
		}
  }
}

/** Translation for a provider agent to the explanations framework */
class ProviderAgentIdentifier (val provider: Provider) extends Agent (Explanations.name) {}
