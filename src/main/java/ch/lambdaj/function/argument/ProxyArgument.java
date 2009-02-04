package ch.lambdaj.function.argument;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;

import java.lang.reflect.*;
import java.util.concurrent.atomic.*;

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
		proxyId = placeholderCounter.addAndGet(1);
	}

	public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		String methodName = method.getName(); 
		if (methodName.equals("hashCode")) return invocationSequence.hashCode();
		if (methodName.equals("equals")) return invocationSequence.equals(args[0]);
		
		// Add this invocation to the current invocation sequence
		InvocationSequence currentInvocationSequence = new InvocationSequence(invocationSequence, new Invocation(proxiedClass, method, args));
		Class<?> returnClass = method.getReturnType();
		
		// Creates a new proxy propagating the invocation sequence
		return !Modifier.isFinal(returnClass.getModifiers()) ? 
				createArgument(rootArgumentId, returnClass, currentInvocationSequence) : 
				createPlaceholder(rootArgumentId, returnClass, currentInvocationSequence);
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof ProxyArgument ? proxyId == ((ProxyArgument)other).proxyId : false;
	}
	
	@Override
	public int hashCode() {
		return proxyId;
	}
	
	private Object createPlaceholder(Integer rootArgumentId, Class<?> clazz, InvocationSequence invocationSequence) {
		// If the returned class is final it just returns a dummy object (of the right class) that acts as a 
		// place holder allowing to bind this proxy argument to the actual one. This means that you can't do a further invocation
		// on this sequence since there is no way to generate a proxy for this object 
		Object result = getPlaceholder(invocationSequence);
		if (result == null) {
			result = createArgumentPlaceholder(clazz);
			// Binds the result to this argument. It will be used as a place holder to retrieve the argument itself
			bindPlaceholder(invocationSequence, result, new Argument(rootArgumentId, invocationSequence));
		}
		return result;
	}

	private static AtomicInteger placeholderCounter = new AtomicInteger(Integer.MIN_VALUE);
	
	public static Object createArgumentPlaceholder(Class<?> clazz) {
		Integer i = placeholderCounter.addAndGet(1);
		if (clazz.isPrimitive() || Number.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz)) return getPrimitivePlaceHolder(clazz, i);
		if (clazz == String.class) return String.valueOf(i);
		if (clazz.isArray()) return new Object[0];

		try {
			return clazz.newInstance();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Object getPrimitivePlaceHolder(Class<?> clazz, Integer i) {
		try {
			return ProxyArgument.class.getMethod(clazz.getSimpleName().substring(0, 3).toLowerCase() + "Placeholder", Integer.class).invoke(null, i);
		} catch (Exception e) {
			throw new RuntimeException("Unable to create placeholder", e);
		}
	}
	
	public static Integer intPlaceholder(Integer i) {
		return i;
	}

	public static Character chaPlaceholder(Integer i) {
		return Character.forDigit(i % Character.MAX_RADIX, Character.MAX_RADIX);
	}
	
	public static Byte bytPlaceholder(Integer i) {
		return Byte.valueOf(i.byteValue());
	}
	
	public static Short shoPlaceholder(Integer i) {
		return Short.valueOf(i.shortValue());
	}
	
	public static Long lonPlaceholder(Integer i) {
		return Long.valueOf(i.longValue());
	}

	public static Float floPlaceholder(Integer i) {
		return Float.valueOf(i.floatValue());
	}
	
	public static Double douPlaceholder(Integer i) {
		return Double.valueOf(i.doubleValue());
	}

	public static Boolean booPlaceholder(Integer i) {
		return i % 2 == 0 ? Boolean.TRUE : Boolean.FALSE;
	}

	public static Void voiPlaceholder(Integer i) {
		return null;
	}
}
