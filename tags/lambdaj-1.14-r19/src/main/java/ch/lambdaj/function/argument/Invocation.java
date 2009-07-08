package ch.lambdaj.function.argument;

import java.lang.ref.*;
import java.lang.reflect.*;
import java.util.*;

import ch.lambdaj.util.*;

class Invocation {

	private Class<?> invokedClass;
	private Method invokedMethod;
	private String inkvokedPropertyName;
	private List<WeakReference<Object>> weakArgs;

	Invocation() { }
	
	Invocation(Class<?> invokedClass, Method invokedMethod, Object[] args) {
		this.invokedClass = invokedClass;
		this.invokedMethod = invokedMethod;
		if (args != null && args.length > 0) {
			weakArgs = new ArrayList<WeakReference<Object>>();
			for (Object arg : args) weakArgs.add(new WeakReference<Object>(arg));
		}
	}
	
	private Object[] getConcreteArgs() {
		if (weakArgs == null) return null;
		Object[] args = new Object[weakArgs.size()];
		int i = 0;
		for (WeakReference<Object> weakArg : weakArgs) args[i++] = weakArg.get();
		return args;
	}

	Class<?> getInvokedClass() {
		return invokedClass;
	}

	Method getInvokedMethod() {
		return invokedMethod;
	}
	
	Class<?> getReturnType() {
		return invokedMethod.getReturnType();
	}
	
	String getInkvokedPropertyName() {
		if (inkvokedPropertyName == null) inkvokedPropertyName = IntrospectionUtil.getPropertyName(invokedMethod);
		return inkvokedPropertyName;
	}

	Object invokeOn(Object object) throws InvocationException {
		if (invokedMethod == null) return object;
		Object result = null;
		try {
			result = invokedMethod.invoke(object, getConcreteArgs());
		} catch (Exception e) {
			throw new InvocationException(e, invokedMethod, object);
		}
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(invokedMethod.toString());
		Object[] args = getConcreteArgs();
		if (args != null) {
			sb.append("with args ");
			for (int i = 0; i < args.length; i++) sb.append(i == 0 ? "" : ", ").append(args[i]);
		}
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		int hashCode = 13 * invokedClass.hashCode();
		if (invokedMethod != null) hashCode += 17 * invokedMethod.hashCode();
		if (weakArgs != null) hashCode += 19 + weakArgs.size();
		return hashCode;
	}
	
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Invocation)) return false;
		Invocation otherInvocation = (Invocation)object;
		if (!areNullSafeEquals(invokedClass, otherInvocation.getInvokedClass())) return false;
		if (!areNullSafeEquals(invokedMethod, otherInvocation.getInvokedMethod())) return false;
		return Arrays.equals(getConcreteArgs(), otherInvocation.getConcreteArgs());
	}
	
	private boolean areNullSafeEquals(Object first, Object second) {
		if (first == null && second == null) return true;
		if (first == null || second == null) return false;
		return first.equals(second);
	}
}
