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
    
	public static <T> T createProxy(InvocationInterceptor interceptor, Class<T> clazz) {
		return createProxy(interceptor, clazz, false);
	}
	
	public static <T> T createProxy(InvocationInterceptor interceptor, Class<T> clazz, boolean failSafe) {
		if (clazz.isPrimitive()) return null;
		if (clazz == String.class) clazz = (Class<T>)CharSequence.class;
		return createProxyImplementingInterface(interceptor, clazz, failSafe, Iterable.class);
	}

    // ////////////////////////////////////////////////////////////////////////
    // /// Private
    // ////////////////////////////////////////////////////////////////////////
    
	private static <T> T createProxyImplementingInterface(InvocationInterceptor interceptor, Class<T> clazz, boolean failSafe, Class<?> implementedInterface) {
		if (clazz.isInterface()) return (T)createNativeJavaProxy(clazz.getClassLoader(), interceptor, Iterable.class, clazz);

		try {
			return (T)createEnhancer(interceptor, clazz, new Class[] { implementedInterface }).create();
		} catch (IllegalArgumentException iae) {
			if (Proxy.isProxyClass(clazz)) return (T)createNativeJavaProxy(clazz.getClassLoader(), interceptor, Iterable.class, clazz.getInterfaces());
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
	
	private static Object createNativeJavaProxy(ClassLoader classLoader, InvocationHandler interceptor, Class<?> implementedInterface, Class<?> ... interfaces) {
		Class<?>[] proxyInterfaces = new Class[interfaces.length + 1];
		proxyInterfaces[0] = implementedInterface;
		System.arraycopy(interfaces, 0, proxyInterfaces, 1, interfaces.length);
		return Proxy.newProxyInstance(classLoader, proxyInterfaces, interceptor);
	}
}
