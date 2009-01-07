package ch.lambdaj.function.converter;


public class DefaultStringConverter implements StringConverter<Object> {

	public String convert(Object from) {
		return from == null ? "" : from.toString();
	}

}
