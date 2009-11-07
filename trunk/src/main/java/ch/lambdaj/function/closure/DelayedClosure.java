// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.closure;

import static ch.lambdaj.function.closure.ClosuresFactory.createClosure;

/**
 * @author Guillaume Bort
 * @author Mario Fusco
 */
public abstract class DelayedClosure<T> {

    private final Closure closure;
    private final ClosureResult<T> closureResult;
    private T result;

    private static final ThreadLocal<DelayedClosure<?>> currentDelayed = new ThreadLocal<DelayedClosure<?>>();

    public DelayedClosure() {
        closure = createClosure();
        closureResult = new ClosureResult<T>() { public T get() { return result; }};
        currentDelayed.set(this);
    }

    /**
     * Override this method to define how the closure has to be used during its delayed invocation
     * @param closure The closure used inside this delayed invocation
     * @return The result of the closure invocation
     */
    public abstract T doWithClosure(Closure closure);

    public final ClosureResult<T> getClosureResult() {
        return closureResult;
    }

    static void call() throws Exception {
        DelayedClosure delayedClosure = currentDelayed.get();
        currentDelayed.set(null);
        if (delayedClosure != null) delayedClosure.execute();
    }

    private void execute() {
        result = doWithClosure(closure);
    }
}
