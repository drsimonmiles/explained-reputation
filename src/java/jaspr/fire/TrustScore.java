/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.fire;

import jaspr.domain.Agent;
import jaspr.domain.Term;
import jaspr.util.WeightedSum;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class is an weighted sum associated with an agent, given the overall
 * trust score of an agent target, for an agent source. It sums a set of term
 * trusts considering their weight.
 * 
 * @author ingridnunes
 *
 */
public class TrustScore extends WeightedSum<Term> {

	private final Agent source;
	private final Agent target;
	private final Map<Term, TermTrust> termTrusts;

	public TrustScore(Agent source, Agent target, Long currentTime) {
		this.source = source;
		this.target = target;

		this.termTrusts = new HashMap<>();
		Map<Term, Double> termPreferences = source.getPreferences()
				.getTermPreferences();
		for (Term term : termPreferences.keySet()) {
			TermTrust termTrust = new TermTrust(source, target, term,
					currentTime);
			this.termTrusts.put(term, termTrust);
			this.addValue(term, termPreferences.get(term),
					termTrust.getWeightedMean());
		}
	}

	public Agent getSource() {
		return source;
	}

	public Agent getTarget() {
		return target;
	}

	public TermTrust getTermTrust(Term term) {
		return termTrusts.get(term);
	}

	public Map<Term, TermTrust> getTermTrusts() {
		return termTrusts;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("T(").append(source).append(",").append(target)
				.append(") = ");
		Iterator<Term> keyIt = keySet().iterator();
		while (keyIt.hasNext()) {
			Term key = keyIt.next();
			sb.append(getWeight(key)).append(" * (")
					.append(termTrusts.get(key)).append(")");
			if (keyIt.hasNext()) {
				sb.append(" + ");
			}
		}
		return sb.toString();
	}

	@Override
	public String toStringDetailed() {
		StringBuffer sb = new StringBuffer();
		sb.append("T(").append(source).append(",").append(target)
				.append(") = \n");
		sb.append(super.toString()).append("\n\n");
		for (Term t : termTrusts.keySet()) {
			sb.append("T(").append(source).append(",").append(target)
					.append(",").append(t).append(") = \n");
			sb.append(termTrusts.get(t)).append("\n\n");
		}
		return sb.toString();
	}

}
