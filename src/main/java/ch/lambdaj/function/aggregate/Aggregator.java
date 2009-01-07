package ch.lambdaj.function.aggregate;

public interface Aggregator<T> {

	public T emptyItem();
	
	public T aggregate(T first, T second);
}
