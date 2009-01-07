package ch.lambdaj.function.converter;

public interface Converter<F, T> {

	public T convert(F from);
}
