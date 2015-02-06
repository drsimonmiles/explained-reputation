import jaspr.domain.{Term, AgentRating}

class Interaction (val client: Client, val provider: Provider, val round: Long, val ratings: Map[Term, Double]) {
  val asRatings = ratings.keys.map (term => (term, new AgentRating (client.agentID, provider.agentID, term, ratings (term), round, 1.0))).toMap
  override val toString = ratings.toString
}
