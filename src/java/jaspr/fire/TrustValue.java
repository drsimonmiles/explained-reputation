/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.fire;

import jaspr.domain.AgentRating;
import jaspr.domain.ReputationType;
import jaspr.util.WeightedSum;

import java.util.List;

/**
 * This class is a weighted sum of agent ratings, which gives the trust value
 * associated with specific term and reputation type. Depending on the
 * reputation type different weight functions can be used.
 * 
 * @author ingridnunes
 */
public class TrustValue extends WeightedSum<Integer> {

	private final ReputationType reputationType;

	public TrustValue(ReputationType reputationType) {
		super();
		this.reputationType = reputationType;
	}

	public TrustValue(ReputationType reputationType, Double lambda,
			Long currentTime, List<AgentRating> ratings) {
		super(new RecencyWeightFunction(lambda, currentTime));
		this.reputationType = reputationType;

		for (AgentRating rating : ratings) {
			this.addValue(rating.getTimestamp().doubleValue(),
					rating.getScore());
		}
	}

	public TrustValue(ReputationType reputationType, List<AgentRating> ratings) {
		super();
		this.reputationType = reputationType;

		for (AgentRating rating : ratings) {
			this.addValue(rating.getParameter(), rating.getScore());
		}
	}

	public synchronized void addValue(Double weightParameter, Double value) {
		addValue(getSize() + 1, weightParameter, value);
	}

	public ReputationType getReputationType() {
		return reputationType;
	}

}
