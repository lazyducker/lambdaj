// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.closure;

import java.util.*;

/**
 * A generic (not strongly typed) lambdaj closure
 * @author Mario Fusco
 */
public class Closure extends AbstractClosure {

    /**
     * Invokes this closure once by applying the given set of parameters to it.
     * @param params The set of parameter used to invoke this closure once
     * @return The result of the closure invocation
     * @throws WrongClosureInvocationException if the number of the passed parameters doesn't correspond to one
     * with which this closure has been defined
     */
	public Object apply(Object... params) throws WrongClosureInvocationException {
		return closeOne(params);
	}

    /**
     * Invokes this closure once for each passed parameter.
     * It is then assumed that this closure has been defined with exactly one free paramater
     * @param params The set of parameter used to invoke this closure once for each parameter
     * @return A list of Object containing the results of each closure invocation
     * @throws WrongClosureInvocationException if this closure hasn't been defined with exactly one free parameter
     */
	public List<Object> each(Object... params) throws WrongClosureInvocationException {
		return closeAll(params);
	}

    /**
     * Invokes this closure once for each passed set of parameters.
     * Each iterable is used as a different set of parameters with which this closure is invoked
     * @param params The parameters used to invoke this closure once for each set of parameters
     * @return A list of Object containing the results of each closure invocation
     * @throws WrongClosureInvocationException if the number of the passed parameters doesn't correspond to one
     * with which this closure has been defined
     */
	public List<Object> each(Iterable<?>... params) throws WrongClosureInvocationException {
		return closeAll(params);
	}

    /**
     * Curry this closure by fixing one of its free parameter to a given value.
     * @param curry The value to which the free parameter should be curry
     * @param position The 1-based position of the parameter to which apply the curry operation
     * @return A Closure having a free parameter less than this one since one of them has been fixed to the given value
     * @throws IllegalArgumentException if this closure doesn't have a free parameter in the specified position
     */
	public Closure curry(Object curry, int position) throws IllegalArgumentException {
		return curry(new Closure(), curry, position);
	}
}
