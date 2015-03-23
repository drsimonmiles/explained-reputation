/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.explanation.argument.generator;

import jaspr.explanation.argument.DecisiveCriteriaArgument;
import jaspr.util.WeightedSum;

import java.util.Set;

/**
 * @author ingridn
 * 
 */
public class DominationArgumentGenerator<O, T> {

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

	public DecisiveCriteriaArgument<O, T> generate(O chosenOption, O option,
			Set<T> keys, WeightedSum<T> bestScore, WeightedSum<T> worstScore) {
		DecisiveCriteriaArgument<O, T> argument = new DecisiveCriteriaArgument<>(
				chosenOption, option);

		double weightMean = 1 / (double) keys.size();

		double varMean = 0.0;
		for (T k : keys) {
			varMean += Math.abs(bestScore.getValue(k) - worstScore.getValue(k));
		}
		varMean /= (double) keys.size();

		double weightedVarMean = weightMean * varMean;

		for (T k : keys) {
			double weightedVar = bestScore.getWeight(k)
					* Math.abs(bestScore.getValue(k) - worstScore.getValue(k));
			if (weightedVar > weightedVarMean) {
				argument.addPro(k);
			}
		}

		// If there is no decisive criteria
		if (argument.getPros().isEmpty()) {
			argument.getPros().addAll(keys);
		}

		return argument;
	}

}
