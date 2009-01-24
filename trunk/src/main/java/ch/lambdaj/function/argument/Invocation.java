package ch.lambdaj.function.argument;

import java.lang.reflect.*;

public class Invocation {

	private Class<?> invokedClass;
	private Method invokedMethod;
	private Object[] args;

	Invocation(Class<?> invokedClass) {
		this(invokedClass, null, null);
	}
			
	Invocation(Class<?> invokedClass, Method invokedMethod, Object[] args) {
		this.invokedClass = invokedClass;
		this.invokedMethod = invokedMethod;
		this.args = args;
	}

	public Class<?> getInvokedClass() {
		return invokedClass;
	}

	public Method getInvokedMethod() {
		return invokedMethod;
	}

	public Object[] getArgs() {
		return args;
	}
	
	public Object invokeOn(Object object) throws InvocationException {
		if (!invokedClass.isInstance(object)) throw new InvocationException(invokedClass, object);
		if (invokedMethod == null) return object;
		Object result = null;
		try {
			result = invokedMethod.invoke(object, args);
		} catch (Exception e) {
			throw new InvocationException(e, invokedMethod, object);
		}
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(invokedMethod.toString());
		if (args != null && args.length > 0) {
			sb.append("with args ");
			for (int i = 0; i < args.length; i++) sb.append(i == 0 ? "" : ", ").append(args[i]);
		}
		return sb.toString();
	}
}
