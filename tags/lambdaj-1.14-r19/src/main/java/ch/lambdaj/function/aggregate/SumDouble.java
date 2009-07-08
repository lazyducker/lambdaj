package ch.lambdaj.function.aggregate;

public class SumDouble implements Aggregator<Double> {

	public Double aggregate(Double first, Double second) {
		return first + second;
	}

	public Double emptyItem() {
		return 0.0;
	}
}
