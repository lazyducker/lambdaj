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

    protected LambdaCollection(Collection<? extends T> inner) {
        super(inner);
    }

    private Collection<T> innerCollection() {
        return (Collection<T>) innerIterable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LambdaCollection<T> filter(Matcher<?> matcher) {
        return new LambdaCollection<T>(doFilter(matcher));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LambdaCollection<T> remove(Matcher<?> matcher) {
        return new LambdaCollection<T>(doRemove(matcher));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LambdaCollection<T> sort(Object argument) {
        return new LambdaCollection<T>(doSort(argument));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <V> LambdaCollection<V> convert(Converter<T, V> converter) {
        return new LambdaCollection<V>(doConvert(converter));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <V> LambdaCollection<V> extract(V argument) {
        return new LambdaCollection<V>(doExtract(argument));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LambdaCollection<T> replace(Matcher<?> matcher, T replacer) {
        return new LambdaCollection<T>(doReplace(matcher, replacer));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LambdaCollection<T> distinct(Object argument) {
        return new LambdaCollection<T>(doDistinct(argument));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <V> LambdaCollection<V> project(Class<V> targetClass, Object... arguments) {
        return new LambdaCollection<V>(doProject(targetClass, arguments));
    }
    
    // ////////////////////////////////////////////////////////////////////////
    // /// Collection interface
    // ////////////////////////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
    public boolean add(T e) {
        return innerCollection().add(e);
    }

    /**
     * {@inheritDoc}
     */
    public boolean addAll(Collection<? extends T> c) {
        return innerCollection().addAll(c);
    }

    /**
     * {@inheritDoc}
     */
    public LambdaCollection<T> append(List<T> list) {
        innerCollection().addAll(list);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public LambdaCollection<T> append(T... list) {
        innerCollection().addAll(asList(list));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        innerCollection().clear();
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(Object o) {
        return innerCollection().contains(o);
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsAll(Collection<?> c) {
        return innerCollection().containsAll(c);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        return innerCollection().isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public boolean remove(Object o) {
        return innerCollection().remove(o);
    }

    /**
     * {@inheritDoc}
     */
    public boolean removeAll(Collection<?> c) {
        return innerCollection().removeAll(c);
    }

    /**
     * {@inheritDoc}
     */
    public boolean retainAll(Collection<?> c) {
        return innerCollection().retainAll(c);
    }

    /**
     * {@inheritDoc}
     */
    public int size() {
        return innerCollection().size();
    }

    /**
     * {@inheritDoc}
     */
    public Object[] toArray() {
        return innerCollection().toArray();
    }

    /**
     * {@inheritDoc}
     */
    public <T> T[] toArray(T[] a) {
        return innerCollection().toArray(a);
    }
}
