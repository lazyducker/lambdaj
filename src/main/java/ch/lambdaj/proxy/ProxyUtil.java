package ch.lambdaj.proxy;

import static java.lang.reflect.Proxy.*;
import static java.lang.reflect.Modifier.*;

import net.sf.cglib.proxy.*;

public class ProxyUtil {
	
	private ProxyUtil() { }
	
	public static <T> T createProxy(MethodInterceptor interceptor, Class<T> clazz) {
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

	private static <T> boolean canBeEnhanced(Class<T> clazz) {
		if (isProxyClass(clazz) || isFinal(clazz.getModifiers())) return false;
		try {
			clazz.getConstructor(null);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
