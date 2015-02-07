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
