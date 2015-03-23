/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul, King's College London, and University of Warwick
 */
import java.lang.Math.sqrt
import scala.collection.mutable

/** Helpful utility functions */
object Utilities {
  /** Averages a set of numbers */
  def average (values: Iterable[Double]) =
    values.sum / values.size

  /** Create an immutable map where a value is generated independently for each key */
  def createMap[V, W] (keys: Iterable[V]) (createValue: => W): Map[V, W] =
    toMap (keys) (_ => createValue)

  /** Create a mutable map where a value is generated independently for each key */
  def createMutableMap[V, W] (keys: Iterable[V]) (createValue: => W): mutable.Map[V, W] =
    toMutableMap (keys) (_ => createValue)

  /** Standard deviation for a set of numbers */
  def standardDeviation (values: Iterable[Double]) = {
    val mean = average (values)
    sqrt (average (values.map (_ - mean).map (x => x * x)))
  }

  /** Creates an immutable map from a list, with the original list as keys, and values created from the keys */
  def toMap[V, W] (list: Iterable[V]) (createValue: V => W): Map[V, W] =
    list.map (item => (item, createValue (item))).toMap

  /** Creates a mutable map from a list, with the original list as keys, and values created from the keys */
  def toMutableMap[V, W] (list: Iterable[V]) (createValue: V => W): mutable.Map[V, W] =
    mutable.Map[V, W] (toMap (list)(createValue).toSeq: _*)

  /** Weighted sum calculation */
  def weightedSum (values: Iterable[Double], weights: Iterable[Double]): Double =
    values.zip (weights).map (x => x._1 * x._2).sum
}
