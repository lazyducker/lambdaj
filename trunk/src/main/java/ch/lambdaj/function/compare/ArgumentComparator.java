package ch.lambdaj.function.compare;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;

import java.util.*;

import ch.lambdaj.function.argument.*;

/**
 * @author Mario Fusco
 */
public class ArgumentComparator<T, A> implements Comparator<T> {

	private final Argument<A> argument;
	private final Comparator<?> comparator;
	
	public ArgumentComparator(A argument) {
		this(actualArgument(argument));
	}

	public ArgumentComparator(Argument<A> argument) {
		this(argument, null);
	}
	
	public ArgumentComparator(A argument, Comparator<?> comparator) {
		this(actualArgument(argument), comparator);
	}
	
	public ArgumentComparator(Argument<A> argument, Comparator<?> comparator) {
		this.argument = argument;
		this.comparator = comparator != null ? comparator : DEFAULT_ARGUMENT_COMPARATOR;
	}
	
	@SuppressWarnings("unchecked")
	public int compare(T o1, T o2) {
		Object val1 = argument.evaluate(o1);
		Object val2 = argument.evaluate(o2);
		if (val1 == null && val2 == null) return 0;
		return ((Comparator<Object>)comparator).compare(val1, val2);
	}
	
	private static final Comparator<?> DEFAULT_ARGUMENT_COMPARATOR = new DefaultArgumentComparator();

	private static class DefaultArgumentComparator implements Comparator<Object> {
		@SuppressWarnings("unchecked")
		public int compare(Object val1, Object val2) {
			return val1 != null ? ((Comparable)val1).compareTo(val2) : -((Comparable)val2).compareTo(null);
		}
	}
}