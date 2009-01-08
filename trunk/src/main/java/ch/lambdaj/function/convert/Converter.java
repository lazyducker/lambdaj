/**
 * // Modified or written by Ex Machina SAGL for inclusion with lambdaj.
 * // Copyright (c) 2008 Mario Fusco, Luca Marrocco.
 * // Licensed under the Apache License, Version 2.0 (the "License")
 */
package ch.lambdaj.function.convert;

public interface Converter<F, T> {

	public T convert(F from);
}
