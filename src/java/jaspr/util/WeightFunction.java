/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.util;

/**
 * @author ingridnunes
 *
 */
public interface WeightFunction {

	public Double calculate(Double parameter);

	/**
	 * Checks if this functions depends on context. A function depends on
	 * context if the method {@link #calculate(Double)} can return different
	 * weights when it is called. If it does not depend on the context,
	 * {@link #calculate(Double)} always returns the same value.
	 * 
	 * @return true, if the function is changeable; false otherwise.
	 */
	public Boolean isContextDependent();

}
