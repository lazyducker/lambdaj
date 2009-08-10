// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.closure;

import java.util.*;

/**
 * A closure with two free variables
 * @author Mario Fusco
 */
public class Closure2<A, B> extends AbstractClosure {

    /**
      * Invokes this closure once by applying the given pair of variables to it.
      * @param var1 The first variable used to invoke this closure
      * @param var2 The second variable used to invoke this closure
      * @return The result of the closure invocation
      */
	public Object apply(A var1, B var2) {
		return closeOne(var1, var2);
	}
	
    /**
     * Invokes this closure once for each passed pair of variables.
     * @param vars1 The first set of variables used to invoke this closure once for each variable
     * @param vars2 The second set of variables used to invoke this closure once for each variable
     * @return A list of Object containing the results of each closure invocation
     */
	public List<?> each(Iterable<? extends A> vars1, Iterable<? extends B> vars2) {
		return closeAll(vars1, vars2);
	}

    /**
     * Curry this closure by fixing its first free variable to a given value.
     * @param curry The value to which the first variable should be curry
     * @return A Closure having a single free variable
     */
	public Closure1<B> curry1(A curry) {
		return curry(new Closure1<B>(), curry, 1);
	}
	
    /**
     * Curry this closure by fixing its second free variable to a given value.
     * @param curry The value to which the second variable should be curry
     * @return A Closure having a single variable
     */
	public Closure1<A> curry2(B curry) {
		return curry(new Closure1<A>(), curry, 2);
	}
}
