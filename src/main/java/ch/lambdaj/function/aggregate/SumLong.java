package ch.lambdaj.function.aggregate;

public class SumLong extends PairAggregator<Long> {

	public Long aggregate(Long first, Long second) {
		return first + second;
	}

	public Long emptyItem() {
		return 0L;
	}
}
