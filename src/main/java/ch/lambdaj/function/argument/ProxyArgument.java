package ch.lambdaj.function.argument;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;

import java.lang.ref.*;
import java.lang.reflect.*;

import net.sf.cglib.proxy.*;

public class ProxyArgument implements MethodInterceptor {
	
	private Class<?> proxiedClass;
	
	private WeakReference<InvocationSequence> invocationSequence;
	
	private int proxyId;
	
	ProxyArgument(Class<?> proxiedClass, InvocationSequence invocationSequence) {
		this.proxiedClass = proxiedClass;
		this.invocationSequence = new WeakReference<InvocationSequence>(invocationSequence);
		proxyId = getNextPlaceholderId();
	}

	public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
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
		return other instanceof ProxyArgument ? proxyId == ((ProxyArgument)other).proxyId : false;
	}
	
	@Override
	public int hashCode() {
		return proxyId;
	}
}
