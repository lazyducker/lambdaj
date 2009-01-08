/**
 * // Modified or written by Ex Machina SAGL for inclusion with lambdaj.
 * // Copyright (c) 2008 Mario Fusco, Luca Marrocco.
 * // Licensed under the Apache License, Version 2.0 (the "License")
 */
package ch.lambdaj.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import ch.lambdaj.Lambda;

public class ProxyIterator<T> implements MethodInterceptor, Iterable<T> {

	private Iterable<T> proxiedCollection;

	protected ProxyIterator(Iterable<T> proxiedCollection) {
		this.proxiedCollection = proxiedCollection;
	}

	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		if (method.getName().equals("iterator")) return iterator();
		return Lambda.forEach(collectValues(method, args), method.getReturnType());
	}

	protected List<Object> collectValues(Method method, Object[] args) throws Throwable {
		List<Object> values = new ArrayList<Object>();
		for (T proxiedItem : proxiedCollection) {
			Object returnedObject = method.invoke(proxiedItem, args);
			values.add(returnedObject);
		}
		return values;
	}

	public static <T> T createProxyIterator(Iterable<T> proxiedCollection, Class<?> clazz) {
		return (T) createProxy(new ProxyIterator<T>(proxiedCollection), clazz);
	}

	protected static <T> T createProxy(MethodInterceptor interceptor, Class<?> clazz) {
		try {
			Enhancer e = new Enhancer();
			if (clazz.isInterface()) {
				e.setInterfaces(new Class[] { clazz, Iterable.class });
			} else if (!canBeEnhanced(clazz)) {
				Class<?>[] interfaces = clazz.getInterfaces();
				Class<?>[] proxyInterfaces = new Class[interfaces.length + 1];
				proxyInterfaces[0] = Iterable.class;
				for (int i = 0; i < interfaces.length; i++)
					proxyInterfaces[i + 1] = interfaces[i];
				e.setInterfaces(proxyInterfaces);
			} else {
				e.setSuperclass(clazz);
				e.setInterfaces(new Class[] { Iterable.class });
			}
			e.setCallback(interceptor);
			Object bean = e.create();
			return (T) bean;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static boolean canBeEnhanced(Class<?> clazz) {
		if (Proxy.isProxyClass(clazz) || Modifier.isFinal(clazz.getModifiers())) return false;
		try {
			clazz.getConstructor(null);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public Iterator<T> iterator() {
		return proxiedCollection.iterator();
	}
}
