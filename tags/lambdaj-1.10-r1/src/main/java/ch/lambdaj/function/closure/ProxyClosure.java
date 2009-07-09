package ch.lambdaj.function.closure;

import static ch.lambdaj.function.closure.ClosuresFactory.*;

import java.lang.reflect.*;

import ch.lambdaj.proxy.*;

public class ProxyClosure extends InvocationInterceptor {

	private Closure closure;
	
	protected ProxyClosure(Closure closure) {
		this.closure = closure;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		closure.registerInvocation(method, args);
		return createProxyClosure(closure, method.getReturnType());
	}
}
