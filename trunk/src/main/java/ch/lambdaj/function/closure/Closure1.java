// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.closure;

import java.util.*;

/**
 * A closure with a single free variable
 * @author Mario Fusco
 */
public class Closure1<A> extends AbstractClosure {

    /**
      * Invokes this closure once by applying the given variable to it.
      * @param var The variable used to invoke this closure
      * @return The result of the closure invocation
      */
	public Object apply(A var) {
		return closeOne(var);
	}
	
    /**
     * Invokes this closure once for each passed variable.
     * @param vars The set of variables used to invoke this closure once for each variable
     * @return A list of Object containing the results of each closure invocation
     */
	public List<?> each(A... vars) {
		return closeAll(vars);
	}
	
    /**
     * Invokes this closure once for each item in the given iterable.
     * @param vars The variables used to invoke this closure once for each of its item
     * @return A list of Object containing the results of each closure invocation
     */
	public List<?> each(Iterable<? extends A> vars) {
		return closeAll(vars);
	}
	
    /**
     * Curry this closure by fixing its only free variable to a given value.
     * @param curry The value to which the free variable should be curry
     * @return A Closure having no free variable
     */
	public Closure0 curry(A curry) {
		return curry(new Closure0(), curry, 1);
	}

    @Override
    public Closure1<A> of(Object closedObject, String methodName, Object ... args) {
        return (Closure1<A>)super.of(closedObject, methodName, args);
    }
}
