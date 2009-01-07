package ch.lambdaj.function.aggregator;

public interface Aggregator<T> {

	public T emptyItem();
	
	public T aggregate(T first, T second);
}
