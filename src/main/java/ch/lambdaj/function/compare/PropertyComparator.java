// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.compare;

import java.util.Comparator;

import ch.lambdaj.util.IntrospectionUtil;

/**
 * @author Mario Fusco
 */
public class PropertyComparator<T> implements Comparator<T> {

	private String propertyName;

	public PropertyComparator(String propertyName) {
		this.propertyName = propertyName;
	}

	@SuppressWarnings("unchecked")
	public int compare(T o1, T o2) {
		Comparable val1 = (Comparable)IntrospectionUtil.getPropertyValue(o1, propertyName);
		Comparable val2 = (Comparable)IntrospectionUtil.getPropertyValue(o2, propertyName);
		if (val1 == null && val2 == null) return 0;
		return val1 != null ? val1.compareTo(val2) : -val2.compareTo(null);
	}

}
