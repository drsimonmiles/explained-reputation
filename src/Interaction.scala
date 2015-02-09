import jaspr.domain.{Term, AgentRating}

/** A record of a past interaction between a client and provider, with ratings by the client of service received */
class Interaction (val client: Client, val provider: Provider, val round: Int, val ratings: Map[Term, Double]) {
  /** Conversion of the ratings to a form for the explanations library */
  val asRatings = ratings.keys.map (term => (term, new AgentRating (client.agentID, provider.agentID, term, ratings (term), round.toLong, 1.0))).toMap
}
