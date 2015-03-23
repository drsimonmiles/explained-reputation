/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.explanation.argument;

import jaspr.explanation.AbstractExplanationArgument;

/**
 * @author ingridn
 * 
 */
public class DominationArgument<O> extends AbstractExplanationArgument<O> {

	public DominationArgument(O chosenOption, O option) {
		super(chosenOption, option);
	}

	@Override
	public String getTextualForm() {
		StringBuffer sb = new StringBuffer();
		sb.append("There is no reason to choose ")
				.append(option)
				.append(", as ")
				.append(chosenOption)
				.append("  is better than it in all aspects that you consider in your preferences.");
		return sb.toString();
	}

}