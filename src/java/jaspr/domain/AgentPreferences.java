/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.domain;

import jaspr.fire.RecencyWeightFunction;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents preference of an agent. It consists of: (i) preferences
 * for terms; (ii) preferences for reputation types; and (iii) the recncy factor
 * lambda.
 * 
 * @author ingridnunes
 */
public class AgentPreferences {

	private Double lambda;
	private Map<ReputationType, Double> reputationType;
	private Map<Term, Double> term;

	public AgentPreferences() {
		this.reputationType = new HashMap<>();
		this.term = new HashMap<>();
		this.lambda = RecencyWeightFunction.DEFAULT_LAMBDA;
	}

	public Double getLambda() {
		return lambda;
	}

	public Map<ReputationType, Double> getReputationTypePreferences() {
		return reputationType;
	}

	public Map<Term, Double> getTermPreferences() {
		return term;
	}

	public void put(ReputationType rt, Double preference) {
		this.reputationType.put(rt, preference);
	}

	public void put(Term t, Double preference) {
		this.term.put(t, preference);
	}

	public void setLambda(Double lambda) {
		this.lambda = lambda;
	}

}
