/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.explanation.argument.generator;

import jaspr.explanation.argument.NotOnAverageArgument;
import jaspr.util.WeightedSum;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author ingridnunes
 *
 */
public class NotOnAverageArgumentGenerator<O, T> {

	public NotOnAverageArgument<O, T> generate(O chosenOption, O option,
			Set<T> keys, WeightedSum<T> bestScore, WeightedSum<T> worstScore) {
		if (bestScore.getMean() >= worstScore.getMean()) {
			return null;
		}

		Collection<T> prosS = new LinkedList<T>();
		Collection<T> consS = new LinkedList<T>();
		double restSum = 0;

		if (keys != null && !keys.isEmpty()) {
			double w_avg = 1.0 / keys.size();

			for (T k : keys) {
				Double weight = bestScore.getWeight(k);
				Double valueBest = bestScore.getValue(k);
				Double valueWorst = worstScore.getValue(k);

				if (valueBest > valueWorst) {
					if (weight > w_avg) {
						prosS.add(k);
					} else {
						restSum += (valueBest - valueWorst);
					}
				} else if (valueBest < valueWorst) {
					if (weight < w_avg) {
						consS.add(k);
					} else {
						restSum -= (valueWorst - valueBest);
					}
				}
			}
		}

		return new NotOnAverageArgument<O, T>(chosenOption, option, prosS,
				consS, restSum > 0);
	}

}
