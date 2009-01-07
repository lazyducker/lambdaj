package ch.lambdaj.function.aggregate;


public abstract class Chooser<T> implements Aggregator<Comparable<T>> {

	public Comparable<T> aggregate(Comparable<T> first, Comparable<T> second) {
		if (first == null) return second;
		if (second == null) return first;
		return choose(first, second);
	}
	
	public abstract Comparable<T> choose(Comparable<T> first, Comparable<T> second);

	public Comparable<T> emptyItem() {
		return null;
	}
}
