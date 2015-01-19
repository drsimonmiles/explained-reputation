import Utilities.toMap

class Configuration (
  val NumberOfClients: Int,
  val NumberOfProviders: Int,
  val NumberOfQualities: Int,
  val NumberOfCompetencies: Int) {

  // Values derived from configuration options
  val PossibleCompetencies = (0 until NumberOfCompetencies).map (_.toDouble / (NumberOfCompetencies - 1))
  val Qualities = 1 to NumberOfQualities
  val QualityExplanations = toMap (Qualities) (DueToQuality)
}
