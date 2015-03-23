/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.explanation.argument.generator;

import jaspr.domain.Agent;
import jaspr.domain.ReputationType;
import jaspr.domain.Term;
import jaspr.explanation.argument.RecencyArgument;
import jaspr.fire.RecencyWeightFunction;
import jaspr.fire.TermTrust;
import jaspr.fire.TrustScore;
import jaspr.fire.TrustValue;
import jaspr.util.WeightedSum;

/**
 * @author ingridnunes
 *
 */
public class RecencyArgumentGenerator {

	public RecencyArgument<Agent> generate(TrustScore bestScore,
			TrustScore worstScore) {
		WeightedSum<Term> trustScoreB = createAverageRatingTrustScore(bestScore);
		WeightedSum<Term> trustScoreW = createAverageRatingTrustScore(worstScore);

		if (trustScoreB.getWeightedMean() < trustScoreW.getWeightedMean()) {
			return new RecencyArgument<Agent>(bestScore.getTarget(),
					worstScore.getTarget());
		} else {
			return null;
		}
	}

	private WeightedSum<Term> createAverageRatingTrustScore(
			TrustScore trustScore) {
		WeightedSum<Term> weightedSumTrust = new WeightedSum<>();
		for (Term t : trustScore.keySet()) {
			TermTrust tt = trustScore.getTermTrust(t);
			WeightedSum<ReputationType> weightedSumTerm = new WeightedSum<>();
			for (ReputationType rt : tt.keySet()) {
				TrustValue tv = tt.getTrustValue(rt);
				if (tv.getWeightFunction() instanceof RecencyWeightFunction) {
					weightedSumTerm.addValue(rt, tt.getWeightParameter(rt),
							tv.getMean());
				} else {
					weightedSumTerm.addValue(rt, tt.getWeightParameter(rt),
							tv.getWeightedMean());
				}
			}
			weightedSumTrust.addValue(t, trustScore.getWeightParameter(t),
					weightedSumTerm.getWeightedMean());
		}
		return weightedSumTrust;
	}

}
