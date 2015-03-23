/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.explanation.argument.generator;

import jaspr.explanation.ExplanationArgument;
import jaspr.explanation.argument.RandomArgument;
import jaspr.explanation.argument.RemainingCaseArgument;
import jaspr.util.WeightedSum;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author ingridnunes
 *
 */
public class RemainingCaseArgumentGenerator<O, T> {

	public ExplanationArgument generate(O chosenOption, O option, Set<T> keys,
			WeightedSum<T> bestScore, WeightedSum<T> worstScore) {
		Collection<T> pros = new LinkedList<T>();
		Collection<T> cons = new LinkedList<T>();

		for (T k : keys) {
			Double valueBest = bestScore.getValue(k);
			Double valueWorst = worstScore.getValue(k);

			if (valueBest > valueWorst) {
				pros.add(k);
			} else if (valueBest < valueWorst) {
				cons.add(k);
			}
		}

		if (pros.isEmpty() && cons.isEmpty()) {
			return new RandomArgument<O>(chosenOption, option);
		} else {
			return new RemainingCaseArgument<O, T>(chosenOption, option, pros,
					cons);
		}
	}

}
