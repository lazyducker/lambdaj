/**
 * // Modified or written by Ex Machina SAGL for inclusion with lambdaj.
 * // Copyright (c) 2008 Mario Fusco, Luca Marrocco.
 * // Licensed under the Apache License, Version 2.0 (the "License")
 */
package ch.lambdaj.function.aggregate;

public interface Aggregator<T> {

	public T emptyItem();
	
	public T aggregate(T first, T second);
}
