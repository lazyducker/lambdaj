// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.collection;

import java.util.*;
import static java.util.Arrays.asList;

/**
 * @author Gianfranco Tognana
 * @author Mario Fusco
 */
public final class LambdaCollections {
    
    private LambdaCollections() { }

    public static <T> LambdaIterator<T> with(Iterator<? extends T> inner) {
        return new LambdaIterator<T>(inner);
    }

    public static <T> LambdaList<T> with(List<? extends T> inner) {
        return with(inner, null);
    }

    public static <T> LambdaList<T> with(List<? extends T> inner, Class<T> type) {
        return new LambdaList<T>(inner, type);
    }

	public static <T> LambdaList<T> with(T... inner) {
		return with(new ArrayList<T>(asList(inner)), (Class<T>)inner.getClass().getComponentType());
	}

	public static <T> LambdaCollection<T> with(Collection<T> inner) {
		return with(inner, null);
	}

	public static <T> LambdaCollection<T> with(Collection<? extends T> inner, Class<T> type) {
		return new LambdaCollection<T>(inner, type);
	}
	
	public static <T> LambdaIterable<T> with(Iterable<? extends T> inner) {
		return with(inner, null);
	}

	public static <T> LambdaIterable<T> with(Iterable<? extends T> inner, Class<T> type) {
		return new LambdaIterable<T>(inner, type);
	}
	
	public static <K, V> LambdaMap<K, V> with(Map<K, V> inner) {
		return new LambdaMap<K, V>(inner);
	}

	public static <T> LambdaSet<T> with(Set<? extends T> inner, Class<T> type) {
		return new LambdaSet<T>(inner, type);
	}
}
