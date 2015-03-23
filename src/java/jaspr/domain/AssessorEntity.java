/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ingridnunes
 */
public class AssessorEntity {

	private Map<Agent, Map<Term, Map<ReputationType, Double>>> trustValues;

	public AssessorEntity() {
		this.trustValues = new HashMap<>();
	}

	public Double getTrustValue(Agent agent, Term term,
			ReputationType reputationType) {
		Map<ReputationType, Double> termTrustValues = getTrustValues(agent,
				term);
		if (termTrustValues == null)
			return null;
		return termTrustValues.get(reputationType);
	}

	public Map<ReputationType, Double> getTrustValues(Agent agent, Term term) {
		Map<Term, Map<ReputationType, Double>> agentTrustValues = trustValues
				.get(agent);
		if (agentTrustValues == null)
			return null;
		return agentTrustValues.get(term);
	}

	public void putTrustValue(Agent agent, Term term,
			ReputationType reputationType, Double value) {
		Map<Term, Map<ReputationType, Double>> agentTrustValues = trustValues
				.get(agent);
		if (agentTrustValues == null) {
			agentTrustValues = new HashMap<>();
			this.trustValues.put(agent, agentTrustValues);
		}
		Map<ReputationType, Double> termTrustValues = agentTrustValues
				.get(term);
		if (termTrustValues == null) {
			termTrustValues = new HashMap<>();
			agentTrustValues.put(term, termTrustValues);
		}
		termTrustValues.put(reputationType, value);
	}

}
