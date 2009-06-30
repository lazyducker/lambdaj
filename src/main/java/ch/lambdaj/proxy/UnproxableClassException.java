package ch.lambdaj.proxy;

import java.lang.reflect.*;

public class UnproxableClassException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	UnproxableClassException(Class<?> clazz, Exception cause) {
		super(Modifier.isFinal(clazz.getModifiers()) ? 
				"Unable to proxy the final class " + clazz.getName() : 
				"Unable to proxy the class without empty constructor " + clazz.getName(), 
				cause);
	}
}
