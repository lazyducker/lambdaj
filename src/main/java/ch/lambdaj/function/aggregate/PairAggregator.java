// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.aggregate;

/**
 * @author Mario Fusco
 */
public abstract class PairAggregator<T> implements Aggregator<T> {

    public T aggregate(Iterable<? extends T> iterable) {
        T result = emptyItem();
        if (iterable != null) for (T item : iterable) result = aggregate(result, item);
        return result;
    }
}
