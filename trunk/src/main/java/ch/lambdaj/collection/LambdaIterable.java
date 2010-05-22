// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.collection;

import static ch.lambdaj.util.iterator.IteratorFactory.discoverGenericType;
import ch.lambdaj.*;
import ch.lambdaj.function.convert.*;
import org.hamcrest.*;

import java.util.*;

/**
 * @author Gianfranco Tognana
 * @author Mario Fusco
 */
public class LambdaIterable<T> extends AbstractLambdaCollection<T> implements Iterable<T> {

    protected final Class<T> type;

    protected LambdaIterable(Iterable<? extends T> inner, Class<T> type) {
        super(inner);
        this.type = type == null ? (Class<T>)discoverGenericType(inner) : type;
    }

    /**
     * {@inheritDoc}
     */
    public LambdaIterator<T> iterator() {
        return new LambdaIterator(innerIterator);
    }

    /**
     * Filters all the objects in this iterable that match the given hamcrest Matcher
     * @param matcher The hamcrest Matcher used to filter this iterable
     * @return A sublist of this containing all the objects that match the given hamcrest Matcher
     */
    public LambdaIterable<T> filter(Matcher<?> matcher) {
        return new LambdaIterable(doFilter(matcher), type);
    }

    List<T> doFilter(Matcher<?> matcher) {
        return (List<T>)Lambda.select(innerIterable, matcher);
    }

    /**
     * Converts all the object in this iterable using the given {@link Converter}.
     * @param converter The converter that specifies how each object in the iterable must be converted
     * @return A LambdaIterable containing all the objects in this iterable converted using the given {@link Converter}
     */
    public <V> LambdaIterable<V> convert(Converter<T, V> converter) {
        return new LambdaIterable<V>(doConvert(converter), null);
    }

    <V> List<V> doConvert(Converter<T, V> converter) {
        return Lambda.convert(innerIterable, converter);
    }

    /**
	 * Converts all the object in this iterable extracting the property defined by the given argument.
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method
	 * @return A LambdaIterable containing the argument's value extracted from the object in this iterable
	 */
    public <V> LambdaIterable<V> extract(V argument) {
        return new LambdaIterable<V>(doExtract(argument), (Class<V>)argument.getClass());
    }

    <V> List<V> doExtract(V argument) {
        return Lambda.extract(innerIterable, argument);
    }
}
