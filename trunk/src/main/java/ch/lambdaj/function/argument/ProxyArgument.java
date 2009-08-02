// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.argument;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;

import java.lang.ref.*;
import java.lang.reflect.*;

import ch.lambdaj.proxy.*;

/**
 * @author Mario Fusco
 */
class ProxyArgument extends InvocationInterceptor {
	
	private final Class<?> proxiedClass;
	
	private final WeakReference<InvocationSequence> invocationSequence;
	
	private final int proxyId;
	
	ProxyArgument(Class<?> proxiedClass, InvocationSequence invocationSequence) {
		this.proxiedClass = proxiedClass;
		this.invocationSequence = new WeakReference<InvocationSequence>(invocationSequence);
		proxyId = getNextPlaceholderId();
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String methodName = method.getName(); 
		if (methodName.equals("hashCode")) return invocationSequence.hashCode();
		if (methodName.equals("equals")) return invocationSequence.equals(args[0]);
		
		// Add this invocation to the current invocation sequence
		InvocationSequence currentInvocationSequence = new InvocationSequence(invocationSequence.get(), new Invocation(proxiedClass, method, args));
		Class<?> returnClass = method.getReturnType();
		
		// Creates a new proxy propagating the invocation sequence
		return createArgument(returnClass, currentInvocationSequence);
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof ProxyArgument && proxyId == ((ProxyArgument)other).proxyId;
	}
	
	@Override
	public int hashCode() {
		return proxyId;
	}
}
