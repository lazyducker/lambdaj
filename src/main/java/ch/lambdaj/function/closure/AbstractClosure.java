// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.closure;

import static ch.lambdaj.function.closure.ClosuresFactory.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * The abstract class extended by all the lambdaj closures
 * @author Mario Fusco
 */
abstract class AbstractClosure {

	private Object closed;
	
	private List<Method> methodList = new ArrayList<Method>();
	private List<Object[]> argsList = new ArrayList<Object[]>();
	
	private Object[] curriedVars;
	private boolean[] curriedVarsFlags;

    private int freeVarsNumber = 0;

    private List<Object[]> unhandeledInvocations = new ArrayList<Object[]>();

    /**
     * Returns the number of free variables in this closure
     * @return The number of free variables in this closure
     */
    public int getFreeVarsNumber() {
        return freeVarsNumber;
    }

    /**
     * Dynamically casts this closure to a one-method interface in order to invoke its method in a strongly typed way
     * @param asInterface The interface to which this closure should be casted
     * @return A proxy that implements the requested interface and wraps this closure
     * @throws IllegalArgumentException if the given Class is not actually an interface or if it has more than one method
     */
	@SuppressWarnings("unchecked")
	public <T> T as(Class<T> asInterface) throws IllegalArgumentException {
		if (!asInterface.isInterface()) throw new IllegalArgumentException("Cannot cast a closure to the concrete class " + asInterface.getName());
		Method[] methods = asInterface.getMethods();
		if (methods.length != 1) throw new IllegalArgumentException("Cannot cast a closure to an interface with more than one method");

		return (T)Proxy.newProxyInstance(asInterface.getClassLoader(), new Class<?>[] { asInterface },
			new InvocationHandler() {
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
					return closeOne(args);
				}
			}
		);
	}

    void setClosed(Object closed) {
		this.closed = closed;
	}
	
	void bindInvocation(Method method, Object[] args) {
        if (!method.isAccessible()) method.setAccessible(true);
		methodList.add(method);
		if (args != null) for (Object arg : args) if (isClosureVarPlaceholder(arg)) freeVarsNumber++;
		argsList.add(args);
	}

    void closeUnhandeledInvocations() {
        for (Object[] vars : unhandeledInvocations) closeOne(vars);
        unhandeledInvocations.clear();
    }

    /**
     * Invokes this closure once by applying the given set of variables to it.
     * @param vars The set of variables used to invoke this closure once
     * @return The result of the closure invocation
     * @throws WrongClosureInvocationException if the number of the passed variables doesn't correspond to one
     * with which this closure has been defined
     */
	Object closeOne(Object... vars) throws WrongClosureInvocationException {
        if (methodList.isEmpty()) {
            unhandeledInvocations.add(vars);
            return null;
        }

		List<Object[]> boundParams = bindParams(vars);
		Object result = closed;
		
		Iterator<Object[]> argsIterator = boundParams != null ? boundParams.iterator() : null;
		for (Method method : methodList) {
			if (result == null) return null;
			try {
				result = method.invoke(result, argsIterator != null ? argsIterator.next() : null);
			} catch (Exception e) {
				throw new WrongClosureInvocationException("Error invoking " + method + " on " + result, e);
			}
		}
		
		return result;
	}

    /**
     * Invokes this closure once for each passed variable.
     * It is then assumed that this closure has been defined with exactly one free variable
     * @param vars The set of variables used to invoke this closure once for each variable
     * @return A list of Object containing the results of each closure invocation
     * @throws WrongClosureInvocationException if this closure hasn't been defined with exactly one free variable
     */
	List<?> closeAll(Object... vars) throws WrongClosureInvocationException {
		List<Object> results = new ArrayList<Object>();
		for (Object var : vars) results.add(closeOne(var));
		return results;
	}
	
    /**
     * Invokes this closure once for each passed set of variables.
     * Each iterable is used as a different set of variables with which this closure is invoked
     * @param vars The variables used to invoke this closure once for each set of variables
     * @return A list of Object containing the results of each closure invocation
     * @throws WrongClosureInvocationException if the number of the passed variables doesn't correspond to one
     * with which this closure has been defined
     */
	List<?> closeAll(Iterable<?>... vars) throws WrongClosureInvocationException {
		List<Object> results = new ArrayList<Object>();
		
		int length = vars.length;
		Iterator<?>[] iterators = new Iterator<?>[length];
		for (int i = 0; i < length; i++) iterators[i] = vars[i].iterator();

		boolean finished = false;
		while (true) {
			Object[] varSet = new Object[length];
			for (int i = 0; i < length; i++) {
				if (!iterators[i].hasNext()) {
					finished = true;
					break;
				}
				varSet[i] = iterators[i].next();
			}
			if (finished) break;
			results.add(closeOne(varSet));
		}
		
		return results;
	}
	
	private List<Object[]> bindParams(Object... vars) throws WrongClosureInvocationException {
		if (vars == null || vars.length == 0) {
			if (freeVarsNumber != 0)
				throw new WrongClosureInvocationException("Closure invoked without vars instead of the expected " + freeVarsNumber);
			if (curriedVars == null) return null;
		}
		if (freeVarsNumber != vars.length)
			throw new WrongClosureInvocationException("Closure invoked with " + vars.length + " vars instead of the expected " + freeVarsNumber);
		
		int varCounter = 0;
		int curriedParamCounter = 0;
		List<Object[]> boundParams = new ArrayList<Object[]>();
		for (Object[] args : argsList) {
			if (args == null) boundParams.add(null);
			else {
				Object[] objs = new Object[args.length];
				for (int i = 0; i < args.length; i++) {
					if (!isClosureVarPlaceholder(args[i])) objs[i] = args[i];
					else if (curriedVars != null && curriedVarsFlags[curriedParamCounter]) objs[i] = curriedVars[curriedParamCounter++];
					else {
						objs[i] = vars[varCounter++];
						curriedParamCounter++;
					}
				} 
				boundParams.add(objs);
			}
		}
		return boundParams;
	}
	
    /**
     * Curry this closure by fixing one of its free variable to a given value.
     * @param curriedClosure The closure resulting from this curry operation
     * @param curried The value to which the free variable should be curry
     * @param position The 1-based position of the variable to which apply the curry operation
     * @return A Closure having a free variable less than this one since one of them has been fixed to the given value
     * @throws IllegalArgumentException if this closure doesn't have a free variable in the specified position
     */
	<T extends AbstractClosure> T curry(T curriedClosure, Object curried, int position) throws IllegalArgumentException {
		curriedClosure.closed = closed;
		curriedClosure.methodList = methodList;
		curriedClosure.argsList = argsList;
        curriedClosure.freeVarsNumber = freeVarsNumber;

        if (curriedVars != null) {
            curriedClosure.curriedVars = new Object[curriedVars.length];
            System.arraycopy(curriedVars, 0, curriedClosure.curriedVars, 0, curriedVars.length);
        }
        if (curriedVarsFlags != null) {
            curriedClosure.curriedVarsFlags = new boolean[curriedVarsFlags.length];
            System.arraycopy(curriedVarsFlags, 0, curriedClosure.curriedVarsFlags, 0, curriedVarsFlags.length);
        }

		curriedClosure.curryParam(curried, position);
		return curriedClosure;
	}
	
	private void curryParam(Object curried, int position) throws IllegalArgumentException {
		if (curriedVars == null) {
			curriedVars = new Object[freeVarsNumber];
			curriedVarsFlags = new boolean[freeVarsNumber];
		}
		
		for (int i = 0; i < curriedVars.length; i++) {
			if (curriedVarsFlags[i]) continue;
			if (--position == 0) {
				curriedVars[i] = curried;
				curriedVarsFlags[i] = true;
				freeVarsNumber--;
				return;
			}
		}
		
		throw new IllegalArgumentException("Trying to curry this closure on an already bound or unexisting variable");
	}
}
