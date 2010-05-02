// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.collection;

import static ch.lambdaj.Lambda.convertMap;

import java.util.*;

import ch.lambdaj.function.convert.Converter;

/**
 * @author Gianfranco Tognana
 * @author Mario Fusco
 */
public class LambdaMap<K, V> implements Map<K, V> {
    
	private Map<K, V> inner;
	
	LambdaMap(Map<K, V> inner) {
		this.inner = inner;
	}
	
	public <T> LambdaMap<K, T> apply(Converter<V, T> converter) {
		return new LambdaMap<K, T>(convertMap(inner, converter));
	}

	public void clear() {
	    inner.clear();
    }

	public boolean containsKey(Object key) {
        return inner.containsKey(key);
    }

	public boolean containsValue(Object value) {
	    return inner.containsValue(value);
    }

	public Set<java.util.Map.Entry<K, V>> entrySet() {
	    return new LambdaSet<java.util.Map.Entry<K, V>>(inner.entrySet(), null);
    }

	public boolean equals(Object o) {
	    return inner.equals(o);
    }

	public V get(Object key) {
	    return inner.get(key);
    }

	public int hashCode() {
	    return inner.hashCode();
    }

	public boolean isEmpty() {
	    return inner.isEmpty();
    }

	public Set<K> keySet() {
	    return new LambdaSet<K>(inner.keySet(), null);
    }

	public V put(K key, V value) {
	    return inner.put(key, value);
    }

	public void putAll(Map<? extends K, ? extends V> m) {
	    inner.putAll(m);
    }

	public V remove(Object key) {
	    return inner.remove(key);
    }

	public int size() {
	    return inner.size();
    }

	public Collection<V> values() {
	    return inner.values();
    }
}
