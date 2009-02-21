package ch.lambdaj.function.compare;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;

import java.util.*;

import ch.lambdaj.function.argument.*;

/**
 * @author Mario Fusco
 */
public class ArgumentComparator<T> implements Comparator<T> {

	private Argument argument;
	
	public ArgumentComparator(Argument argument) {
		this.argument = argument;
	}
	
	public ArgumentComparator(Object argument) {
		this(actualArgument(argument));
	}

	@SuppressWarnings("unchecked")
	public int compare(T o1, T o2) {
		Comparable val1 = (Comparable)argument.evaluate(o1);
		Comparable val2 = (Comparable)argument.evaluate(o2);
		if (val1 == null && val2 == null) return 0;
		return val1 != null ? val1.compareTo(val2) : -val2.compareTo(null);
	}

}