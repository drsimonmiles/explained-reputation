import jaspr.domain.{ReputationType, Term}

class ReactionStrategy (network: Network) {
  import network.clients

  def improve (): (List[Client], Map[Client, Map[Term, Double]]) = {
    val finalTailoring: Map[Client, Map[Term, Double]] = improvedTailoring ::: sameTailoring ::: reducedTailoring
    val finalReputation: Map[Client, Double] = improvedReputation ::: sameReputation ::: reducedReputation
    val offer = clients.filter (finalReputation (_) >= 0.0)

    (offer, finalTailoring)
  }

  def estimateRatings (client: Client, offer: Map[Term, Double],
                       preferences: Map[Term, Double], competence: Double): Map[Term, Double] = {

  }

  def estimateReputationIncrease (client: Client, ratings: Map[Term, Double], selectionLikelihood: Double,
                                  repTypeWeights: Map[Client, Map[ReputationType, Double]],
                                  recencyWeights: Map[Client, Double]): Double = {

  }

  def estimateSelectionLikelihood (client: Client, provider: Provider,
                                   recentSelections: Iterable[Provider], exploration: Double): Double = {

  }
}
