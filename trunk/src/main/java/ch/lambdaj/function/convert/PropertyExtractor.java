package ch.lambdaj.function.convert;

import ch.lambdaj.util.IntrospectionUtil;


public class PropertyExtractor<F, T> implements Converter<F, T> {

	private String propertyName;
	
	public PropertyExtractor(String propertyName) {
		this.propertyName = propertyName; 
	}
	
	public T convert(F from) {
		return (T)IntrospectionUtil.getPropertyValue(from, propertyName);
	}

}
