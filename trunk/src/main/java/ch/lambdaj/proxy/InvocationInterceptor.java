package ch.lambdaj.proxy;

import java.lang.reflect.*;
import java.lang.reflect.InvocationHandler;

import net.sf.cglib.proxy.*;

/**
 * @author Mario Fusco
 */
public abstract class InvocationInterceptor implements MethodInterceptor, InvocationHandler {

	public static interface VoidInterceptor { }
	
	public static final InvocationInterceptor VOID = new InvocationInterceptor() {
		public Object invoke(Object arg0, Method arg1, Object[] arg2) throws Throwable {
			return null;
		}
	};
	
	public final Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		return invoke(proxy, method, args);
	}
}
