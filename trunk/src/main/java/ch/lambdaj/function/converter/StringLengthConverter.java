package ch.lambdaj.function.converter;


public class StringLengthConverter extends PropertyExtractor<String, Integer> {

	public StringLengthConverter() {
		super("length");
	}
}
