package ch.lambdaj.function.argument;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;

import java.lang.reflect.*;

import net.sf.cglib.proxy.*;

public class ProxyArgument implements MethodInterceptor {
	
	private Integer rootArgumentId;
	
	private Class<?> proxiedClass;
	
	private InvocationSequence invocationSequence;
	
	private int proxyId;
	
	ProxyArgument(Integer rootArgumentId, Class<?> proxiedClass, InvocationSequence invocationSequence) {
		this.rootArgumentId = rootArgumentId;
		this.proxiedClass = proxiedClass;
		this.invocationSequence = invocationSequence;
		synchronized (placeholderCounter) { proxyId = placeholderCounter++; }
	}

	public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		String methodName = method.getName(); 
		if (methodName.equals("hashCode")) return hashCode();
		if (methodName.equals("equals")) return equals(args[0]);
		
		// Add this invocation to the current invocation sequence
		InvocationSequence currentInvocationSequence = new InvocationSequence(invocationSequence, new Invocation(proxiedClass, method, args));
		
		Class<?> returnClass = method.getReturnType();
		Object result = null;
		if (!Modifier.isFinal(returnClass.getModifiers())) {
			// Creates a new proxy propagating the invocation sequence
			result = createArgument(rootArgumentId, returnClass, currentInvocationSequence);
		} else {
			// If the returned class is final it just returns a dummy object (of the right class) that acts as a 
			// place holder allowing to bind this proxy argument to the actual one. This means that you can't do a further invocation
			// on this sequence since there is no way to generate a proxy for this object 
			result = getPlaceholder(currentInvocationSequence);
			if (result == null) {
				result = createArgumentPlaceholder(returnClass);
				// Binds the result to this argument. It will be used as a place holder to retrieve the argument itself
				bindPlaceholder(currentInvocationSequence, result, new Argument(rootArgumentId, currentInvocationSequence));
			}
		}
		
		return result;
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof ProxyArgument ? proxyId == ((ProxyArgument)other).proxyId : false;
	}
	
	@Override
	public int hashCode() {
		return proxyId;
	}

	private static Integer placeholderCounter = Integer.MIN_VALUE;
	
	private Object createArgumentPlaceholder(Class<?> clazz) {
		Integer i = 0;
		synchronized (placeholderCounter) { i = placeholderCounter++; }
		
		if (clazz.isPrimitive()) {
			if (clazz == Void.TYPE) return null;
			if (clazz == Integer.TYPE) return i;
			if (clazz == Character.TYPE) return Character.forDigit(i % Character.MAX_RADIX, Character.MAX_RADIX);
			if (clazz == Byte.TYPE) return new Byte(i.byteValue());
			if (clazz == Short.TYPE) return new Short(i.shortValue());
			if (clazz == Long.TYPE) return new Long(i.longValue());
			if (clazz == Float.TYPE) return new Float(i.floatValue());
			if (clazz == Double.TYPE) return new Double(i.doubleValue());
			if (clazz == Boolean.TYPE) return i % 2 == 0 ? Boolean.TRUE : Boolean.FALSE;
		}
		
		if (clazz.isAssignableFrom(String.class)) return "" + i;
		if (clazz.isArray()) return new Object[0];

		try {
			return clazz.newInstance();
		} catch (Exception e) {}
		return null;
	}
}
