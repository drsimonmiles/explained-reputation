import java.io._
import scala.collection.mutable
import Utilities.{average, standardDeviation}

/**
 * Simulation results table.
 *
 * @param utilities Map of strategy name (String) and round to a list of utilities gained by that strategy at that round in a simulation
 */
class Results (val utilities: mutable.Map[(String, Long), List[Double]]) {
  def this () = this (mutable.Map[(String, Long), List[Double]] ())

  def getRecord (strategy: String, round: Long, simulation: Int): Option[Double] =
    utilities.getOrElse (utilities.keys.head, Nil).drop (simulation).headOption

  def numberOfRounds =
    utilities.keys.map (_._2).max + 1

  /** Return the number of simulations recorded for an arbitrary strategy and round, assuming it will be the same for all */
  def numberOfSimulations =
    utilities.getOrElse (utilities.keys.head, Nil).size

  /** Record a new result into the table */
  def record (strategy: String, round: Long, result: Double): Unit =
    utilities ((strategy, round)) = result :: utilities.getOrElse ((strategy, round), Nil)

  def strategies: Set[String] =
    utilities.keys.map (_._1).toSet

  /** Write out all the results to the configured results file, with one row per simulation, one column per round */
  def writeAll (resultsFile: String): Unit = {
    val out = new PrintWriter (new FileWriter (resultsFile, true))
    for ((strategy, round) <- utilities.keys)
      out.print (strategy + "," + round + "," + utilities ((strategy, round)).mkString (","))
    out.close ()
  }

  // Write the averages and standard deviations out to a file, with one row per round, two columns per strategy (average, std dev)
  def writeAverages (filename: String) {
    val out = new PrintWriter (new FileWriter (filename))
    val strategyList = strategies.toList
    out.print ("Round,")
//    out.println (strategyList.map (s => s + " avg," + s + " std dev").mkString (","))
    out.println (strategyList.map (s => s + " avg").mkString (","))
    for (round <- 0l until numberOfRounds) {
      out.print (round)
      for (strategy <- strategyList) {
        val values = utilities ((strategy, round))
        val mean = average (values)
        val deviation = standardDeviation (values)
        out.print (",")
        out.print (mean)
//        out.print (",")
//        out.print (deviation)
      }
      out.println ()
    }
    out.close ()
  }
}

object Results {
  // Load all the results contained in the given results file
  def load (resultsFile: String) : Results = {
    val results = mutable.Map[(String, Long), List[Double]] ()
    val in = new BufferedReader (new FileReader (resultsFile))

    var line = in.readLine
    while (line != null) {
      val parts = line.split (',').toList
      val strategy = parts.head
      val round = parts.tail.head.toLong
      results ((strategy, round)) = parts.tail.tail.map (_.toDouble)
      line = in.readLine
    }
    in.close ()

    new Results (results)
  }
}
