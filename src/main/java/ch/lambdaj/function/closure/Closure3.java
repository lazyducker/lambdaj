// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.closure;

import java.util.*;

/**
 * A closure with three free parameters
 * @author Mario Fusco
 */
public class Closure3<A, B, C> extends AbstractClosure {

    /**
      * Invokes this closure once by applying the given triple of parameters to it.
      * @param param1 The first parameter used to invoke this closure
      * @param param2 The second parameter used to invoke this closure
      * @param param3 The third parameter used to invoke this closure
      * @return The result of the closure invocation
      */
	public Object apply(A param1, B param2, C param3) {
		return closeOne(param1, param2, param3);
	}
	
    /**
     * Invokes this closure once for each passed triple of parameters.
     * @param params1 The first set of parameter used to invoke this closure once for each parameter
     * @param params2 The second set of parameter used to invoke this closure once for each parameter
     * @param params3 The third set of parameter used to invoke this closure once for each parameter
     * @return A list of Object containing the results of each closure invocation
     */
	public List<Object> each(Iterable<? extends A> params1, Iterable<? extends B> params2, Iterable<? extends C> params3) {
		return closeAll(params1, params2, params3);
	}
	
    /**
     * Curry this closure by fixing its first free parameter to a given value.
     * @param curry The value to which the first parameter should be curry
     * @return A Closure having two free parameters
     */
	public Closure2<B, C> curry1(A curry) {
		return curry(new Closure2<B, C>(), curry, 1);
	}
	
    /**
     * Curry this closure by fixing its second free parameter to a given value.
     * @param curry The value to which the second parameter should be curry
     * @return A Closure having two free parameters
     */
	public Closure2<A, C> curry2(B curry) {
		return curry(new Closure2<A, C>(), curry, 2);
	}
	
    /**
     * Curry this closure by fixing its third free parameter to a given value.
     * @param curry The value to which the third parameter should be curry
     * @return A Closure having two free parameters
     */
	public Closure2<A, B> curry3(C curry) {
		return curry(new Closure2<A, B>(), curry, 3);
	}
}
