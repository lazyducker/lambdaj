package ch.lambdaj.function.argument;

public class ArgumentConversionException extends RuntimeException {

    ArgumentConversionException(String message) {
    	super(message);
    }

    ArgumentConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
