// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.collection;

import org.hamcrest.*;

import java.util.*;

/**
 * A Set that extends the Set interface with the fluent interface methods provided by lambdaj
 * @author Gianfranco Tognana
 * @author Mario Fusco
 */
public class LambdaSet<T> extends LambdaCollection<T> implements Set<T> {

	LambdaSet(Set<? extends T> inner) {
        super(inner);
	}

    /**
     * {@inheritDoc}
     */
    public LambdaSet<T> retain(Matcher<?> matcher) {
        doRetain(matcher);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public LambdaSet<T> remove(Matcher<?> matcher) {
        doRemove(matcher);
        return this;
    }
}
