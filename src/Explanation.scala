sealed abstract class Explanation
case object Successful extends Explanation
case class DueToQuality (quality: Int) extends Explanation
case class DueToRecency (stepsTo50pc: Int) extends Explanation
case class DueToOwnRecords (client: Client) extends Explanation
case class DueToPeerRecords (client: Client) extends Explanation
