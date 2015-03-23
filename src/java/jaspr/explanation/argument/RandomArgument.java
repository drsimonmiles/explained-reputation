/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.explanation.argument;

import jaspr.explanation.AbstractExplanationArgument;

/**
 * @author ingridn
 * 
 */
public class RandomArgument<O> extends AbstractExplanationArgument<O> {

	public RandomArgument(O chosenOption, O option) {
		super(chosenOption, option);
	}

	@Override
	public String getTextualForm() {
		StringBuffer sb = new StringBuffer();
		sb.append(option)
				.append(" is exactly as good as ")
				.append(chosenOption)
				.append(", as only one option can be chosen, ")
				.append("the choice among equally good options was made randomly.");
		return sb.toString();
	}

}
