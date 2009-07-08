package ch.lambdaj.function.argument;

import java.util.*;

class InvocationSequence extends ArrayList<Invocation> {

	private static final long serialVersionUID = 1L;
	
	private Class<?> rootInvokedClass;
	
	private String inkvokedPropertyName = "";
	
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
		inkvokedPropertyName = sequence.getInkvokedPropertyName() + (sequence.isEmpty() ? "" : ".") + invocation.getInkvokedPropertyName();
	}
	
	Class<?> getRootInvokedClass() {
		return rootInvokedClass;
	}
	
	String getInkvokedPropertyName() {
		return inkvokedPropertyName;
	}
	
	Class<?> getReturnType() {
		return get(size()-1).getReturnType();
	}
	
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof InvocationSequence)) return false;
		InvocationSequence othersSequence = (InvocationSequence)object;
		if (!rootInvokedClass.equals(othersSequence.getRootInvokedClass())) return false;
		if (size() != othersSequence.size()) return false;
		for (int i = 0; i < size(); i++) if (!get(i).equals(othersSequence.get(i))) return false;
		return true;
	}
	
	int[] primes = new int[] { 13, 17, 19, 23, 29, 31, 37 };
	
	@Override
	public int hashCode() {
		int hashCode = 11 * rootInvokedClass.hashCode();
		for (int i = 0; i < size(); i++) hashCode += primes[i % primes.length] * get(i).hashCode();
		return hashCode;
	}
}
