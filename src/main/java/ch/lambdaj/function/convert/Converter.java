package ch.lambdaj.function.convert;

public interface Converter<F, T> {

	T convert(F from);
}
