// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.closure;

/**
 * A closure with no free parameters
 * @author Mario Fusco
 */
public class Closure0 extends AbstractClosure {

    /**
     * Invoke this parameterless closure
     * @return The result of the closure invocation
     */
	public Object apply() {
		return closeOne();
	}
}
