package ch.lambdaj.function.argument;

import java.lang.reflect.*;
import java.util.*;

import ch.lambdaj.util.*;

public class Invocation {

	private Class<?> invokedClass;
	private Method invokedMethod;
	private Object[] args;

	Invocation() { }
	
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
	
	public String getInkvokedPropertyName() {
		return IntrospectionUtil.getPropertyName(invokedMethod);
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
	
	@Override
	public int hashCode() {
		int hashCode = 13 * invokedClass.hashCode();
		if (invokedMethod != null) hashCode += 17 * invokedMethod.hashCode();
		if (args != null) hashCode += 19 + args.length;
		return hashCode;
	}
	
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Invocation)) return false;
		Invocation otherInvocation = (Invocation)object;
		if (!areNullSafeEquals(invokedClass, otherInvocation.getInvokedClass())) return false;
		if (!areNullSafeEquals(invokedMethod, otherInvocation.getInvokedMethod())) return false;
		return Arrays.equals(args, otherInvocation.args);
	}
	
	private boolean areNullSafeEquals(Object first, Object second) {
		if (first == null && second == null) return true;
		if (first == null || second == null) return false;
		return first.equals(second);
	}
}
