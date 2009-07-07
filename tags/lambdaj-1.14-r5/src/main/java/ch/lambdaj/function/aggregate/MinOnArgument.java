package ch.lambdaj.function.aggregate;


public class MinOnArgument<T, A> extends ChooserOnArgument<T, A> {

	public MinOnArgument(A argument) {
		super(argument);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected T chooseOnArgument(T first, A firstArgument, T second, A secondArgument) {
		return ((Comparable)firstArgument).compareTo(secondArgument) < 0 ? first : second;
	}
}
