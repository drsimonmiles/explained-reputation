import scala.util.Random
import Utilities.toMap

/** Singleton utility functions based on random selection */
object Chooser extends Random {
  /** Selects a random value from a parameter list (throws exception IndexOutOfBoundsException if the list is empty) */
  def choose[V] (items: V*): V =
    items (nextInt (items.size))

  /** Selects a random value from a list (throws exception IndexOutOfBoundsException if the list is empty) */
  def chooseFrom[V] (all: Seq[V]): V =
    all (nextInt (all.size))

  /** Create a random set of values which add to 1.0, mapped from a set of keys */
  def distributionSummingToOne[K] (keys: List[K]): Map[K, Double] =
    keys.zip (distributionSummingToOne (keys.size)).toMap

  /** Create a random set of values which add to 1.0, mapped from a set of keys, with biases to certain keys */
  def distributionSummingToOne[K] (keys: List[K], bias: Map[K, Double]): Map[K, Double] = {
    val initial = keys.map (randomDouble (0.0, 1.0) + bias (_))
    val total = initial.sum
    keys.zip (initial.map (_ / total)).toMap
  }

  /** Create a random list of a given length of values which add to 1.0 */
  def distributionSummingToOne (length: Int): List[Double] = {
    val initial = List.fill (length) (randomDouble (0.0, 1.0))
    val total = initial.sum
    initial.map (_ / total)
  }

  /** Choose true or false based on a probability */
  def flip (probability: Double) =
    nextDouble <= probability

  /** Tests a random number against the given probability, executing ifMet if passed, ifNotMet if not */
  def ifHappens[V] (probability: Double) (ifMet: => V) (ifNotMet: => V): V =
    if (flip (probability)) ifMet else ifNotMet

  /** Tests a random number against the given probability, returning Some (ifMet) if passed, None if not */
  def getIfHappens[V] (probability: Double) (ifMet: => V): Option[V] =
    ifHappens[Option[V]] (probability) (Some (ifMet)) (None)

  /** Selects a random double between a minimum and maximum */
  def randomDouble (minimum: Double, maximum: Double) =
    nextDouble * (maximum - minimum) + minimum

  /** Selects a random integer between a minimum (inclusive) and maximum (exclusive) */
  def randomInt (minimum: Int, maximum: Int) =
    nextInt (maximum - minimum) + minimum

  /** Selects a random sample from a list up to a given number (ensuring no duplicates) */
  def randomSubset[V] (all: Seq[V], number: Int): List[V] = number match {
    case n if n <= 0 => Nil
    case n =>
      val shuffled = shuffle (all)
      shuffled.head :: randomSubset (shuffled.tail, number - 1)
  }
}
