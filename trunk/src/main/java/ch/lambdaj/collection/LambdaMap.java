// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.collection;

import static ch.lambdaj.Lambda.convertMap;
import static org.hamcrest.Matchers.not;

import java.util.*;

import ch.lambdaj.function.convert.Converter;
import org.hamcrest.*;

/**
 * A Map that extends the Map interface with the fluent interface methods provided by lambdaj
 * @author Gianfranco Tognana
 * @author Mario Fusco
 */
public class LambdaMap<K, V> implements Map<K, V> {
    
	private Map<K, V> inner;
	
	LambdaMap(Map<K, V> inner) {
		this.inner = inner;
	}
	
    /**
	 * Converts all the values in this map using the given {@link Converter}.
     * @param converter The converter that specifies how each map's value must be converted
	 * @return A Map containing the same keys of the original one and the value converted from the ones
     *      in the corresponding entry of the map
	 */
    public <T> LambdaMap<K, T> convertValues(Converter<V, T> converter) {
        return new LambdaMap<K, T>(convertMap(inner, converter));
    }

    /**
	 * Converts all the values in this map extracting the property defined by the given argument.
	 * @param argument An argument defined using the {@link ch.lambdaj.Lambda#on(Class)} method method
	 * @return A Map containing the same keys of the original one and the argument's value extracted from the value
     *      in the corresponding entry of the map
	 */
	public <T> LambdaMap<K, T> convertValues(T argument) {
		return new LambdaMap<K, T>(convertMap(inner, argument));
	}

    public LambdaMap<K, V> retainKeys(Matcher<?> matcher) {
        return retain(matcher, true);
    }

    public LambdaMap<K, V> removeKeys(Matcher<?> matcher) {
        return retainKeys(not(matcher));
    }

    public LambdaMap<K, V> retainValues(Matcher<?> matcher) {
        return retain(matcher, false);
    }

    public LambdaMap<K, V> removeValues(Matcher<?> matcher) {
        return retainValues(not(matcher));
    }

    private LambdaMap<K, V> retain(Matcher<?> matcher, boolean matchKeys) {
        Map<K, V> map = new HashMap<K, V>();
        for (Entry<K, V> entry : inner.entrySet()) {
            if (matcher.matches(matchKeys ? entry.getKey() : entry.getValue())) map.put(entry.getKey(), entry.getValue());
        }
        return new LambdaMap<K, V>(map);
    }

    // ////////////////////////////////////////////////////////////////////////
    // /// Map interface
    // ////////////////////////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
	public void clear() {
	    inner.clear();
    }

    /**
     * {@inheritDoc}
     */
	public boolean containsKey(Object key) {
        return inner.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
	public boolean containsValue(Object value) {
	    return inner.containsValue(value);
    }

    /**
     * {@inheritDoc}
     */
	public LambdaSet<java.util.Map.Entry<K, V>> entrySet() {
	    return new LambdaSet<java.util.Map.Entry<K, V>>(inner.entrySet());
    }

    /**
     * {@inheritDoc}
     */
	public boolean equals(Object o) {
	    return inner.equals(o);
    }

    /**
     * {@inheritDoc}
     */
	public V get(Object key) {
	    return inner.get(key);
    }

    /**
     * {@inheritDoc}
     */
	public int hashCode() {
	    return inner.hashCode();
    }

    /**
     * {@inheritDoc}
     */
	public boolean isEmpty() {
	    return inner.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
	public LambdaSet<K> keySet() {
	    return new LambdaSet<K>(inner.keySet());
    }

    /**
     * {@inheritDoc}
     */
	public V put(K key, V value) {
	    return inner.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
	public void putAll(Map<? extends K, ? extends V> m) {
	    inner.putAll(m);
    }

    /**
     * {@inheritDoc}
     */
	public V remove(Object key) {
	    return inner.remove(key);
    }

    /**
     * {@inheritDoc}
     */
	public int size() {
	    return inner.size();
    }

    /**
     * {@inheritDoc}
     */
	public LambdaCollection<V> values() {
	    return new LambdaCollection(inner.values());
    }
}
