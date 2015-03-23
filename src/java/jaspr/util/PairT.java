/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.util;

/**
 * @author ingrid
 * 
 */
public class PairT<T1, T2> {

	protected T1 value1;
	protected T2 value2;

	public PairT() {
		this.value1 = null;
		this.value2 = null;
	}

	public PairT(T1 value1, T2 value2) {
		this.value1 = value1;
		this.value2 = value2;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PairT))
			return false;
		PairT<?, ?> other = (PairT<?, ?>) obj;
		return (value1.equals(other.value1) && (value2.equals(other.value2)));
	}

	public T1 getValue1() {
		return value1;
	}

	public T2 getValue2() {
		return value2;
	}

	@Override
	public int hashCode() {
		int result = value1.hashCode();
		result = HashCodeUtil.hash(result, value2);
		return result;
	}

	public void setValue1(T1 value1) {
		this.value1 = value1;
	}

	public void setValue2(T2 value2) {
		this.value2 = value2;
	}

	@Override
	public String toString() {
		return "<" + value1 + ", " + value2 + ">";
	}

}