package ch.lambdaj.proxy;

import java.lang.reflect.*;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * @author Mario Fusco
 * @author Sebastian Jancke
 */
@SuppressWarnings("unchecked")
public final class ProxyUtil {
	
	private ProxyUtil() { }
	
    public static boolean isProxable(Class<?> clazz) {
        return !clazz.isPrimitive() && !Modifier.isFinal(clazz.getModifiers()) && !clazz.isAnonymousClass();
    }
	
    // ////////////////////////////////////////////////////////////////////////
    // /// Void Proxy
    // ////////////////////////////////////////////////////////////////////////
    
	public static boolean isVoidProxy(Object object) {
		return object instanceof InvocationInterceptor.VoidInterceptor;
	}
	
	public static <T> T createVoidProxy(Class<T> clazz) {
		return createProxyImplementingInterface(InvocationInterceptor.VOID, clazz, false, InvocationInterceptor.VoidInterceptor.class);
	}
	
    // ////////////////////////////////////////////////////////////////////////
    // /// Iterable Proxy
    // ////////////////////////////////////////////////////////////////////////
    
	public static <T> T createIterableProxy(InvocationInterceptor interceptor, Class<T> clazz) {
		return createIterableProxy(interceptor, clazz, false);
	}
	
	public static <T> T createIterableProxy(InvocationInterceptor interceptor, Class<T> clazz, boolean failSafe) {
		if (clazz.isPrimitive()) return null;
		if (clazz == String.class) clazz = (Class<T>)CharSequence.class;
		return createProxyImplementingInterface(interceptor, clazz, failSafe, Iterable.class);
	}

    // ////////////////////////////////////////////////////////////////////////
    // /// Private
    // ////////////////////////////////////////////////////////////////////////
    
	private static <T> T createProxyImplementingInterface(InvocationInterceptor interceptor, Class<T> clazz, boolean failSafe, Class<?> implementedInterface) {
		if (clazz.isInterface()) return (T)createNativeJavaProxy(clazz.getClassLoader(), interceptor, implementedInterface, clazz);

		try {
			return (T)createEnhancer(interceptor, clazz, new Class[] { implementedInterface }).create();
		} catch (IllegalArgumentException iae) {
			if (Proxy.isProxyClass(clazz)) return (T)createNativeJavaProxy(clazz.getClassLoader(), interceptor, concatClasses(implementedInterface, clazz.getInterfaces()));
			if (isProxable(clazz)) return ClassImposterizer.INSTANCE.imposterise(interceptor, clazz, implementedInterface);
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
		return Proxy.newProxyInstance(classLoader, interfaces, interceptor);
	}
	
	private static Class<?>[] concatClasses(Class<?> first, Class<?> ... interfaces) {
		Class<?>[] concatClasses = new Class[interfaces.length + 1];
		concatClasses[0] = first;
		System.arraycopy(interfaces, 0, concatClasses, 1, interfaces.length);
		return concatClasses;
	}
}
