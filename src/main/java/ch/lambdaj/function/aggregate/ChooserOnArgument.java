package ch.lambdaj.function.aggregate;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;
import ch.lambdaj.function.argument.*;

public abstract class ChooserOnArgument<T, A> extends Chooser<T> {

	private final Argument<A> argument;
	
	public ChooserOnArgument(A argument) {
		this.argument = actualArgument(argument);
	}

	@Override
	protected T choose(T first, T second) {
		A firstArgument = argument.evaluate(first);
		if (firstArgument == null) return second;
		A secondArgument = argument.evaluate(second);
		if (secondArgument == null) return first;
		return chooseOnArgument(first, firstArgument, second, secondArgument);
	}

	protected abstract T chooseOnArgument(T first, A firstArgument, T second, A secondArgument);
}
