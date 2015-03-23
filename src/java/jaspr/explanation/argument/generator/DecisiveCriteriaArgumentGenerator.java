/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.explanation.argument.generator;

import jaspr.explanation.argument.DecisiveCriteriaArgument;
import jaspr.util.WeightedSum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ingridn
 * 
 */
public class DecisiveCriteriaArgumentGenerator<O, T> {

	public DecisiveCriteriaArgument<O, T> generate(O chosenOption, O option,
			Set<T> keys, WeightedSum<T> bestScore, WeightedSum<T> worstScore) {
		Set<T> decisivePros = generate(keys, bestScore, worstScore,
				getPros(keys, bestScore, worstScore));

		if (decisivePros.isEmpty()) {
			return null;
		} else {
			DecisiveCriteriaArgument<O, T> argument = new DecisiveCriteriaArgument<>(
					chosenOption, option);
			for (T k : decisivePros) {
				argument.addPro(k);
			}
			return argument;

		}
	}

	public Set<T> generate(Set<T> keys, final WeightedSum<T> bestScore,
			final WeightedSum<T> worstScore, double pros) {
		List<T> attMinus = new ArrayList<T>(getAttMinus(keys, bestScore,
				worstScore));
		int attMinusSize = attMinus.size();
		Collections.sort(attMinus, new Comparator<T>() {
			public int compare(T k1, T k2) {
				return getCon(k2, bestScore, worstScore).compareTo(
						getCon(k1, bestScore, worstScore));
			}
		});

		double accumulatedCons = 0;

		int i = 0;
		while (accumulatedCons <= pros && i < attMinusSize) {
			T bLast = attMinus.get(i);
			accumulatedCons += getCon(bLast, bestScore, worstScore);
			i++;
		}

		if (i < attMinusSize) {
			Set<T> d = new HashSet<T>();
			getDecisiveCriteria(bestScore, worstScore, attMinus, pros, i, 0, 0,
					d, new HashSet<T>());
			if (d.size() < attMinusSize)
				return d;
		}

		return new HashSet<T>();
	}

	public Set<T> getAttMinus(Set<T> keys, WeightedSum<T> bestScore,
			WeightedSum<T> worstScore) {
		Set<T> attMinus = new HashSet<>();
		for (T k : keys) {
			if (bestScore.getValue(k) > worstScore.getValue(k)) {
				attMinus.add(k);
			}
		}
		return attMinus;
	}

	public Set<T> getAttPlus(Set<T> keys, WeightedSum<T> bestScore,
			WeightedSum<T> worstScore) {
		Set<T> attPlus = new HashSet<>();
		for (T k : keys) {
			if (bestScore.getValue(k) < worstScore.getValue(k)) {
				attPlus.add(k);
			}
		}
		return attPlus;
	}

	private Double getCon(T k, WeightedSum<T> bestScore,
			WeightedSum<T> worstScore) {
		return worstScore.getWeight(k)
				* (bestScore.getValue(k) - worstScore.getValue(k));
	}

	public double getCons(Set<T> keys, WeightedSum<T> bestScore,
			WeightedSum<T> worstScore) {
		double cons = 0;
		for (T k : keys) {
			if (bestScore.getValue(k) > worstScore.getValue(k)) {
				cons += getCon(k, bestScore, worstScore);
			}
		}
		return cons;
	}

	private boolean getDecisiveCriteria(WeightedSum<T> bestScore,
			WeightedSum<T> worstScore, List<T> attMinus, double pros,
			int cardinality, int currentIndex, double accumulatedCons,
			Set<T> decisiveCriteria, Set<T> currentAttributes) {
		if (currentAttributes.size() == cardinality) {
			if (accumulatedCons <= pros) {
				return true;
			} else {
				decisiveCriteria.addAll(currentAttributes);
				return false;
			}
		} else {
			for (int i = currentIndex; i < attMinus.size(); i++) {
				T k = attMinus.get(i);
				currentAttributes.add(k);

				boolean stop = getDecisiveCriteria(bestScore, worstScore,
						attMinus, pros, cardinality, i + 1, accumulatedCons
								+ getCon(k, bestScore, worstScore),
						decisiveCriteria, currentAttributes);
				currentAttributes.remove(k);
				if (stop)
					return true;
			}
			return false;
		}
	}

	public double getPros(Set<T> keys, WeightedSum<T> bestScore,
			WeightedSum<T> worstScore) {
		double pros = 0;
		for (T k : keys) {
			if (bestScore.getValue(k) < worstScore.getValue(k)) {
				pros += bestScore.getWeight(k)
						* (worstScore.getValue(k) - bestScore.getValue(k));
			}
		}
		return pros;
	}
}
