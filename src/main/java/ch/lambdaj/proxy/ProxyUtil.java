package ch.lambdaj.proxy;

import java.lang.reflect.*;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * @author Mario Fusco
 * @author Sebastian Jancke
 */
public final class ProxyUtil {
	
	private ProxyUtil() { }
	
	public static <T> T createProxy(InvocationInterceptor interceptor, Class<T> clazz) {
		return createProxy(interceptor, clazz, false);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T createProxy(InvocationInterceptor interceptor, Class<T> clazz, boolean failSafe) {
		if (clazz.isPrimitive()) return null;
		if (clazz == String.class) clazz = (Class<T>)CharSequence.class;
			
		if (clazz.isInterface()) return (T)createNativeJavaProxy(clazz.getClassLoader(), interceptor, clazz);
		
		try {
			return (T)createEnhancer(interceptor, clazz, new Class[] { Iterable.class }).create();
		} catch (IllegalArgumentException iae) {
			if (Proxy.isProxyClass(clazz)) return (T)createNativeJavaProxy(clazz.getClassLoader(), interceptor, clazz.getInterfaces());
			if (ClassImposterizer.INSTANCE.canImposterise(clazz)) return ClassImposterizer.INSTANCE.imposterise(interceptor, clazz, Iterable.class);
			if (failSafe) return null;
			throw new UnproxableClassException(clazz, iae);
		}
	}

	private static Enhancer createEnhancer(MethodInterceptor interceptor, Class<?> clazz, Class<?>[] interfaces) {
		Enhancer enhancer = new Enhancer();
		enhancer.setCallback(interceptor);
		enhancer.setSuperclass(clazz);
		enhancer.setInterfaces(interfaces);
		return enhancer;
	}
	
	private static Object createNativeJavaProxy(ClassLoader classLoader, InvocationHandler interceptor, Class<?> ... interfaces) {
		Class<?>[] proxyInterfaces = new Class[interfaces.length + 1];
		proxyInterfaces[0] = Iterable.class;
		System.arraycopy(interfaces, 0, proxyInterfaces, 1, interfaces.length);
		return Proxy.newProxyInstance(classLoader, proxyInterfaces, interceptor);
	}
}
