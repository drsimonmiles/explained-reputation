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
public class TradeOffResolutionArgumentGenerator<O, T> {

	private final DecisiveCriteriaArgumentGenerator<O, T> decisiveCriteriaPattern;

	public TradeOffResolutionArgumentGenerator(
			DecisiveCriteriaArgumentGenerator<O, T> decisiveCriteriaPattern) {
		this.decisiveCriteriaPattern = decisiveCriteriaPattern;
	}

	public DecisiveCriteriaArgument<O, T> generate(O chosenOption, O option,
			Set<T> keys, final WeightedSum<T> bestScore,
			final WeightedSum<T> worstScore) {
		DecisiveCriteriaArgument<O, T> argument = new DecisiveCriteriaArgument<>(
				chosenOption, option);

		List<T> attPlus = new ArrayList<T>(decisiveCriteriaPattern.getAttPlus(
				keys, bestScore, worstScore));
		Collections.sort(attPlus, new Comparator<T>() {
			public int compare(T k1, T k2) {
				return getPro(k1, bestScore, worstScore).compareTo(
						getPro(k2, bestScore, worstScore));
			}
		});
		double remainingPros = decisiveCriteriaPattern.getPros(keys, bestScore,
				worstScore);

		Set<T> p = new HashSet<>();
		while (p.isEmpty() && !attPlus.isEmpty()) {
			T bLast = attPlus.remove(attPlus.size() - 1);
			remainingPros -= getPro(bLast, bestScore, worstScore);
			argument.addCon(bLast);
			p = decisiveCriteriaPattern.generate(keys, bestScore, worstScore,
					remainingPros);
		}

		if (p.isEmpty()) {
			for (T k : decisiveCriteriaPattern.getAttMinus(keys, bestScore,
					worstScore)) {
				argument.addPro(k);
			}
		} else {
			for (T k : p) {
				argument.addPro(k);
			}
		}

		return argument;
	}

	private Double getPro(T k, WeightedSum<T> bestScore,
			WeightedSum<T> worstScore) {
		return bestScore.getWeight(k)
				* (worstScore.getValue(k) - bestScore.getValue(k));
	}

}
