/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.fire;

import jaspr.util.WeightFunction;

/**
 * This class is a weight function, which gives higher weight to more recent
 * factors of a weighted sum. The input of the weight calculation is interpreted
 * as the timestamp of a factor.
 * 
 * @author ingridnunes
 *
 */
public class RecencyWeightFunction implements WeightFunction {

	public static final Long DEFAULT_CURRENT_TIME = 10l;
	public static final Double DEFAULT_LAMBDA = 0.5;

	private Long currentTime;
	private Double lambda;

	public RecencyWeightFunction() {
		this(DEFAULT_LAMBDA, DEFAULT_CURRENT_TIME);
	}

	public RecencyWeightFunction(Double lambda, Long currentTime) {
		this.lambda = lambda;
		this.currentTime = currentTime;
	}

	@Override
	public Double calculate(Double parameter) {
		Long time = currentTime == null ? DEFAULT_CURRENT_TIME : currentTime;
		Double weight = Math.exp(-((time - parameter.longValue()) / lambda));
		return weight;
	}

	public Long getCurrentTime() {
		return currentTime;
	}

	public Double getLambda() {
		return lambda;
	}

	@Override
	public Boolean isContextDependent() {
		return false;
	}

}
