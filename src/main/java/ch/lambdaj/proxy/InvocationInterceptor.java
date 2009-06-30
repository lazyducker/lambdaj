package ch.lambdaj.proxy;

import java.lang.reflect.*;
import java.lang.reflect.InvocationHandler;

import net.sf.cglib.proxy.*;

/**
 * @author Mario Fusco
 */
public abstract class InvocationInterceptor implements MethodInterceptor, InvocationHandler {

	public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		return invoke(proxy, method, args);
	}

}
