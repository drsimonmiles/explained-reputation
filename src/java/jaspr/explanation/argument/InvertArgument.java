/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.explanation.argument;

import jaspr.explanation.AbstractExplanationArgument;
import jaspr.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ingrid
 * 
 */
public class InvertArgument<O, T> extends AbstractExplanationArgument<O> {

	private Collection<T> c_nrw;
	private Collection<T> c_nw;
	private List<Pair<T>> c_pn;
	private Collection<T> c_prs;
	private Collection<T> c_ps;

	public InvertArgument(O chosenOption, O option) {
		this(chosenOption, option, new LinkedList<T>(), new LinkedList<T>(),
				new LinkedList<T>(), new LinkedList<T>(),
				new LinkedList<Pair<T>>());
	}

	public InvertArgument(O chosenOption, O option, Collection<T> c_ps,
			Collection<T> c_prs, Collection<T> c_nw, Collection<T> c_nrw,
			List<Pair<T>> c_pn) {
		super(chosenOption, option);
		this.c_ps = c_ps;
		this.c_prs = c_prs;
		this.c_nw = c_nw;
		this.c_nrw = c_nrw;
		this.c_pn = c_pn;
	}

	public Collection<T> getC_nrw() {
		return c_nrw;
	}

	public Collection<T> getC_nw() {
		return c_nw;
	}

	public List<Pair<T>> getC_pn() {
		return c_pn;
	}

	public Collection<T> getC_prs() {
		return c_prs;
	}

	public Collection<T> getC_ps() {
		return c_ps;
	}

	public Collection<T> getNegativeAttributes() {
		List<T> negative = new ArrayList<>(c_nrw.size() + c_nw.size()
				+ c_pn.size());
		negative.addAll(c_nrw);
		negative.addAll(c_nw);
		for (Pair<T> ivtPair : c_pn) {
			negative.add(ivtPair.getValue1());
		}
		return negative;
	}

	public Collection<T> getPositiveAttributes() {
		List<T> positive = new ArrayList<>(c_prs.size() + c_ps.size()
				+ c_pn.size());
		positive.addAll(c_prs);
		positive.addAll(c_ps);
		for (Pair<T> ivtPair : c_pn) {
			positive.add(ivtPair.getValue2());
		}
		return positive;
	}

	@Override
	public String getTextualForm() {
		StringBuffer sb = new StringBuffer();
		boolean firstP = true;
		boolean firstN = true;
		sb.append(chosenOption).append(" is preferred to ").append(option)
				.append(" since ");
		if (!c_ps.isEmpty()) {
			sb.append(chosenOption).append(" is better than ").append(option)
					.append(" on the ");
			if (c_ps.size() < 2) {
				sb.append("criterion ");
			} else {
				sb.append("criteria ");
			}
			appendAttributes(sb, c_ps);
			if (c_ps.size() < 2) {
				sb.append(" that is important");
			} else {
				sb.append(" that are important");
			}
			firstP = false;
		}
		if (!c_prs.isEmpty()) {
			if (firstP) {
				sb.append(chosenOption).append(" is better than ")
						.append(option);
				firstP = false;
			} else {
				sb.append(" and");
			}
			sb.append(" on the ");
			if (c_prs.size() < 2) {
				sb.append("criterion ");
			} else {
				sb.append("criteria ");
			}
			appendAttributes(sb, c_prs);
			if (c_prs.size() < 2) {
				sb.append(" that is relatively important");
			} else {
				sb.append(" that are relatively important");
			}
		}
		if (!c_nw.isEmpty()) {
			if (firstP) {
				firstP = false;
			} else {
				sb.append(", ");
				firstN = false;
			}
			sb.append(option).append(" is better than ").append(chosenOption)
					.append(" on the ");
			if (c_nw.size() < 2) {
				sb.append("criterion ");
			} else {
				sb.append("criteria ");
			}
			appendAttributes(sb, c_nw);
			if (c_nw.size() < 2) {
				sb.append(" that is not important");
			} else {
				sb.append(" that are not important");
			}
		}
		if (!c_nrw.isEmpty()) {
			if (firstP) {
				firstP = false;
			} else {
				sb.append(", ");
				if (firstN) {
					sb.append(option).append(" is better than ")
							.append(chosenOption);
					firstN = false;
				} else {
					sb.append(" and");
				}
				sb.append(" on the ");
				if (c_nrw.size() < 2) {
					sb.append("criterion ");
				} else {
					sb.append("criteria ");
				}
				appendAttributes(sb, c_nrw);
				if (c_nrw.size() < 2) {
					sb.append(" that is not really important");
				} else {
					sb.append(" that are not really important");
				}
			}
		}
		if (!c_pn.isEmpty()) {
			if (!firstP || !firstN) {
				sb.append(", and ");
			}
			for (Pair<T> ivtPair : c_pn) {
				sb.append("criterion ").append(ivtPair.getValue2())
						.append(" for which ").append(chosenOption)
						.append(" is better than ").append(option)
						.append(" is more important than criterion ")
						.append(ivtPair.getValue1()).append(" for which ")
						.append(chosenOption).append(" is worse than ")
						.append(option).append(", ");
			}
			sb.replace(sb.length() - 2, sb.length() - 1, ".");
		} else {
			sb.append(".");
		}
		return sb.toString();
	}

}