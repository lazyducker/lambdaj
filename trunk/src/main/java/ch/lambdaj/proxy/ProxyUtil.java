package ch.lambdaj.proxy;

import java.lang.reflect.*;
import java.lang.reflect.Proxy;

import net.sf.cglib.proxy.*;

public class ProxyUtil {
	
	private ProxyUtil() { }
	
	public static <T> T createProxy(MethodInterceptor interceptor, Class<?> clazz) {
		try {
			Enhancer e = new Enhancer();
			if (clazz.isInterface()) {
				e.setInterfaces(new Class[] { clazz, Iterable.class });
			} else if (!canBeEnhanced(clazz)) {
				Class<?>[] interfaces = clazz.getInterfaces();
				Class<?>[] proxyInterfaces = new Class[interfaces.length + 1];
				proxyInterfaces[0] = Iterable.class;
				for (int i = 0; i < interfaces.length; i++)
					proxyInterfaces[i + 1] = interfaces[i];
				e.setInterfaces(proxyInterfaces);
			} else {
				e.setSuperclass(clazz);
				e.setInterfaces(new Class[] { Iterable.class });
			}
			e.setCallback(interceptor);
			Object bean = e.create();
			return (T) bean;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static boolean canBeEnhanced(Class<?> clazz) {
		if (Proxy.isProxyClass(clazz) || Modifier.isFinal(clazz.getModifiers())) return false;
		try {
			clazz.getConstructor(null);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
