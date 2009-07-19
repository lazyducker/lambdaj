package ch.lambdaj.function.closure;

import ch.lambdaj.proxy.*;

/**
 * @author Mario Fusco
 */
public final class ClosuresFactory {
	
	private ClosuresFactory() { }

	private static ThreadLocal<AbstractClosure> closures = new ThreadLocal<AbstractClosure>();
	
	public static <T> T bindClosure(T closed, Class<T> closedClass) {
		AbstractClosure closure = closures.get();
		closure.setClosed(closed);
		return createProxyClosure(closure, closedClass);
	}
	
	static <T> T createProxyClosure(AbstractClosure closure, Class<T> closedClass) {
		return ProxyUtil.createProxy(new ProxyClosure(closure), closedClass, true);
	}
	
	public static Closure createClosure() {
		Closure closure = new Closure();
		closures.set(closure);
		return closure;
	}
	
	public static <A> Closure1<A> createClosure(Class<A> type1) {
		Closure1<A> closure = new Closure1<A>();
		closures.set(closure);
		return closure;
	}

	public static <A, B> Closure2<A, B> createClosure(Class<A> type1, Class<B> type2) {
		Closure2<A, B> closure = new Closure2<A, B>();
		closures.set(closure);
		return closure;
	}

	public static <A, B, C> Closure3<A, B, C> createClosure(Class<A> type1, Class<B> type2, Class<C> type3) {
		Closure3<A, B, C> closure = new Closure3<A, B, C>();
		closures.set(closure);
		return closure;
	}
}
