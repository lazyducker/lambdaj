package ch.lambdaj.proxy;

import net.sf.cglib.proxy.*;

/**
 * @author Mario Fusco
 */
public final class ProxyUtil {
	
	private ProxyUtil() { }
	
	@SuppressWarnings("unchecked")
	public static <T> T createProxy(MethodInterceptor interceptor, Class<T> clazz) {
		Enhancer enhancer = createEnhancer(interceptor);
		if (clazz.isInterface()) {
			enhancer.setInterfaces(new Class[] { clazz, Iterable.class });
		} else {
			enhancer.setSuperclass(clazz);
			enhancer.setInterfaces(new Class[] { Iterable.class });
		}
		try {
			return (T)enhancer.create();
		} catch (IllegalArgumentException iae) {
			return (T)createProxyForFinalClass(interceptor, clazz);
		}
	}

	private static Object createProxyForFinalClass(MethodInterceptor interceptor, Class<?> clazz) {
		Enhancer enhancer = createEnhancer(interceptor);
		Class<?>[] interfaces = clazz.getInterfaces();
		Class<?>[] proxyInterfaces = new Class[interfaces.length + 1];
		proxyInterfaces[0] = Iterable.class;
		System.arraycopy(interfaces, 0, proxyInterfaces, 1, interfaces.length);
		enhancer.setInterfaces(proxyInterfaces);
		return enhancer.create();
	}
	
	private static Enhancer createEnhancer(MethodInterceptor interceptor) {
		Enhancer enhancer = new Enhancer();
		enhancer.setCallback(interceptor);
		return enhancer;
	}
/*	
	@SuppressWarnings("unchecked")
	public static <T> T createProxy(InvocationInterceptor interceptor, Class<T> clazz) {
		if (clazz.isPrimitive()) return null;
		if (clazz == String.class) clazz = (Class<T>)CharSequence.class;
			
		if (clazz.isInterface()) 
			return (T)java.lang.reflect.Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz, Iterable.class }, interceptor);
		
		try {
			return (T)createEnhancer(interceptor, clazz, new Class[] { Iterable.class }).create();
		} catch (IllegalArgumentException iae) {
			return (T)createProxyForFinalClass(interceptor, clazz);
//			throw new UnproxableClassException(clazz, iae);
		}
	}

	private static Enhancer createEnhancer(MethodInterceptor interceptor, Class<?> clazz, Class<?>[] interfaces) {
		Enhancer enhancer = new Enhancer();
		enhancer.setCallback(interceptor);
		enhancer.setSuperclass(clazz);
		enhancer.setInterfaces(interfaces);
		return enhancer;
	}
	
	static Object createProxyForFinalClass(MethodInterceptor interceptor, Class<?> clazz) {
		Enhancer enhancer = new Enhancer();
		enhancer.setCallback(interceptor);
		Class<?>[] interfaces = clazz.getInterfaces();
		Class<?>[] proxyInterfaces = new Class[interfaces.length + 1];
		proxyInterfaces[0] = Iterable.class;
		System.arraycopy(interfaces, 0, proxyInterfaces, 1, interfaces.length);
		enhancer.setInterfaces(proxyInterfaces);
		return enhancer.create();
	}
*/	
}
