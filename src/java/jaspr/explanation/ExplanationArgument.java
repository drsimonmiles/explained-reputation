/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.explanation;

/**
 * This interface represents explanation arguments, which are associated with an
 * explanation.
 * 
 * @author ingridnunes
 */
public interface ExplanationArgument {

	public Explanation getExplanation();

	public abstract String getTextualForm();

	public void setExplanation(Explanation explanation);

}
