/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.domain;

/**
 * This class represents ratings (i.e. an score) given by an agent source, to an
 * agent target, with respect to a term. Such ratings can be associated with a
 * timestamp of when it was given, or a parameter, which may be additional data.
 * A parameter may be the strength of belief of a in a rule used for role-based
 * trust, for example.
 * 
 * @author ingridnunes
 */
public class AgentRating {

	private Double parameter;
	private Double score;
	private Agent source;
	private Agent target;
	private Term term;
	private Long timestamp;

	public AgentRating() {

	}

	public AgentRating(Agent source, Agent target, Term term, Double score,
			Double parameter) {
		this(source, target, term, score, null, parameter);
	}

	public AgentRating(Agent source, Agent target, Term term, Double score,
			Long timestamp) {
		this(source, target, term, score, timestamp, null);
	}

	public AgentRating(Agent source, Agent target, Term term, Double score,
			Long timestamp, Double parameter) {
		this.source = source;
		this.target = target;
		this.term = term;
		this.score = score;
		this.timestamp = timestamp;
		this.parameter = parameter;
	}

	/**
	 * @return the parameter
	 */
	public Double getParameter() {
		return parameter;
	}

	/**
	 * @return the score
	 */
	public Double getScore() {
		return score;
	}

	/**
	 * @return the source
	 */
	public Agent getSource() {
		return source;
	}

	/**
	 * @return the target
	 */
	public Agent getTarget() {
		return target;
	}

	/**
	 * @return the term
	 */
	public Term getTerm() {
		return term;
	}

	/**
	 * @return the timestamp
	 */
	public Long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param parameter
	 *            the parameter to set
	 */
	public void setParameter(Double parameter) {
		this.parameter = parameter;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(Double score) {
		this.score = score;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(Agent source) {
		this.source = source;
	}

	/**
	 * @param target
	 *            the target to set
	 */
	public void setTarget(Agent target) {
		this.target = target;
	}

	/**
	 * @param term
	 *            the term to set
	 */
	public void setTerm(Term term) {
		this.term = term;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

}
