package ch.lambdaj.function.aggregate;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;
import ch.lambdaj.function.argument.*;

public abstract class ChooserOnArgument<T> extends Chooser<T> {

	private Argument argument;
	
	public ChooserOnArgument(Object argument) {
		this.argument = actualArgument(argument);
	}

	@Override
	protected T choose(T first, T second) {
		Object firstArgument = argument.evaluate(first);
		if (firstArgument == null) return second;
		Object secondArgument = argument.evaluate(second);
		if (secondArgument == null) return first;
		return chooseOnArgument(first, firstArgument, second, secondArgument);
	}

	protected abstract T chooseOnArgument(T first, Object firstArgument, T second, Object secondArgument);
}
