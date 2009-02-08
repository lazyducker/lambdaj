// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.aggregate;

/**
 * @author Mario Fusco
 * @author Luca Marrocco
 */
public class Max<T> extends Chooser<T> {

	@SuppressWarnings("unchecked")
	@Override
	public Comparable<T> choose(Comparable<T> first, Comparable<T> second) {
		if (first == null) return second;
		if (second == null) return first;
		return first.compareTo((T) second) > 0 ? first : second;
	}
}
