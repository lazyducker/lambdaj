// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.argument;

import java.util.*;

/**
 * Registers a sequence of method invocations
 * @author Mario Fusco
 */
final class InvocationSequence extends ArrayList<Invocation> {

	private static final long serialVersionUID = 1L;
	
	private final Class<?> rootInvokedClass;
	
	private String inkvokedPropertyName;
	
	InvocationSequence(Class<?> rootInvokedClass) { 
		this.rootInvokedClass = rootInvokedClass;
	}
	
	private InvocationSequence(InvocationSequence sequence) {
		this(sequence.getRootInvokedClass());
		addAll(sequence);
	}

	InvocationSequence(InvocationSequence sequence, Invocation invocation) {
		this(sequence);
		add(invocation);
	}
	
	Class<?> getRootInvokedClass() {
		return rootInvokedClass;
	}
	
	String getInkvokedPropertyName() {
        if (inkvokedPropertyName == null) inkvokedPropertyName = calcInkvokedPropertyName();
		return inkvokedPropertyName;
	}

    private String calcInkvokedPropertyName() {
        if (isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (Invocation invocation : this) { sb.append(".").append(invocation.getInvokedPropertyName()); }
        return sb.substring(1);
    }
	
	Class<?> getReturnType() {
		return get(size()-1).getReturnType();
	}
	
    /**
     * {@inheritDoc}
     */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof InvocationSequence)) return false;
		InvocationSequence othersSequence = (InvocationSequence)object;
		if (!rootInvokedClass.equals(othersSequence.getRootInvokedClass())) return false;
		if (size() != othersSequence.size()) return false;
		for (int i = 0; i < size(); i++) { if (!get(i).equals(othersSequence.get(i))) return false; }
		return true;
	}
	
	final int[] primes = new int[] { 13, 17, 19, 23, 29, 31, 37 };
	
    /**
     * {@inheritDoc}
     */
	@Override
	public int hashCode() {
		int hashCode = 11 * rootInvokedClass.hashCode();
		for (int i = 0; i < size(); i++) { hashCode += primes[i % primes.length] * get(i).hashCode(); }
		return hashCode;
	}
}
