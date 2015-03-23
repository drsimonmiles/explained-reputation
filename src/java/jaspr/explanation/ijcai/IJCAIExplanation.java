/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.explanation.ijcai;

import jaspr.domain.Agent;
import jaspr.domain.ReputationType;
import jaspr.domain.Term;
import jaspr.explanation.Explanation;
import jaspr.explanation.ExplanationArgument;
import jaspr.explanation.argument.DecisiveCriteriaArgument;
import jaspr.explanation.argument.InvertArgument;
import jaspr.explanation.argument.RecencyArgument;
import jaspr.util.PairT;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ingridnunes
 *
 */
public class IJCAIExplanation extends Explanation {

	private DecisiveCriteriaArgument<Agent, Term> decisiveCriteria;
	private RecencyArgument<Agent> recency;
	private Map<PairT<Term, ReputationType>, RecencyArgument<Agent>> recencyTK;
	private Map<Term, InvertArgument<Agent, ReputationType>> reputationType;

	public IJCAIExplanation() {
		this.reputationType = new HashMap<>();
		this.recencyTK = new HashMap<>();
	}

	public IJCAIExplanation(Agent assessor, Agent bestAgent, Agent worstAgent) {
		super(assessor, bestAgent, worstAgent);
		this.reputationType = new HashMap<>();
		this.recencyTK = new HashMap<>();
	}

	public void addRecencyTK(Term t, ReputationType rt,
			RecencyArgument<Agent> recencyTK) {
		PairT<Term, ReputationType> key = new PairT<Term, ReputationType>(t, rt);
		ExplanationArgument oldArgument = this.recencyTK.get(key);
		this.recencyTK.put(key, recencyTK);
		saveUpdateArgument(oldArgument, recencyTK);
	}

	public void addReputationType(Term t,
			InvertArgument<Agent, ReputationType> invert) {
		ExplanationArgument oldArgument = this.reputationType.get(t);
		this.reputationType.put(t, invert);
		saveUpdateArgument(oldArgument, invert);
	}

	private void appendCollection(StringBuffer sb, Collection<?> col) {
		if (!col.isEmpty()) {
			for (Object o : col) {
				sb.append(o).append(", ");
			}
		}
	}

	public DecisiveCriteriaArgument<Agent, Term> getDecisiveCriteria() {
		return decisiveCriteria;
	}

	public RecencyArgument<Agent> getRecency() {
		return recency;
	}

	public Map<PairT<Term, ReputationType>, RecencyArgument<Agent>> getRecencyTK() {
		return recencyTK;
	}

	public RecencyArgument<Agent> getRecencyTK(Term t, ReputationType rt) {
		return this.recencyTK.get(new PairT<Term, ReputationType>(t, rt));
	}

	public Map<Term, InvertArgument<Agent, ReputationType>> getReputationType() {
		return reputationType;
	}

	public InvertArgument<Agent, ReputationType> getReputationType(Term t) {
		return reputationType.get(t);
	}

	@Override
	public String getTextualForm() {
		StringBuffer sb = new StringBuffer();

		sb.append(decisiveCriteria.getTextualForm()).append("\n");

		if (this.recency != null) {
			sb.append(recency.getTextualForm()).append("\n");
		}

		for (Term t : reputationType.keySet()) {
			InvertArgument<Agent, ReputationType> invert = reputationType
					.get(t);
			sb.append("Considering ").append(t).append(", even though ");
			sb.append(invert.getOption()).append(
					" has higher trust value considering ");
			Collection<ReputationType> negative = invert
					.getNegativeAttributes();
			appendCollection(sb, negative);
			sb.append("which ").append(negative.size() > 1 ? "are " : "is ")
					.append("not important, ");
			sb.append(invert.getChosenOption()).append(
					" has has higher trust value considering ");
			Collection<ReputationType> positive = invert
					.getPositiveAttributes();
			appendCollection(sb, positive);
			sb.append("which ").append(positive.size() > 1 ? "are " : "is ")
					.append("important.").append("\n");
		}

		if (!recencyTK.isEmpty()) {
			sb.append("Moreover, ");
		}
		for (PairT<Term, ReputationType> key : recencyTK.keySet()) {
			RecencyArgument<Agent> r = recencyTK.get(key);
			sb.append(r.getOption()).append(
					" has, on average, higher ratings for ");
			sb.append(key.getValue1()).append(" than ")
					.append(r.getChosenOption());
			sb.append(" considering ").append(key.getValue2()).append(", but ");
			sb.append(r.getChosenOption())
					.append(" has been recently receiving higher ratings than ")
					.append(r.getOption());
			sb.append(", which is more valuable.\n");
		}

		return sb.toString();
	}

	private void saveUpdateArgument(ExplanationArgument oldArgument,
			ExplanationArgument newArgument) {
		if (oldArgument != null) {
			removeArgument(oldArgument);
		}
		addArgument(newArgument);
	}

	public void setDecisiveCriteria(
			DecisiveCriteriaArgument<Agent, Term> decisiveCriteria) {
		ExplanationArgument oldArgument = this.decisiveCriteria;
		this.decisiveCriteria = decisiveCriteria;
		saveUpdateArgument(oldArgument, decisiveCriteria);
	}

	public void setRecency(RecencyArgument<Agent> recency) {
		ExplanationArgument oldArgument = recency;
		this.recency = recency;
		saveUpdateArgument(oldArgument, recency);
	}

}
