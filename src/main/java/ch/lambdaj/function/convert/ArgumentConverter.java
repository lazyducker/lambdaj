// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.convert;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;
import ch.lambdaj.function.argument.*;

public class ArgumentConverter<F, T> implements Converter<F, T>{

	private Argument argument;
	
	public ArgumentConverter(Argument argument) {
		this.argument = argument;
	}
	
	public ArgumentConverter(T argument) {
		this(actualArgument(argument));
	}
	
	@SuppressWarnings("unchecked")
	public T convert(F from) {
		return (T)argument.evaluate(from);
	}

}
