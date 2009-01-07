package ch.lambdaj.function.aggregate;


public class Summer implements Aggregator<Number> {

	public Number aggregate(Number first, Number second) {
		if (second instanceof Integer) return aggregate(first, (Integer)second);
		if (second instanceof Long) return aggregate(first, (Long)second);
		if (second instanceof Float) return aggregate(first, (Float)second);
		
		double firstValue = first == null ? emptyItem().doubleValue() : first.doubleValue();
		double secondValue = second == null ? emptyItem().doubleValue() : second.doubleValue();
		return firstValue + secondValue;
	}

	public Integer aggregate(Number first, Integer second) {
		int firstValue = first == null ? emptyItem().intValue() : first.intValue();
		int secondValue = second == null ? emptyItem().intValue() : second.intValue();
		return firstValue + secondValue;
	}

	public Long aggregate(Number first, Long second) {
		long firstValue = first == null ? emptyItem().longValue() : first.longValue();
		long secondValue = second == null ? emptyItem().longValue() : second.longValue();
		return firstValue + secondValue;
	}

	public Float aggregate(Number first, Float second) {
		float firstValue = first == null ? emptyItem().floatValue() : first.floatValue();
		float secondValue = second == null ? emptyItem().floatValue() : second.floatValue();
		return firstValue + secondValue;
	}

	public Number emptyItem() {
		return 0.0;
	}
}
