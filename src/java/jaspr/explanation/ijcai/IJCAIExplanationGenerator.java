/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.explanation.ijcai;

import jaspr.domain.Agent;
import jaspr.domain.ReputationType;
import jaspr.domain.Term;
import jaspr.explanation.Explanation;
import jaspr.explanation.ExplanationGenerator;
import jaspr.explanation.argument.DecisiveCriteriaArgument;
import jaspr.explanation.argument.InvertArgument;
import jaspr.explanation.argument.NotOnAverageArgument;
import jaspr.explanation.argument.RecencyArgument;
import jaspr.explanation.argument.generator.DecisiveCriteriaArgumentGenerator;
import jaspr.explanation.argument.generator.DominationArgumentGenerator;
import jaspr.explanation.argument.generator.InvertArgumentGenerator;
import jaspr.explanation.argument.generator.NotOnAverageArgumentGenerator;
import jaspr.explanation.argument.generator.RecencyArgumentGenerator;
import jaspr.explanation.argument.generator.TradeOffResolutionArgumentGenerator;
import jaspr.fire.RecencyWeightFunction;
import jaspr.fire.TermTrust;
import jaspr.fire.TrustScore;
import jaspr.fire.TrustValue;

import java.security.InvalidParameterException;

/**
 * @author ingridn
 * 
 */
public class IJCAIExplanationGenerator implements ExplanationGenerator {

	private DecisiveCriteriaArgumentGenerator<Agent, Term> decisiveCriteriaArgumentGenerator;
	private DominationArgumentGenerator<Agent, Term> dominationArgumentGenerator;
	private InvertArgumentGenerator<Agent, ReputationType> invertArgumentGenerator;
	private NotOnAverageArgumentGenerator<Agent, Integer> notOnAverageArgumentGenerator;
	private RecencyArgumentGenerator recencyArgumentGenerator;
	private TradeOffResolutionArgumentGenerator<Agent, Term> tradeOffResolutionArgumentGenerator;

	public IJCAIExplanationGenerator() {
		this.dominationArgumentGenerator = new DominationArgumentGenerator<>();
		this.decisiveCriteriaArgumentGenerator = new DecisiveCriteriaArgumentGenerator<>();
		this.tradeOffResolutionArgumentGenerator = new TradeOffResolutionArgumentGenerator<>(
				decisiveCriteriaArgumentGenerator);
		this.recencyArgumentGenerator = new RecencyArgumentGenerator();
		this.invertArgumentGenerator = new InvertArgumentGenerator<>();
		this.notOnAverageArgumentGenerator = new NotOnAverageArgumentGenerator<>();
	}

	public Explanation generateExplanation(TrustScore bestAgentScore,
			TrustScore worstAgentScore) {
		if (bestAgentScore.getWeightedMean() < worstAgentScore
				.getWeightedMean()) {
			throw new InvalidParameterException("Agent "
					+ bestAgentScore.getTarget() + " is worse than Agent "
					+ worstAgentScore.getTarget() + ".");
		}

		IJCAIExplanation explanation = new IJCAIExplanation(
				bestAgentScore.getSource(), bestAgentScore.getTarget(),
				worstAgentScore.getTarget());

		DecisiveCriteriaArgument<Agent, Term> dcArgument = null;
		if (dominationArgumentGenerator.dominates(bestAgentScore,
				worstAgentScore)) {
			dcArgument = dominationArgumentGenerator.generate(
					bestAgentScore.getTarget(), worstAgentScore.getTarget(),
					bestAgentScore.keySet(), bestAgentScore, worstAgentScore);
		} else {
			dcArgument = decisiveCriteriaArgumentGenerator.generate(
					bestAgentScore.getTarget(), worstAgentScore.getTarget(),
					bestAgentScore.keySet(), bestAgentScore, worstAgentScore);
			if (dcArgument == null) {
				dcArgument = tradeOffResolutionArgumentGenerator.generate(
						bestAgentScore.getTarget(),
						worstAgentScore.getTarget(), bestAgentScore.keySet(),
						bestAgentScore, worstAgentScore);
			}
		}
		explanation.setDecisiveCriteria(dcArgument);

		RecencyArgument<Agent> overallRecency = recencyArgumentGenerator
				.generate(bestAgentScore, worstAgentScore);
		if (overallRecency != null) {
			explanation.setRecency(overallRecency);
		}

		for (Term t : dcArgument.getPros()) {
			TermTrust ttB = bestAgentScore.getTermTrust(t);
			TermTrust ttW = worstAgentScore.getTermTrust(t);
			InvertArgument<Agent, ReputationType> invertArgument = invertArgumentGenerator
					.generate(bestAgentScore.getTarget(),
							worstAgentScore.getTarget(), ttB.keySet(), ttB, ttW);
			if (invertArgument != null
					&& (!invertArgument.getC_pn().isEmpty() || ((!invertArgument
							.getC_ps().isEmpty() || !invertArgument.getC_prs()
							.isEmpty()) && (!invertArgument.getC_nw().isEmpty() || !invertArgument
							.getC_nrw().isEmpty())))) {
				explanation.addReputationType(t, invertArgument);
			}

			for (ReputationType rt : ttB.keySet()) {
				TrustValue tvB = ttB.getTrustValue(rt);
				TrustValue tvW = ttW.getTrustValue(rt);

				if (tvB.getWeightFunction() instanceof RecencyWeightFunction
						&& tvB.getWeightedMean() > tvW.getWeightedMean()) {
					NotOnAverageArgument<Agent, Integer> noa = notOnAverageArgumentGenerator
							.generate(bestAgentScore.getTarget(),
									worstAgentScore.getTarget(), null, tvB, tvW);
					if (noa != null) {
						RecencyArgument<Agent> recency = new RecencyArgument<Agent>(
								bestAgentScore.getTarget(),
								worstAgentScore.getTarget());
						explanation.addRecencyTK(t, rt, recency);
					}
				}
			}
		}

		return explanation;
	}

}
