package ch.lambdaj.function.converter;


public class StringPropertyExtractor<T> extends PropertyExtractor<T, String> implements StringConverter<T> {
	
	public StringPropertyExtractor(String propertyName) {
		super(propertyName); 
	}

	@Override
	public String convert(T from) {
		Object converted = super.convert(from); 
		return converted == null ? null : converted.toString();
	}
}
