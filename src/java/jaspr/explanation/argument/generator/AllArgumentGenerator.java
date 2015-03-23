/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.explanation.argument.generator;

import jaspr.explanation.argument.AllArgument;
import jaspr.util.WeightedSum;

/**
 * @author ingridnunes
 *
 */
public class AllArgumentGenerator<O, T> {

	public boolean dominates(WeightedSum<T> bestScore, WeightedSum<T> worstScore) {
		boolean existsBetter = false;
		boolean existsWorse = false;

		for (T k : bestScore.keySet()) {
			double bestValue = bestScore.getValue(k);
			double worstValue = worstScore.getValue(k);

			if (bestValue > worstValue) {
				existsBetter = true;
			} else if (worstValue > bestValue) {
				existsWorse = true;
			}
		}

		return existsBetter && !existsWorse;
	}

	public AllArgument<O> generate(O chosenOption, O option,
			WeightedSum<T> bestScore, WeightedSum<T> worstScore) {
		if (dominates(bestScore, worstScore)) {
			return new AllArgument<O>(chosenOption, option);
		} else {
			return null;
		}
	}

}
