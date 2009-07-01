package ch.lambdaj.proxy;

import net.sf.cglib.proxy.*;

/**
 * @author Mario Fusco
 * @author Sebastian Jancke
 */
public final class ProxyUtil {
	
	private ProxyUtil() { }
	
	@SuppressWarnings("unchecked")
	public static <T> T createProxy(InvocationInterceptor interceptor, Class<T> clazz) {
		if (clazz.isPrimitive()) return null;
		if (clazz == String.class) clazz = (Class<T>)CharSequence.class;
			
		if (clazz.isInterface()) 
			return (T)java.lang.reflect.Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz, Iterable.class }, interceptor);
		
		try {
			return (T)createEnhancer(interceptor, clazz, new Class[] { Iterable.class }).create();
		} catch (IllegalArgumentException iae) {
			if (ClassImposterizer.INSTANCE.canImposterise(clazz)) return ClassImposterizer.INSTANCE.imposterise(interceptor, clazz, Iterable.class);
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
}
