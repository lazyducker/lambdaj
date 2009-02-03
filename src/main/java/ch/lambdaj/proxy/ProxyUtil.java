package ch.lambdaj.proxy;

import static java.lang.reflect.Proxy.*;
import static java.lang.reflect.Modifier.*;

import net.sf.cglib.proxy.*;

public final class ProxyUtil {
	
	private ProxyUtil() { }
	
	public static <T> T createProxy(MethodInterceptor interceptor, Class<T> clazz) {
		try {
			Enhancer e = new Enhancer();
			if (clazz.isInterface()) e.setInterfaces(new Class[] { clazz, Iterable.class });
			else if (!canBeEnhanced(clazz)) proxyFinalClass(e, clazz);
			else {
				e.setSuperclass(clazz);
				e.setInterfaces(new Class[] { Iterable.class });
			}
			e.setCallback(interceptor);
			return (T)e.create();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> void proxyFinalClass(Enhancer e, Class<T> clazz) {
		Class<?>[] interfaces = clazz.getInterfaces();
		Class<?>[] proxyInterfaces = new Class[interfaces.length + 1];
		proxyInterfaces[0] = Iterable.class;
		System.arraycopy(interfaces, 0, proxyInterfaces, 1, interfaces.length);
		e.setInterfaces(proxyInterfaces);
	}
	
	private static <T> boolean canBeEnhanced(Class<T> clazz) {
		if (isProxyClass(clazz) || isFinal(clazz.getModifiers())) return false;
		try {
			clazz.getConstructor((Class[])null);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
