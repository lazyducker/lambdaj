package ch.lambdaj.function.aggregate;

public class SumInteger extends PairAggregator<Integer> {

	public Integer aggregate(Integer first, Integer second) {
		return first + second;
	}

	public Integer emptyItem() {
		return 0;
	}
}