package ch.lambdaj.function.convert;


public class DefaultStringConverter implements StringConverter<Object> {

	public String convert(Object from) {
		return from == null ? "" : from.toString();
	}

}
