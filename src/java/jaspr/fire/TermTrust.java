/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.fire;

import jaspr.domain.Agent;
import jaspr.domain.ReputationType;
import jaspr.domain.Term;
import jaspr.util.WeightedSum;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class is an weighted sum associated with a term. It sums a set of trust
 * values (associated with reputation types) considering their weight.
 * 
 * @author ingridnunes
 */
public class TermTrust extends WeightedSum<ReputationType> {

	private final Agent source;
	private final Agent target;
	private final Term term;
	private final Map<ReputationType, TrustValue> trustValues;

	public TermTrust(Agent source, Agent target, Term term, Long currentTime) {
		this.source = source;
		this.target = target;
		this.term = term;
		this.trustValues = new HashMap<>();
		Map<ReputationType, Double> rtPreferences = source.getPreferences()
				.getReputationTypePreferences();
		for (ReputationType rtType : rtPreferences.keySet()) {
			TrustValue trustValueSum = null;
			if (ReputationType.R.equals(rtType)) {
				// Identity function, weight given by parameter
				trustValueSum = new TrustValue(rtType, source.getRatings(
						target, term, rtType));
			} else {
				// Recency function
				trustValueSum = new TrustValue(rtType, source.getPreferences()
						.getLambda(), currentTime, source.getRatings(target,
						term, rtType));
			}
			this.trustValues.put(rtType, trustValueSum);
			Double trustValue = trustValueSum.getWeightedMean();
			this.addValue(rtType, rtPreferences.get(rtType),
					trustValue == null ? 0.0 : trustValue);
		}
	}

	public Term getTerm() {
		return term;
	}

	public TrustValue getTrustValue(ReputationType rt) {
		return trustValues.get(rt);
	}

	public Map<ReputationType, TrustValue> getTrustValues() {
		return trustValues;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		Iterator<ReputationType> keyIt = keySet().iterator();
		while (keyIt.hasNext()) {
			ReputationType key = keyIt.next();
			sb.append(getWeight(key)).append(" * (")
					.append(trustValues.get(key)).append(")");
			if (keyIt.hasNext()) {
				sb.append(" + ");
			}
		}
		return sb.toString();
	}

	@Override
	public String toStringDetailed() {
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString()).append("\n");
		for (ReputationType rt : trustValues.keySet()) {
			sb.append("T_").append(rt).append("(").append(source).append(",")
					.append(target).append(",").append(term).append(") = \n");
			sb.append(trustValues.get(rt)).append("\n");
		}
		return sb.toString();
	}

}
