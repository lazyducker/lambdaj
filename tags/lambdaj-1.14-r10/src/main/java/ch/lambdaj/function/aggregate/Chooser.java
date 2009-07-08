// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.aggregate;

public abstract class Chooser<T> implements Aggregator<T> {

	public T aggregate(T first, T second) {
		if (first == null) return second;
		if (second == null) return first;
		return choose(first, second);
	}

	protected abstract T choose(T first, T second);

	public T emptyItem() {
		return null;
	}
}
