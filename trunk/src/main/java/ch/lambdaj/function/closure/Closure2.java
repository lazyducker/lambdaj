// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.closure;

import java.util.*;

/**
 * A closure with two free parameters
 * @author Mario Fusco
 */
public class Closure2<A, B> extends AbstractClosure {

    /**
      * Invokes this closure once by applying the given pair of parameters to it.
      * @param param1 The first parameter used to invoke this closure
      * @param param2 The second parameter used to invoke this closure
      * @return The result of the closure invocation
      */
	public Object apply(A param1, B param2) {
		return closeOne(param1, param2);
	}
	
    /**
     * Invokes this closure once for each passed pair of parameters.
     * @param params1 The first set of parameter used to invoke this closure once for each parameter
     * @param params2 The second set of parameter used to invoke this closure once for each parameter
     * @return A list of Object containing the results of each closure invocation
     */
	public List<Object> each(Iterable<? extends A> params1, Iterable<? extends B> params2) {
		return closeAll(params1, params2);
	}

    /**
     * Curry this closure by fixing its first free parameter to a given value.
     * @param curry The value to which the first parameter should be curry
     * @return A Closure having a single free parameter
     */
	public Closure1<B> curry1(A curry) {
		return curry(new Closure1<B>(), curry, 1);
	}
	
    /**
     * Curry this closure by fixing its second free parameter to a given value.
     * @param curry The value to which the second parameter should be curry
     * @return A Closure having a single parameter
     */
	public Closure1<A> curry2(B curry) {
		return curry(new Closure1<A>(), curry, 2);
	}
}
