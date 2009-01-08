// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.aggregate;

public abstract class Chooser<T> implements Aggregator<Comparable<T>> {

	public Comparable<T> aggregate(Comparable<T> first, Comparable<T> second) {
		if (first == null) return second;
		if (second == null) return first;
		return choose(first, second);
	}

	public abstract Comparable<T> choose(Comparable<T> first, Comparable<T> second);

	public Comparable<T> emptyItem() {
		return null;
	}
}
