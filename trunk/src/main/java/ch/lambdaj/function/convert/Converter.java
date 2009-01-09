package ch.lambdaj.function.convert;

public interface Converter<F, T> {

	public T convert(F from);
}
