package ch.lambdaj.function.closure;

public class WrongClosureInvocationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	WrongClosureInvocationException(String message) {
		super(message);
	}

	WrongClosureInvocationException(String message, Throwable cause) {
		super(message, cause);
	}
}
