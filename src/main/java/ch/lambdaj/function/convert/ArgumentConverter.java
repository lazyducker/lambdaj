package ch.lambdaj.function.convert;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;
import ch.lambdaj.function.argument.*;

public class ArgumentConverter<F, T> implements Converter<F, T>{

	private Argument argument;
	
	public ArgumentConverter(Argument argument) {
		this.argument = argument;
	}
	
	public ArgumentConverter(Object argument) {
		this(actualArgument(argument));
	}
	
	public T convert(F from) {
		return (T)argument.evaluate(from);
	}

}
