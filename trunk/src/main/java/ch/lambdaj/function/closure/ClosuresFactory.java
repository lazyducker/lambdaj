package ch.lambdaj.function.closure;

import ch.lambdaj.proxy.*;


public final class ClosuresFactory {
	
	private ClosuresFactory() { }

	private static ThreadLocal<Closure> closures = new ThreadLocal<Closure>();
	
	public static <T> T bindClosure(T closed, Class<T> closedClass) {
		Closure closure = closures.get();
		closure.setClosed(closed);
		return createProxyClosure(closure, closedClass);
	}
	
	static <T> T createProxyClosure(Closure closure, Class<T> closedClass) {
		return ProxyUtil.createProxy(new ProxyClosure(closure), closedClass);
	}
	
	public static Closure createClosure() {
		Closure closure = new Closure();
		closures.set(closure);
		return closure;
	}
	
}
