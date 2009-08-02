// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.proxy;

import static ch.lambdaj.Lambda.*;

import java.lang.reflect.*;

import ch.lambdaj.function.aggregate.*;

/**
 * Proxies a list of objects in order to seamlessly aggregate their value by exposing the API of a single object.
 * @author Mario Fusco
 */
public class ProxyAggregator<T, A> extends ProxyIterator<T> {

	private final Aggregator<A> aggregator;

	protected ProxyAggregator(Iterable<T> proxiedCollection, Aggregator<A> aggregator) {
		super(proxiedCollection);
		this.aggregator = aggregator;
	}

	@Override
	public Object invoke(Object obj, Method method, Object[] args) throws Throwable {
		return aggregate(collectValues(method, args), aggregator);
	}

	@SuppressWarnings("unchecked")
	public static <T, A> T createProxyAggregator(Iterable<T> proxiedCollection, Aggregator<A> aggregator, Class<?> clazz) {
		return (T)ProxyUtil.createIterableProxy(new ProxyAggregator<T, A>(proxiedCollection, aggregator), clazz);
	}
}
