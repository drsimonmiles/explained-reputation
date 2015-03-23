/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.domain;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class represents an agent. It is identified by a name (in inherited from
 * {@link NamedEntity}). It has its preferences and ratings given for different
 * agents (which can be associated with specific terms or reputation types).
 * Such ratings can be ratings given by other agents (e.g. peer) that where
 * informed to this agent. In this case, ratings are possibly associated with
 * witness trust.
 * 
 * @author ingridnunes
 */
public class Agent extends NamedEntity {

	private AgentPreferences preferences;
	private Map<Agent, Map<Term, Map<ReputationType, List<AgentRating>>>> ratings;

	public Agent(String name) {
		this(name, new AgentPreferences());
	}

	public Agent(String name, AgentPreferences preferences) {
		super(name);
		this.preferences = preferences;
		this.ratings = new HashMap<>();
	}

	public void addRating(Agent source, Agent target, Term term,
			ReputationType reputationType, Double score, Long timestamp) {
		addRating(source, target, term, reputationType, score, timestamp, null);
	}

	public void addRating(Agent source, Agent target, Term term,
			ReputationType reputationType, Double score, Long timestamp,
			Double parameter) {
		Map<Term, Map<ReputationType, List<AgentRating>>> agentRatings = this.ratings
				.get(target);
		if (agentRatings == null) {
			agentRatings = new HashMap<>();
			this.ratings.put(target, agentRatings);
		}
		Map<ReputationType, List<AgentRating>> termRatings = agentRatings
				.get(term);
		if (termRatings == null) {
			termRatings = new HashMap<>();
			agentRatings.put(term, termRatings);
		}
		List<AgentRating> rtRatings = termRatings.get(reputationType);
		if (rtRatings == null) {
			rtRatings = new LinkedList<>();
			termRatings.put(reputationType, rtRatings);
		}

		rtRatings.add(new AgentRating(source, target, term, score, timestamp,
				parameter));
	}

	public void addRating(Agent target, Term term,
			ReputationType reputationType, Double score) {
		addRating(target, term, reputationType, score,
				System.currentTimeMillis(), null);
	}

	public void addRating(Agent target, Term term,
			ReputationType reputationType, Double score, Double parameter) {
		addRating(this, target, term, reputationType, score,
				System.currentTimeMillis(), parameter);
	}

	public void addRating(Agent target, Term term,
			ReputationType reputationType, Double score, Long timestamp) {
		addRating(this, target, term, reputationType, score, timestamp, null);
	}

	public void addRating(Agent target, Term term,
			ReputationType reputationType, Double score, Long timestamp,
			Double parameter) {
		addRating(this, target, term, reputationType, score, timestamp,
				parameter);
	}

	public AgentPreferences getPreferences() {
		return preferences;
	}

	public Map<Agent, Map<Term, Map<ReputationType, List<AgentRating>>>> getRatings() {
		return ratings;
	}

	public Map<Term, Map<ReputationType, List<AgentRating>>> getRatings(
			Agent target) {
		return ratings.get(target);
	}

	public Map<ReputationType, List<AgentRating>> getRatings(Agent target,
			Term term) {
		Map<Term, Map<ReputationType, List<AgentRating>>> agentRatings = getRatings(target);
		return agentRatings == null ? null : agentRatings.get(term);
	}

	public List<AgentRating> getRatings(Agent target, Term term,
			ReputationType reputationType) {
		Map<ReputationType, List<AgentRating>> termRatings = getRatings(target,
				term);
		return termRatings == null ? null : termRatings.get(reputationType);
	}

	public void setPreferences(AgentPreferences preferences) {
		this.preferences = preferences;
	}

}
