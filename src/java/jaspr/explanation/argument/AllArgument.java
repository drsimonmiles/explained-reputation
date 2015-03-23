/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.explanation.argument;

import jaspr.explanation.AbstractExplanationArgument;

/**
 * @author ingrid
 * 
 */
public class AllArgument<O> extends AbstractExplanationArgument<O> {

	public AllArgument(O chosenOption, O option) {
		super(chosenOption, option);
	}

	@Override
	public String getTextualForm() {
		StringBuffer sb = new StringBuffer();
		sb.append(chosenOption).append(" is preferred to ").append(option)
				.append(" since ").append(chosenOption)
				.append(" is better than ").append(option)
				.append(" on ALL criteria.");
		return sb.toString();
	}

}
