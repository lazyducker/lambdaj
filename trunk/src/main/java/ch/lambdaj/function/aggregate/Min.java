package ch.lambdaj.function.aggregate;

/**
 * @author Mario Fusco
 * @author Luca Marrocco
 */
public class Min<T> extends Chooser<T> {

	@Override
	public Comparable<T> choose(Comparable<T> first, Comparable<T> second) {
		if (first == null) return second;
		if (second == null) return first;
		return first.compareTo((T) second) < 0 ? first : second;
	}
}
