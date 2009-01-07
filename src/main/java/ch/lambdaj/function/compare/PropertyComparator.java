package ch.lambdaj.function.compare;

import java.util.Comparator;

import ch.lambdaj.util.IntrospectionUtil;

public class PropertyComparator<T> implements Comparator<T> {

	private String propertyName;

	public PropertyComparator(String propertyName) {
		this.propertyName = propertyName;
	}

	public int compare(T o1, T o2) {
		Comparable val1 = (Comparable)IntrospectionUtil.getPropertyValue(o1, propertyName);
		Comparable val2 = (Comparable)IntrospectionUtil.getPropertyValue(o2, propertyName);
		return val1.compareTo(val2);
	}

}
