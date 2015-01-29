/**
 * Explanation of why one provider was chosen by a client over a comparison provider, with term-specific lists of positive and
 * negative points, and in some cases, also a general explanation that recency was a factor
 */
class Explanation (val client, val chosen: Provider, val comparison: Provider, val pros: List[TermExplanation],
                   val cons: List[TermExplanation], val generalRecency: Boolean)

sealed abstract class TermExplanation (val term: Term)
case class PreferredTermExplanation (term: Term) extends TermExplanation (term)
case class ReputationTypeExplanation (term: Term, repType: ReputationType) extends TermExplanation (term)
case class TypedRecencyExplanation (term: Term, repType: ReputationType) extends TermExplanation (term)

sealed abstract class ReputationType
case object IndividualRatings extends ReputationType
case object WitnessRatings extends ReputationType
