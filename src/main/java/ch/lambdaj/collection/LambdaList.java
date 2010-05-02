// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.collection;

import org.hamcrest.*;

import java.util.*;

/**
 * @author Gianfranco Tognana
 * @author Mario Fusco
 */
public class LambdaList<T> extends LambdaCollection<T> implements List<T> {

	LambdaList(List<? extends T> inner, Class<T> type) {
        super(inner, type);
	}

    private List<T> innerList() {
        return (List<T>) innerIterable;
    }

    public void add(int index, T element) {
        innerList().add(index, element);
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        return innerList().addAll(index, c);
    }

    public T get(int index) {
        return innerList().get(index);
    }

    public int indexOf(Object o) {
        return innerList().indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return innerList().lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        return innerList().listIterator();
    }

    public ListIterator<T> listIterator(int index) {
        return innerList().listIterator(index);
    }

    public T remove(int index) {
        return innerList().remove(index);
    }

    public T set(int index, T element) {
        return innerList().set(index, element);
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return innerList().subList(fromIndex, toIndex);
    }

    public LambdaList<T> filter(Matcher<?> matcher) {
        return new LambdaList(doFilter(matcher), type);        
    }
}
