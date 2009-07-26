// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.closure;

import java.util.*;

/**
 * A closure with a single free parameter
 * @author Mario Fusco
 */
public class Closure1<A> extends AbstractClosure {

    /**
      * Invokes this closure once by applying the given parameter to it.
      * @param param The parameter used to invoke this closure
      * @return The result of the closure invocation
      */
	public Object apply(A param) {
		return closeOne(param);
	}
	
    /**
     * Invokes this closure once for each passed parameter.
     * @param params The set of parameter used to invoke this closure once for each parameter
     * @return A list of Object containing the results of each closure invocation
     */
	public List<Object> each(A... params) {
		return closeAll(params);
	}
	
    /**
     * Invokes this closure once for each item in the given iterable.
     * @param params The parameters used to invoke this closure once for each of its item
     * @return A list of Object containing the results of each closure invocation
     */
	public List<Object> each(Iterable<? extends A> params) {
		return closeAll(params);
	}
	
    /**
     * Curry this closure by fixing its only free parameter to a given value.
     * @param curry The value to which the free parameter should be curry
     * @return A Closure having no free parameter
     */
	public Closure0 curry(A curry) {
		return curry(new Closure0(), curry, 1);
	}
}
