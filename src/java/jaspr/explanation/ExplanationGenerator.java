/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.explanation;

import jaspr.fire.TrustScore;

/**
 * This interface represents explanation generators, which are entities that
 * produce explanations to justify why an agent with a higher trust score is
 * better than another (with a lower trust score).
 * 
 * @author ingridnunes
 */
public interface ExplanationGenerator {

	public Explanation generateExplanation(TrustScore bestAgentScore,
			TrustScore worstAgentScore);

}
