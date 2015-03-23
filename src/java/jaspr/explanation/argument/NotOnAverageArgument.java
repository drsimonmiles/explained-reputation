/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.explanation.argument;

import jaspr.explanation.AbstractExplanationArgument;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author ingrid
 * 
 */
public class NotOnAverageArgument<O, T> extends AbstractExplanationArgument<O> {

	private Collection<T> consAttributes;
	private Collection<T> prosAttributes;
	private boolean ps;

	public NotOnAverageArgument(O chosenOption, O option) {
		this(chosenOption, option, new LinkedList<T>(), new LinkedList<T>(),
				false);
	}

	public NotOnAverageArgument(O chosenOption, O option,
			Collection<T> prosAttributes, Collection<T> consAttributes,
			boolean ps) {
		super(chosenOption, option);
		this.consAttributes = consAttributes;
		this.prosAttributes = prosAttributes;
		this.ps = ps;
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
		sb.append("Even though ").append(option).append(" is better than ")
				.append(chosenOption).append(" on average, ")
				.append(chosenOption).append(" is preferred to ")
				.append(option).append(" since ").append(chosenOption)
				.append(" is better than ").append(option)
				.append(" on the criteria ");
		appendAttributes(sb, prosAttributes);
		sb.append(" that are important whereas ").append(chosenOption)
				.append(" is worse than ").append(option)
				.append(" on the criteria");
		appendAttributes(sb, consAttributes);
		sb.append(" that are not important.");
		if (ps) {
			sb.append(" Moreover, ").append(chosenOption)
					.append(" is on average better than ").append(option)
					.append(" on the other criteria.");
		}
		return sb.toString();
	}

}
