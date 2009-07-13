package ch.lambdaj.function.argument;

import java.lang.reflect.*;

public class InvocationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	InvocationException(Throwable t, Method invokedMethod, Object object) {
		super("Failed invocation of " + invokedMethod + " on object " + object + " caused by: " + t.getLocalizedMessage(), t);
	}
}
