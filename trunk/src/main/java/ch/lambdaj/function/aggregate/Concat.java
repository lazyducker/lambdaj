// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.aggregate;

import java.util.*;

/**
 * An Aggregator that concats String or more generally the String representation of a given Object
 * @author Mario Fusco
 */
public class Concat implements Aggregator<Object> {

	private final String separator;

    /**
     * Creates an aggregator that concats Strings using the default separator \", \"
     */
	public Concat() {
		this(", ");
	}

    /**
     * Creates an aggregator that concats Strings using the given separator
     * @param separator The string used to separate two concatenated Strings
     */
	public Concat(String separator) {
		this.separator = separator;
	}

    /**
     * Aggregates two object by concatenating their String representation separating them with the choosen separator
     * @param first The first object of which the String representation should be concatenated
     * @param second The second object of which the String representation should be concatenated
     * @return A String resulting of the objects Stringification separated by the given argument
     */
	public Object aggregate(Object first, Object second) {
		if (empty(first) && empty(second)) return emptyItem();
		if (empty(first)) return second;
		if (empty(second)) return first;
		return new StringBuilder().append(first.toString()).append(separator).append(second.toString()).toString();
	}

    /**
     * Aggregates two object by concatenating their String representation separating them with the choosen separator.
     * It has been implemented a custom implementation for this method instead of using the one provided
     * by the PairAggregator for performance reason, in order to levarege the StringBuilder features.
     * @param iterable The objects of which the String representation should be concatenated
     * @return A String resulting of the objects Stringification separated by the given argument
     */
    public Object aggregate(Iterable<?> iterable) {
        if (iterable == null) return emptyItem();
        StringBuilder sb = new StringBuilder();
        Iterator<?> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            Object item = iterator.next();
            if (!empty(item)) {
                sb.append(item.toString());
                break;
            }
        }
        while (iterator.hasNext()) {
            Object item = iterator.next();
            if (!empty(item)) sb.append(separator).append(item.toString());
        }
        return sb.toString();
    }

	private boolean empty(Object object) {
		if (object == null) return true;
		return object.toString().trim().equals("");
	}

    /**
     * Returns an empty String that is the idempotent item for this aggregation operation
     * @return An empty String
     */
	public String emptyItem() {
		return "";
	}
}
