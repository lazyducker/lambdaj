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
	
	private Object[] curriedParams;
	private boolean[] curriedParamsFlags;

    private int freeParamsNumber = 0;

    /**
     * Returns the number of free parameters in this closure
     * @return The number of free parameters in this closure
     */
    public int getFreeParamsNumber() {
        return freeParamsNumber;
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
	
	void registerInvocation(Method method, Object[] args) {
		methodList.add(method);
		if (args != null) for (Object arg : args) if (isClosureArgPlaceholder(arg)) freeParamsNumber++;
		argsList.add(args);
	}
	
    /**
     * Invokes this closure once by applying the given set of parameters to it.
     * @param params The set of parameter used to invoke this closure once
     * @return The result of the closure invocation
     * @throws WrongClosureInvocationException if the number of the passed parameters doesn't correspond to one
     * with which this closure has been defined
     */
	Object closeOne(Object... params) throws WrongClosureInvocationException {
		List<Object[]> boundParams = bindParams(params);
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
     * Invokes this closure once for each passed parameter.
     * It is then assumed that this closure has been defined with exactly one free paramater
     * @param params The set of parameter used to invoke this closure once for each parameter
     * @return A list of Object containing the results of each closure invocation
     * @throws WrongClosureInvocationException if this closure hasn't been defined with exactly one free parameter
     */
	List<Object> closeAll(Object... params) throws WrongClosureInvocationException {
		List<Object> results = new ArrayList<Object>();
		for (Object param : params) results.add(closeOne(param));
		return results;
	}
	
    /**
     * Invokes this closure once for each passed set of parameters.
     * Each iterable is used as a different set of parameters with which this closure is invoked
     * @param params The parameters used to invoke this closure once for each set of parameters
     * @return A list of Object containing the results of each closure invocation
     * @throws WrongClosureInvocationException if the number of the passed parameters doesn't correspond to one
     * with which this closure has been defined
     */
	List<Object> closeAll(Iterable<?>... params) throws WrongClosureInvocationException {
		List<Object> results = new ArrayList<Object>();
		
		int length = params.length;
		Iterator<?>[] iterators = new Iterator<?>[length];
		for (int i = 0; i < length; i++) iterators[i] = params[i].iterator();

		boolean finished = false;
		while (true) {
			Object[] paramSet = new Object[length];
			for (int i = 0; i < length; i++) {
				if (!iterators[i].hasNext()) {
					finished = true;
					break;
				}
				paramSet[i] = iterators[i].next();
			}
			if (finished) break;
			results.add(closeOne(paramSet));
		}
		
		return results;
	}
	
	private List<Object[]> bindParams(Object... params) throws WrongClosureInvocationException {
		if (params == null || params.length == 0) {
			if (freeParamsNumber != 0)
				throw new WrongClosureInvocationException("Closure invoked without params instead of the expected " + freeParamsNumber);
			if (curriedParams == null) return null;
		}
		if (freeParamsNumber != params.length)
			throw new WrongClosureInvocationException("Closure invoked with " + params.length + " params instead of the expected " + freeParamsNumber);
		
		int paramCounter = 0; 
		int curriedParamCounter = 0;
		List<Object[]> boundParams = new ArrayList<Object[]>();
		for (Object[] args : argsList) {
			if (args == null) boundParams.add(null);
			else {
				Object[] objs = new Object[args.length];
				for (int i = 0; i < args.length; i++) {
					if (!isClosureArgPlaceholder(args[i])) objs[i] = args[i];
					else if (curriedParams != null && curriedParamsFlags[curriedParamCounter]) objs[i] = curriedParams[curriedParamCounter++];
					else {
						objs[i] = params[paramCounter++];
						curriedParamCounter++;
					}
				} 
				boundParams.add(objs);
			}
		}
		return boundParams;
	}
	
    /**
     * Curry this closure by fixing one of its free parameter to a given value.
     * @param curriedClosure The closure resulting from this curry operation
     * @param curried The value to which the free parameter should be curry
     * @param position The 1-based position of the parameter to which apply the curry operation
     * @return A Closure having a free parameter less than this one since one of them has been fixed to the given value
     * @throws IllegalArgumentException if this closure doesn't have a free parameter in the specified position
     */
	<T extends AbstractClosure> T curry(T curriedClosure, Object curried, int position) throws IllegalArgumentException {
		curriedClosure.closed = closed;
		curriedClosure.methodList = methodList;
		curriedClosure.argsList = argsList;
		curriedClosure.curriedParams = curriedParams;
		curriedClosure.curriedParamsFlags = curriedParamsFlags;
		curriedClosure.freeParamsNumber = freeParamsNumber;

		curriedClosure.curryParam(curried, position);
		return curriedClosure;
	}
	
	private void curryParam(Object curried, int position) throws IllegalArgumentException {
		if (curriedParams == null) {
			curriedParams = new Object[freeParamsNumber];
			curriedParamsFlags = new boolean[freeParamsNumber];
		}
		
		for (int i = 0; i < curriedParams.length; i++) {
			if (curriedParamsFlags[i]) continue;
			if (--position == 0) {
				curriedParams[i] = curried;
				curriedParamsFlags[i] = true;
				freeParamsNumber--;
				return;
			}
		}
		
		throw new IllegalArgumentException("Trying to curry this closure on an already bound or unexisting paramater");
	}
}
