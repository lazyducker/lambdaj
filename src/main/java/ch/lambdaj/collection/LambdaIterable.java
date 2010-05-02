// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.collection;

import static ch.lambdaj.util.iterator.IteratorFactory.discoverGenericType;
import ch.lambdaj.*;
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

    public LambdaIterator<T> iterator() {
        return new LambdaIterator(innerIterator);
    }

    public LambdaIterable<T> filter(Matcher<?> matcher) {
        return new LambdaIterable(doFilter(matcher), type);
    }

    protected List<T> doFilter(Matcher<?> matcher) {
        return (List<T>)Lambda.select(innerIterable, matcher);
    }
}
