/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.explanation.argument;

import jaspr.explanation.AbstractExplanationArgument;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ingridnunes
 *
 */
public class DecisiveCriteriaArgument<O, T> extends
		AbstractExplanationArgument<O> {

	private List<T> cons;
	private List<T> pros;

	public DecisiveCriteriaArgument(O chosenOption, O option) {
		this(chosenOption, option, new LinkedList<T>(), new LinkedList<T>());
	}

	public DecisiveCriteriaArgument(O chosenOption, O option, List<T> pros,
			List<T> cons) {
		super(chosenOption, option);
		this.pros = pros;
		this.cons = cons;
	}

	public void addCon(T term) {
		this.cons.add(term);
	}

	public void addPro(T term) {
		this.pros.add(term);
	}

	public List<T> getCons() {
		return cons;
	}

	public List<T> getPros() {
		return pros;
	}

	@Override
	public String getTextualForm() {
		StringBuffer sb = new StringBuffer();
		sb.append(chosenOption).append(" has a better reputation than ")
				.append(option).append(" mainly due to ");
		appendAttributes(sb, pros);
		if (!cons.isEmpty()) {
			sb.append(", even though ").append(option)
					.append(" provides better ");
			appendAttributes(sb, cons);
		}
		sb.append(".");
		return sb.toString();
	}

}
