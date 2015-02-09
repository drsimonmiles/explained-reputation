import Chooser.distributionSummingToOne
import jaspr.domain.Term

class Group (config: Configuration) {
  val bias: Map[Term, Double] = distributionSummingToOne (config.terms)
}
