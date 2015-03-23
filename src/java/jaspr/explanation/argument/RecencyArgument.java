/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.explanation.argument;

import jaspr.explanation.AbstractExplanationArgument;

/**
 * @author ingridnunes
 *
 */
public class RecencyArgument<O> extends AbstractExplanationArgument<O> {

	public RecencyArgument(O chosenOption, O option) {
		super(chosenOption, option);
	}

	@Override
	public String getTextualForm() {
		StringBuffer sb = new StringBuffer();
		sb.append("In addition, ").append(option)
				.append(" has, on average, higher ratings than ")
				.append(chosenOption).append(", but ").append(chosenOption)
				.append(" has been recently receiving higher ratings than ")
				.append(option).append(", which is more valuable.");
		return sb.toString();

	}

}