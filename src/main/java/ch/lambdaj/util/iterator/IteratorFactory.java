// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.util.iterator;

import java.util.*;

/**
 * @author Mario Fusco
 */
public final class IteratorFactory {

    private IteratorFactory() { }

    /**
     * Tries to convert a generic object in an Iterator.
     * This method works with Iterators, Arrays, Iterables and Maps and
     * in this last case an Iterator over the Map's values is returned.
     * If the object is null returns an Iterator over an empty collection.
     * If none of the above applies throws an IllegalArgumentException.
     * @param object The object to be converted
     * @return The Iterator resulting from the object conversion
     * @throws IllegalArgumentException if the given object is neither an Iterator, Array, Iterable or Map.
     */
    public static Iterator<?> asIterator(Object object) {
        if (object == null) return new ArrayList().iterator();
        if (object instanceof Iterable) return ((Iterable<?>)object).iterator();
        if (object instanceof Iterator) return (Iterator<?>)object;
        if (object.getClass().isArray()) return new ResettableIteratorOnArray<Object>((Object[])object);
        if (object instanceof Map) return ((Map<?,?>)object).values().iterator();
        throw new IllegalArgumentException("Cannot convert " + object + " to an iterator");
    }

    public static ResettableIterator<?> asResettableIterator(Object object) {
        if (object == null) return new ResettableIteratorOnIterable(new ArrayList());
        if (object instanceof Iterable) return new ResettableIteratorOnIterable((Iterable<?>)object);
        if (object instanceof Iterator) return new ResettableIteratorOnIterator((Iterator<?>)object);
        if (object.getClass().isArray()) return new ResettableIteratorOnArray<Object>((Object[])object);
        if (object instanceof Map) return new ResettableIteratorOnIterable(((Map<?,?>)object).values());
        throw new IllegalArgumentException("Cannot convert " + object + " to an iterator");
    }
}
