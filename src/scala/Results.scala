/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul, King's College London, and University of Warwick
 */

import java.io._
import scala.collection.mutable
import Strategy.{strategies, fromString}
import Utilities.{average, standardDeviation}

/**
 * Simulation results table.
 *
 * @param results Map of strategy name (String) and x-axis value to a list of utilities gained by that strategy at
 *                  that x-axis value in a simulation
 */
class Results (val results: mutable.Map[(Strategy, Int), List[Double]]) {
  def this () = this (mutable.Map[(Strategy, Int), List[Double]] ())

  /** Values of the x-axis parameter recorded */
  def xValues =
    results.keys.map (_._2).toList.sorted

  /** Return the number of simulations recorded for an arbitrary strategy and x-axis value, assuming it will be the same for all */
  def numberOfSimulations =
    results.getOrElse (results.keys.head, Nil).size

  /** Record a new result into the table */
  def record (strategy: Strategy, x: Int, y: Double): Unit =
    results ((strategy, x)) = y :: results.getOrElse ((strategy, x), Nil)

  /** Write out all the results to the configured results file, with one row per simulation, one column per round */
  def writeRecords (resultsFile: String): Unit = {
    val out = new PrintWriter (new FileWriter (resultsFile, true))
    for ((strategy, x) <- results.keys)
      out.println (strategy + "," + x + "," + results ((strategy, x)).mkString (","))
    out.close ()
  }

  // Write the averages and standard deviations out to a file, with one row per round, two columns per strategy (average, std dev)
  def writeAverages (filename: String) {
    val out = new PrintWriter (new FileWriter (filename))
    val strategyList = strategies.toList
    out.print ("X,")
    out.println (strategyList.map (s => s + " avg," + s + " std dev").mkString (","))
//    out.println (strategyList.map (s => s + " avg").mkString (","))
    for (x <- xValues) {
      out.print (x)
      for (strategy <- strategyList) {
        val values = results ((strategy, x))
        val mean = average (values)
        val deviation = standardDeviation (values)
        out.print (",")
        out.print (mean)
        out.print (",")
        out.print (deviation)
      }
      out.println ()
    }
    out.close ()
  }
}

object Results {
  // Load all the results contained in the given results file
  def load (resultsFile: String) : Results = {
    val results = mutable.Map[(Strategy, Int), List[Double]] ()
    val in = new BufferedReader (new FileReader (resultsFile))

    var line = in.readLine
    while (line != null) {
      val parts = line.split (',').toList
      val strategy = fromString (parts.head)
      val x = parts.tail.head.toInt
      results ((strategy, x)) = parts.tail.tail.map (_.toDouble)
      line = in.readLine
    }
    in.close ()

    new Results (results)
  }
}
