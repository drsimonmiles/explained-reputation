/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.util;

/**
 * @author ingridnunes
 *
 */
public class IdentityFunction implements WeightFunction {

	@Override
	public Double calculate(Double parameter) {
		return parameter;
	}

	@Override
	public Boolean isContextDependent() {
		return false;
	}

}
