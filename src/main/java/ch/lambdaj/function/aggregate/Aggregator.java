// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.aggregate;

/**
 * Defines an aggregation operation, i.e. how to aggregate the items of a collection.
 * @author Mario Fusco
 */
public interface Aggregator<T> {

	/**
	 * Returns the idempotent item for this aggregation operation
	 */
	public T emptyItem();

	/**
	 * Define how this operation aggregates 2 different items
	 * @param first The first item
	 * @param second The second item
	 * @return The aggregation of first and second item
	 */
	public T aggregate(T first, T second);
}
