// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.aggregate;

/**
 * @author Mario Fusco
 */
public class Min<T> extends Chooser<Comparable<T>> {

	@SuppressWarnings("unchecked")
	@Override
	protected Comparable<T> choose(Comparable<T> first, Comparable<T> second) {
		return first.compareTo((T) second) < 0 ? first : second;
	}
}
