package ch.lambdaj.function.aggregate;


public class MinOnArgument<T> extends ChooserOnArgument<T> {

	public MinOnArgument(Object argument) {
		super(argument);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected T chooseOnArgument(T first, Object firstArgument, T second, Object secondArgument) {
		return ((Comparable)firstArgument).compareTo(secondArgument) < 0 ? first : second;
	}
}
