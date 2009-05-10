// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.aggregate;

/**
 * @author Mario Fusco
 */
public class Concat implements Aggregator<String> {

	private String separator;

	public Concat() {
		this(", ");
	}

	public Concat(String separator) {
		this.separator = separator;
	}

	public String aggregate(String first, String second) {
		boolean isFirstEmpty = empty(first);
		boolean isSecondEmpty = empty(second);
		
		if (isFirstEmpty && isSecondEmpty) return "";
		if (isFirstEmpty) return second;
		if (isSecondEmpty) return first;
		
		return new StringBuilder().append(first).append(separator).append(second).toString();
	}

	private boolean empty(String string) {
		return string == null || string.trim().equals("");
	}

	public String emptyItem() {
		return "";
	}
}
