// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.compare;

import java.io.*;
import java.util.*;

/**
 * Some utilities methods to compare two objects.
 * @author Mario Fusco
 */
public final class ComparatorUtil {

    private ComparatorUtil() { }

    public static int nullSafeCompare(Object o1, Object o2) {
        return nullSafeCompare(DEFAULT_ARGUMENT_COMPARATOR, o1, o2);
    }

    static int nullSafeCompare(Comparator<Object> comparator, Object o1, Object o2) {
        if (o1 == null) return o2 == null ? 0 : 1;
        return o2 == null ? -1 : comparator.compare(o1, o2);
    }

	static final Comparator<Object> DEFAULT_ARGUMENT_COMPARATOR = new DefaultArgumentComparator();

	static class DefaultArgumentComparator implements Comparator<Object>, Serializable {
		@SuppressWarnings("unchecked")
		public int compare(Object val1, Object val2) {
			return val1 != null ? ((Comparable)val1).compareTo(val2) : -((Comparable)val2).compareTo(null);
		}
	}
}
