package ch.lambdaj.proxy;

/**
 * @author Mario Fusco
 */
public class UnproxableClassException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	UnproxableClassException(Class<?> clazz, Exception cause) {
		super("Unable to proxy the final class " + clazz.getName(),	cause);
	}
}
