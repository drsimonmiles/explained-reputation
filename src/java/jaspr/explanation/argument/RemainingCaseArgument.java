/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.explanation.argument;

import jaspr.explanation.AbstractExplanationArgument;

import java.util.Collection;

/**
 * @author ingrid
 * 
 */
public class RemainingCaseArgument<O, T> extends AbstractExplanationArgument<O> {

	private Collection<T> consAttributes;
	private Collection<T> prosAttributes;

	public RemainingCaseArgument(O chosenOption, O option,
			Collection<T> prosAttributes, Collection<T> consAttributes) {
		super(chosenOption, option);
		this.consAttributes = consAttributes;
		this.prosAttributes = prosAttributes;
	}

	public Collection<T> getConsAttributes() {
		return consAttributes;
	}

	public Collection<T> getProsAttributes() {
		return prosAttributes;
	}

	@Override
	public String getTextualForm() {
		StringBuffer sb = new StringBuffer();
		sb.append(chosenOption).append(" is preferred to ").append(option)
				.append(" since the intensity of preference ")
				.append(chosenOption).append(" over ").append(option)
				.append(" on ");
		appendAttributes(sb, prosAttributes);
		sb.append(" is larger than the intensity of preference of ")
				.append(option).append(" over ").append(chosenOption)
				.append(" on ");
		appendAttributes(sb, consAttributes);
		sb.append(".");
		return sb.toString();
	}
}
