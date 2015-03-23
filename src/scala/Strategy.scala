/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul, King's College London, and University of Warwick
 */
sealed abstract class Strategy

case object SmartStrategy extends Strategy {
  override val toString = "Smart"
}

case object DumbStrategy extends Strategy {
  override val toString = "Dumb"
}

object Strategy {
  val strategies = List (SmartStrategy, DumbStrategy)
  val fromString = Map ("Smart" -> SmartStrategy, "Dumb" -> DumbStrategy)
}
