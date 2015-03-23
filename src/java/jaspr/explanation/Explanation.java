/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.explanation;

import jaspr.domain.Agent;

import java.util.HashSet;
import java.util.Set;

/**
 * This class represents an explanation of why an agent bestAgent is considered
 * better (i.e. has higher trust score) than an agent worstAgent, for an agent
 * assessor. Such explanation is composed of a set of arguments (
 * {@link ExplanationArgument}).
 * 
 * @author ingridn
 */
public class Explanation {

	protected Set<ExplanationArgument> arguments;
	protected Agent assessor;
	protected Agent bestAgent;
	protected Agent worstAgent;

	public Explanation() {
		this.arguments = new HashSet<>();
	}

	public Explanation(Agent assessor, Agent bestAgent, Agent worstAgent) {
		this.assessor = assessor;
		this.bestAgent = bestAgent;
		this.worstAgent = worstAgent;
		this.arguments = new HashSet<>();
	}

	public void addArgument(ExplanationArgument argument) {
		this.arguments.add(argument);
		argument.setExplanation(this);
	}

	public Set<ExplanationArgument> getArguments() {
		return arguments;
	}

	public Agent getAssessor() {
		return assessor;
	}

	public Agent getBestAgent() {
		return bestAgent;
	}

	public String getTextualForm() {
		StringBuffer sb = new StringBuffer();
		for (ExplanationArgument argument : arguments) {
			sb.append(argument.getTextualForm()).append("\n");
		}
		return sb.toString();
	}

	public Agent getWorstAgent() {
		return worstAgent;
	}

	public void removeArgument(ExplanationArgument argument) {
		boolean removed = this.arguments.remove(argument);
		if (removed) {
			argument.setExplanation(null);
		}
	}

	@Override
	public String toString() {
		return getTextualForm();
	}

}
