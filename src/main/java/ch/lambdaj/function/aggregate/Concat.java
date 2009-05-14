// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.aggregate;

/**
 * @author Mario Fusco
 */
public class Concat implements Aggregator<Object> {

	private String separator;

	public Concat() {
		this(", ");
	}

	public Concat(String separator) {
		this.separator = separator;
	}

	public Object aggregate(Object first, Object second) {
		if (empty(first) && empty(second)) return emptyItem();
		if (empty(first)) return second;
		if (empty(second)) return first;
		return new StringBuilder().append(first.toString()).append(separator).append(second.toString()).toString();
	}

	private boolean empty(Object object) {
		if (object == null) return true;
		if (object.toString().trim().equals("")) return true;
		return false;
	}

	public String emptyItem() {
		return "";
	}
}
