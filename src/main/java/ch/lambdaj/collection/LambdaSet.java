// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.collection;

import java.util.*;

/**
 * @author Gianfranco Tognana
 * @author Mario Fusco
 */
public class LambdaSet<T> extends LambdaCollection<T> implements Set<T> {

	LambdaSet(Set<? extends T> inner) {
        super(inner);
	}

    private Set<T> innerSet() {
        return (Set<T>) innerIterable;
    }
}
