// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.proxy;

import static ch.lambdaj.proxy.ProxyUtil.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author Mario Fusco
 */
public class ProxyIterator<T> extends InvocationInterceptor implements Iterable<T> {

	private final Iterable<T> proxiedCollection;

	protected ProxyIterator(Iterable<T> proxiedCollection) {
		this.proxiedCollection = proxiedCollection;
	}

	public Object invoke(Object obj, Method method, Object[] args) throws Throwable {
		if (method.getName().equals("iterator")) return iterator();
		List<Object> collectedValues = collectValues(method, args);
		return createProxyIterator(collectedValues, method.getReturnType());
	}

	protected List<Object> collectValues(Method method, Object[] args) throws Throwable {
		List<Object> values = new ArrayList<Object>();
		for (T proxiedItem : proxiedCollection) {
			Object returnedObject = method.invoke(proxiedItem, args);
			values.add(returnedObject);
		}
		return values;
	}

	@SuppressWarnings("unchecked")
	public static <T> T createProxyIterator(Iterable<T> proxiedCollection, Class<?> clazz) {
		return (T)createIterableProxy(new ProxyIterator<T>(proxiedCollection), clazz);
	}

	public Iterator<T> iterator() {
		return proxiedCollection.iterator();
	}
}
