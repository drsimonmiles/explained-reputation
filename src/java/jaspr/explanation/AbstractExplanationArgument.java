/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.explanation;

import java.util.Collection;
import java.util.Iterator;

/**
 * This class is an abstract explanation argument. It is associated with the
 * chosen (best) and rejected (worst) options, besides being part of an
 * explanation. The best and worst options may not necessarily be the best and
 * worst agent, because options here may be finer-grained factors considered to
 * make a choice.
 * 
 * @author ingridnunes
 */
public abstract class AbstractExplanationArgument<O> implements
		ExplanationArgument {

	protected final O chosenOption;
	private Explanation explanation;
	protected final O option;

	public AbstractExplanationArgument(Explanation explanation, O chosenOption,
			O option) {
		this.chosenOption = chosenOption;
		this.option = option;
		setExplanation(explanation);
	}

	public AbstractExplanationArgument(O chosenOption, O option) {
		this.chosenOption = chosenOption;
		this.option = option;
		this.explanation = null;
	}

	protected void appendAttributes(StringBuffer sb, Collection<?> terms) {
		Iterator<?> it = terms.iterator();
		if (it.hasNext()) {
			sb.append(it.next());
		}
		while (it.hasNext()) {
			sb.append(", ");
			Object term = it.next();
			if (!it.hasNext()) {
				sb.append("and ");
			}
			sb.append(term);
		}
	}

	public O getChosenOption() {
		return chosenOption;
	}

	public Explanation getExplanation() {
		return explanation;
	}

	public O getOption() {
		return option;
	}

	public abstract String getTextualForm();

	public void setExplanation(Explanation explanation) {
		this.explanation = explanation;
	}

}
