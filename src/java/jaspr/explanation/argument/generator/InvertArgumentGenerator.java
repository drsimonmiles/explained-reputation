/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.explanation.argument.generator;

import jaspr.explanation.argument.InvertArgument;
import jaspr.util.Pair;
import jaspr.util.WeightedSum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author ingrid
 * 
 */
public class InvertArgumentGenerator<O, T> {

	private Log log = LogFactory.getLog(getClass());

	public InvertArgument<O, T> generate(O chosenOption, O option, Set<T> keys,
			WeightedSum<T> bestScore, WeightedSum<T> worstScore) {
		long init = System.currentTimeMillis();
		log.debug("Invert started.");
		List<T> permutation = getInvert(keys, bestScore, worstScore);
		log.debug("Invert ended: "
				+ ((System.currentTimeMillis() - init) / 1000) + "s.");

		if (permutation != null) {
			double w_avg = 1.0 / keys.size();
			Collection<T> c_nrw = new HashSet<T>();
			Collection<T> c_nw = new HashSet<T>();
			Collection<T> c_prs = new HashSet<T>();
			Collection<T> c_ps = new HashSet<T>();
			List<Pair<T>> c_pn = new LinkedList<Pair<T>>();

			T i = permutation.get(permutation.size() - 1);
			for (T j : permutation) {
				log.debug("Analysing pair (i, j) = (" + i + ", " + j + ")");

				double var_i = bestScore.getValue(i) - worstScore.getValue(i);
				double var_j = bestScore.getValue(j) - worstScore.getValue(j);
				double w_i = bestScore.getWeight(i);
				double w_j = bestScore.getWeight(j);

				log.debug("var_i = " + var_i + " ; var_j = " + var_j
						+ " ; w_i = " + w_i + "; w_j = " + w_j);

				// i in A- and j in A+
				if (var_i < 0 && var_j > 0 && w_i < w_j) {
					log.debug("i in A- and j in A+ and w_i < w_j");
					if (w_j >= w_avg && w_i <= w_avg) {
						c_ps.add(j);
						c_nw.add(i);
						log.debug("j in c_ps and i in c_nw");
					} else {
						c_pn.add(new Pair<T>(i, j));
						log.debug("(i,j) in c_pn");
					}
				} else
				// i in A+ and j in A+
				if (var_i > 0 && var_j > 0 && var_j > var_i && w_i < w_j) {
					log.debug("i in A+ and j in A+ and var_j > var_i and w_i < w_j");
					if (w_j >= w_avg) {
						c_ps.add(j);
						log.debug("j in c_ps");
					} else {
						c_prs.add(j);
						log.debug("j in c_prs");
					}
				} else
				// i in A- and j in A-
				if (var_i < 0 && var_j < 0 && var_j > var_i && w_i < w_j) {
					log.debug("i in A- and j in A- and var_j > var_i and w_i < w_j");
					if (w_j <= w_avg) {
						c_nw.add(j);
						log.debug("j in c_nw");
					} else {
						c_nrw.add(j);
						log.debug("j in c_nrw");
					}
				} else {
					log.debug("---> Pair not added!");
				}

				i = j;
			}

			return new InvertArgument<O, T>(chosenOption, option, c_ps, c_prs,
					c_nw, c_nrw, c_pn);
		}

		return null;
	}

	private List<T> getInvert(Set<T> keys, WeightedSum<T> bestScore,
			WeightedSum<T> worstScore) {
		List<T> best = new ArrayList<T>();
		Double bestDiff = null;

		List<List<T>> toProcess = new ArrayList<List<T>>();
		toProcess.add(new ArrayList<T>());

		while (!toProcess.isEmpty()) {
			List<T> added = toProcess.get(0);
			toProcess.remove(0);

			List<T> left = new ArrayList<T>(keys);
			left.removeAll(added);

			for (T newAtt : left) {
				List<T> newAdded = new ArrayList<T>(added.size() + 1);
				newAdded.addAll(added);
				newAdded.add(newAtt);

				if (newAdded.size() > 1) {
					double newCostCO = getPermutatedCost(keys, bestScore,
							newAdded);
					double newCostRO = getPermutatedCost(keys, worstScore,
							newAdded);
					double diff = Math.abs(newCostCO - newCostRO);

					if (newCostCO < newCostRO) { // Candidate
						if ((bestDiff == null)
								|| (newAdded.size() < best.size() || (newAdded
										.size() == best.size() && diff > bestDiff))) {
							best.clear();
							best.addAll(newAdded);
							bestDiff = diff;
							log.debug("New best: " + best + " (size = "
									+ best.size() + ", diff = " + bestDiff
									+ ")");
						}
					}
				}

				toProcess.add(newAdded);
			}

			if (!best.isEmpty()) {
				log.trace("Best permutation: " + best);
				return best;
			}
		}

		log.trace("No invert");
		return null;
	}

	private double getPermutatedCost(Set<T> keys, WeightedSum<T> score,
			List<T> permutations) {
		double cost = 0;
		for (T att : keys) {
			Double attValue = score.getValue(att);
			int indexOfAtt = permutations.indexOf(att);
			if (indexOfAtt != -1) {
				int nextAtt = indexOfAtt == (permutations.size() - 1) ? 0
						: indexOfAtt + 1;
				Double nextAttWeight = score.getWeight(permutations
						.get(nextAtt));
				cost += nextAttWeight * attValue;
			} else {
				cost += score.getWeight(att) * attValue;
			}
		}
		return cost;
	}

}
