/**
 * // Modified or written by Ex Machina SAGL for inclusion with lambdaj.
 * // Copyright (c) 2008 Mario Fusco, Luca Marrocco.
 * // Licensed under the Apache License, Version 2.0 (the "License")
 */
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
