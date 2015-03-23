/**
 * (C) 2015 Universidade Federal do Rio Grande do Sul
 */
package jaspr.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author ingrid
 * 
 */
public class WeightedSum<T> {

	private Double mean;
	private Pair<Double> minMax;
	private Double standardDeviation;
	private Double sum;
	private Map<T, Pair<Double>> values;
	private Double weightedMean;
	private Double weightedSum;
	private WeightFunction weightFunction;
	private Double weightTotal;

	public WeightedSum() {
		this(new IdentityFunction());
	}

	public WeightedSum(WeightFunction weightFunction) {
		this.weightFunction = weightFunction;
		this.values = new HashMap<>();
		this.minMax = null;
		this.sum = 0.0;
		this.weightedSum = 0.0;
		this.weightTotal = 0.0;
		this.mean = null;
		this.weightedMean = null;
		this.standardDeviation = null;
	}

	public synchronized void addValue(T key, Double weightParameter,
			Double value) {
		this.values.put(key, new Pair<Double>(weightParameter, value));
		if (minMax == null) {
			this.minMax = new Pair<Double>(value, value);
		} else {
			if (minMax.getValue1() > value)
				minMax.setValue1(value);
			if (minMax.getValue2() < value)
				minMax.setValue2(value);
		}

		this.sum += value;
		if (!weightFunction.isContextDependent()) {
			Double weight = weightFunction.calculate(weightParameter);
			this.weightedSum += weight * value;
			this.weightTotal += weight;
		}

		this.mean = null;
		this.weightedMean = null;
		this.standardDeviation = null;
	}

	private synchronized void calculateMean() {
		this.mean = (values.size() == 0) ? null : sum / values.size();
	}

	private synchronized void calculateStandardDeviation() {
		Double variance = getVariance();
		this.standardDeviation = (variance == null) ? null : Math
				.sqrt(getVariance());
	}

	private synchronized void calculateWeightedMean() {
		if (weightedSum == null || weightTotal == null) {
			this.weightedSum = 0.0;
			this.weightTotal = 0.0;
			for (Pair<Double> value : values.values()) {
				Double weight = weightFunction.calculate(value.getValue1());
				this.weightedSum += weight * value.getValue2();
				this.weightTotal += weight;
			}
		}
		this.weightedMean = (values.size() == 0) ? null : weightedSum
				/ weightTotal;
	}

	public Double getMax() {
		return minMax == null ? null : minMax.getValue2();
	}

	public synchronized Double getMean() {
		if (mean == null)
			calculateMean();
		return mean;
	}

	public Double getMin() {
		return minMax == null ? null : minMax.getValue1();
	}

	public Integer getSize() {
		return values.size();
	}

	public synchronized Double getStandardDeviation() {
		if (standardDeviation == null)
			calculateStandardDeviation();
		return standardDeviation;
	}

	public synchronized Double getTotal() {
		return sum;
	}

	public Double getValue(T key) {
		Pair<Double> pair = values.get(key);
		return pair == null ? null : pair.getValue2();
	}

	public synchronized Double getVariance() {
		getMean();
		double temp = 0;
		for (Pair<Double> value : values.values()) {
			temp += Math.pow(mean - value.getValue2(), 2);
		}
		return temp / values.size();
	}

	public Double getWeight(T key) {
		Pair<Double> pair = values.get(key);
		return pair == null ? null : weightFunction.calculate(pair.getValue1());
	}

	public Double getWeightedMean() {
		if (weightFunction.isContextDependent()) {
			this.weightedSum = null;
			this.weightTotal = null;
			calculateWeightedMean();
		} else if (weightedMean == null) {
			calculateWeightedMean();
		}
		return weightedMean;
	}

	public WeightFunction getWeightFunction() {
		return weightFunction;
	}

	public Double getWeightParameter(T key) {
		Pair<Double> pair = values.get(key);
		return pair == null ? null : pair.getValue1();
	}

	public Set<T> keySet() {
		return values.keySet();
	}

	public String stats() {
		StringBuffer sb = new StringBuffer();
		sb.append("Weighted Mean = ").append(getWeightedMean()).append("\n");
		sb.append("Mean = ").append(getMean()).append("\n");
		sb.append("Standard Deviation = ").append(getStandardDeviation())
				.append("\n");
		sb.append("Min = ").append(getMin()).append("\n");
		sb.append("Max = ").append(getMax()).append("\n");
		sb.append("Total = ").append(getTotal()).append("\n");
		sb.append("Count = ").append(values.size());
		return sb.toString();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		Iterator<T> keyIt = values.keySet().iterator();
		while (keyIt.hasNext()) {
			T key = keyIt.next();
			Pair<Double> pair = values.get(key);
			sb.append(weightFunction.calculate(pair.getValue1())).append(" * ")
					.append(pair.getValue2());
			if (keyIt.hasNext()) {
				sb.append(" + ");
			}
		}
		return sb.toString();
	}

	public String toStringDetailed() {
		StringBuffer sb = new StringBuffer();
		for (T key : values.keySet()) {
			Pair<Double> pair = values.get(key);
			sb.append("R(").append(key).append(") = < w(")
					.append(pair.getValue1()).append(")=")
					.append(weightFunction.calculate(pair.getValue1()))
					.append(" , ").append(pair.getValue2()).append(" >")
					.append("\n");
		}
		sb.append("Weighted Mean = ").append(getWeightedMean()).append("\n");
		sb.append("Mean = ").append(getMean());
		return sb.toString();
	}

}
