// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.proxy;

import static ch.lambdaj.Lambda.aggregate;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;
import ch.lambdaj.function.aggregate.*;

/**
 * @author Mario Fusco
 */
public class ProxyAggregator<T, A> extends ProxyIterator<T> {

	private Aggregator<A> aggregator;

	protected ProxyAggregator(Iterable<T> proxiedCollection, Aggregator<A> aggregator) {
		super(proxiedCollection);
		this.aggregator = aggregator;
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		return aggregate(collectValues(method, args), aggregator);
	}

	public static <T, A> T createProxyAggregator(Iterable<T> proxiedCollection, Aggregator<A> aggregator, Class<?> clazz) {
		return (T)ProxyUtil.createProxy(new ProxyAggregator<T, A>(proxiedCollection, aggregator), clazz);
	}
}
