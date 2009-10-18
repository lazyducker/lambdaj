// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.proxy;

import static ch.lambdaj.proxy.ProxyUtil.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Proxies a list of objects in order to seamlessly iterate on them by exposing the API of a single object.
 * @author Mario Fusco
 */
public class ProxyIterator<T> extends InvocationInterceptor implements Iterable<T> {

	private final Iterator<? extends T> proxiedIterator;
    private final T firstItem;

	protected ProxyIterator(Iterator<? extends T> proxiedIterator) {
		this(proxiedIterator, null);
	}

    private ProxyIterator(Iterator<? extends T> proxiedIterator, T firstItem) {
        this.proxiedIterator = proxiedIterator;
        this.firstItem = firstItem;
    }

	public Object invoke(Object obj, Method method, Object[] args) throws Throwable {
		if (method.getName().equals("iterator")) return iterator();
		return createProxyIterator(iterateOnValues(method, args), (Class<Object>)method.getReturnType());
	}

	protected Iterator<Object> iterateOnValues(Method method, Object[] args) throws Throwable {
        List<Object> list = new ArrayList<Object>();
        if (firstItem != null) list.add(method.invoke(firstItem, args));
        while (proxiedIterator.hasNext()) list.add(method.invoke(proxiedIterator.next(), args));
		return list.iterator();
	}

	public static <T> T createProxyIterator(Iterator<? extends T> proxiedIterator, Class<T> clazz) {
		return createIterableProxy(new ProxyIterator<T>(proxiedIterator), clazz);
	}

    public static <T> T createProxyIterator(Iterator<? extends T> proxiedIterator, T firstItem) {
        return (T)createIterableProxy(new ProxyIterator<T>(proxiedIterator, firstItem), firstItem.getClass());
    }

    @SuppressWarnings("unchecked")
	public Iterator<T> iterator() {
		return firstItem == null ? (Iterator<T>)proxiedIterator : new IteratorWithFirstItem();
	}

    private class IteratorWithFirstItem implements Iterator<T> {

        private boolean firstItemConsumed = false;

        public boolean hasNext() {
            return !firstItemConsumed || proxiedIterator.hasNext();
        }

        public T next() {
            if (firstItemConsumed) return proxiedIterator.next();
            firstItemConsumed = true;
            return firstItem;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
