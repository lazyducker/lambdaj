// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.collection;

import java.util.*;

/**
 * @author Gianfranco Tognana
 * @author Mario Fusco
 */
public class LambdaIterator<T> extends AbstractLambdaCollection<T> implements Iterator<T> {

	protected LambdaIterator(Iterator<? extends T> inner) {
        super(inner);
    }

	public boolean hasNext() {
		return innerIterator.hasNext();
	}

	public T next() {
		return innerIterator.next();
	}

	public void remove() {
		innerIterator.remove();
	}
}