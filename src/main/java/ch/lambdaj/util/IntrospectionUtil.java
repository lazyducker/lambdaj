// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.util;

import java.lang.reflect.*;
import java.util.*;

/**
 * This class consists exclusively of static methods that offer some introspection facilities.
 * @author Mario Fusco
 */
public final class IntrospectionUtil {

	private IntrospectionUtil() {}

    public static Iterator<?> asIterator(Object object) {
        if (object == null) return new ArrayList().iterator();
        if (object instanceof Iterable) return ((Iterable<?>)object).iterator();
        if (object instanceof Iterator) return (Iterator<?>)object;
        if (object.getClass().isArray()) return new ArrayIterator<Object>((Object[])object);
        if (object instanceof Map) return ((Map<?,?>)object).values().iterator();
        throw new IllegalArgumentException("Cannot convert " + object + " to an iterator");
    }

    public static class ArrayIterator<T> implements Iterator<T> {

        private T[] array;
        private int counter = 0;

        public ArrayIterator(T[] array) {
            this.array = array;
        }

        public boolean hasNext() {
            return counter < array.length;
        }

        public T next() {
            return array[counter++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

	public static String getPropertyName(Method invokedMethod) {
		String methodName = invokedMethod.getName();
		if (methodName.startsWith("get") || methodName.startsWith("set")) methodName = methodName.substring(3);
		else if (methodName.startsWith("is")) methodName = methodName.substring(2);
		return methodName.substring(0, 1).toLowerCase() + methodName.substring(1);
	}
	
	public static Object getPropertyValue(Object bean, String propertyName) {
		if (bean == null) return null;
		int dotPos = propertyName.indexOf('.');
		if (dotPos > 0) return getPropertyValue(getPropertyValue(bean, propertyName.substring(0, dotPos)), propertyName.substring(dotPos + 1));

		String accessorName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		try {
			return bean.getClass().getMethod("get" + accessorName).invoke(bean, (Object[]) null);
		} catch (Exception e) {
			return getBooleanPropertyValue(bean, propertyName, accessorName);
		}
	}
	
	private static Object getBooleanPropertyValue(Object bean, String propertyName, String accessorName) {
		try {
			return bean.getClass().getMethod("is" + accessorName).invoke(bean, (Object[]) null);
		} catch (Exception e) {
			return getPlainPropertyValue(bean, propertyName);
		}
	}
	
	private static Object getPlainPropertyValue(Object bean, String propertyName) {
		try {
			return bean.getClass().getMethod(propertyName).invoke(bean, (Object[]) null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
