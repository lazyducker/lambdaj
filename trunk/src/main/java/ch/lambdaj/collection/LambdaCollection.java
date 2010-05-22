// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.collection;

import java.util.*;
import static java.util.Arrays.asList;

import org.hamcrest.*;
import ch.lambdaj.function.convert.*;

/**
 * @author Gianfranco Tognana
 * @author Mario Fusco
 */
public class LambdaCollection<T> extends LambdaIterable<T> implements Collection<T> {

    protected LambdaCollection(Collection<? extends T> inner, Class<T> type) {
        super(inner, type);
    }

    private Collection<T> innerCollection() {
        return (Collection<T>) innerIterable;
    }

    public LambdaCollection<T> remove(Matcher<T> matcher) {
        // TODO
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public LambdaCollection<T> filter(Matcher<?> matcher) {
        return new LambdaCollection<T>(doFilter(matcher), type);
    }

    /**
     * {@inheritDoc}
     */
    public <V> LambdaCollection<V> convert(Converter<T, V> converter) {
        return new LambdaCollection<V>(doConvert(converter), null);
    }

    /**
     * {@inheritDoc}
     */
    public <V> LambdaCollection<V> extract(V argument) {
        return new LambdaCollection<V>(doExtract(argument), (Class<V>)argument.getClass());
    }

    // ////////////////////////////////////////////////////////////////////////
    // /// Collection interface
    // ////////////////////////////////////////////////////////////////////////

    public boolean add(T e) {
        return innerCollection().add(e);
    }

    public boolean addAll(Collection<? extends T> c) {
        return innerCollection().addAll(c);
    }

    public LambdaCollection<T> append(List<T> list) {
        innerCollection().addAll(list);
        return this;
    }

    public LambdaCollection<T> append(T... list) {
        innerCollection().addAll(asList(list));
        return this;
    }

    public void clear() {
        innerCollection().clear();
    }

    public boolean contains(Object o) {
        return innerCollection().contains(o);
    }

    public boolean containsAll(Collection<?> c) {
        return innerCollection().containsAll(c);
    }

    public boolean isEmpty() {
        return innerCollection().isEmpty();
    }

    public boolean remove(Object o) {
        return innerCollection().remove(o);
    }

    public boolean removeAll(Collection<?> c) {
        return innerCollection().removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return innerCollection().retainAll(c);
    }

    public int size() {
        return innerCollection().size();
    }

    public Object[] toArray() {
        return innerCollection().toArray();
    }

    public <T> T[] toArray(T[] a) {
        return innerCollection().toArray(a);
    }
}
