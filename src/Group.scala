import Chooser.distributionSummingToOne
import jaspr.domain.Term

/** A group which clients may belong to */
class Group (config: Configuration) {
  /** The average bias of the group towards particular terms */
  val bias: Map[Term, Double] = distributionSummingToOne (config.terms)
}
