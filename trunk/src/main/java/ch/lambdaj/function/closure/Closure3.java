// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.closure;

import java.util.*;

/**
 * A closure with three free variables
 * @author Mario Fusco
 */
public class Closure3<A, B, C> extends AbstractClosure {

    /**
      * Invokes this closure once by applying the given triple of variables to it.
      * @param var1 The first variable used to invoke this closure
      * @param var2 The second variable used to invoke this closure
      * @param var3 The third variable used to invoke this closure
      * @return The result of the closure invocation
      */
	public Object apply(A var1, B var2, C var3) {
		return closeOne(var1, var2, var3);
	}
	
    /**
     * Invokes this closure once for each passed triple of variables.
     * @param vars1 The first set of variables used to invoke this closure once for each variable
     * @param vars2 The second set of variables used to invoke this closure once for each variable
     * @param vars3 The third set of variables used to invoke this closure once for each variable
     * @return A list of Object containing the results of each closure invocation
     */
	public List<?> each(Iterable<? extends A> vars1, Iterable<? extends B> vars2, Iterable<? extends C> vars3) {
		return closeAll(vars1, vars2, vars3);
	}
	
    /**
     * Curry this closure by fixing its first free variable to a given value.
     * @param curry The value to which the first variable should be curry
     * @return A Closure having two free variables
     */
	public Closure2<B, C> curry1(A curry) {
		return curry(new Closure2<B, C>(), curry, 1);
	}
	
    /**
     * Curry this closure by fixing its second free variable to a given value.
     * @param curry The value to which the second variable should be curry
     * @return A Closure having two free variables
     */
	public Closure2<A, C> curry2(B curry) {
		return curry(new Closure2<A, C>(), curry, 2);
	}
	
    /**
     * Curry this closure by fixing its third free variable to a given value.
     * @param curry The value to which the third variable should be curry
     * @return A Closure having two free variables
     */
	public Closure2<A, B> curry3(C curry) {
		return curry(new Closure2<A, B>(), curry, 3);
	}

    @Override
    public Closure3<A, B, C> of(Object closedObject, String methodName, Object ... args) {
        return (Closure3<A, B, C>)super.of(closedObject, methodName, args);
    }
}
