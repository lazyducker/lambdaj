package ch.lambdaj.function.argument;

import java.util.*;

public class Argument implements Iterable<Invocation> {

	private InvocationSequence invocationSequence;
	
	private Integer rootArgumentId;
	
	Argument(Integer rootArgumentId, InvocationSequence invocationSequence) {
		this.rootArgumentId = rootArgumentId;
		this.invocationSequence = invocationSequence;
	}
	
	public InvocationSequence getInvocationSequence() {
		return invocationSequence;
	}

	public Iterator<Invocation> iterator() {
		return invocationSequence.iterator();
	}

	public Object evaluate(Object object) {
//		if (object instanceof JoinedObject) object = ((JoinedObject)object).getObjectByClass(getRootArgumentClass());
		for (Invocation invocation : invocationSequence) object = invocation.invokeOn(object);
		return object;
	}
	
	public Integer getRootArgumentId() {
		return rootArgumentId;
	}

	public Class<?> getRootArgumentClass() {
		return invocationSequence.getRootInvokedClass();
	}
	
	/**
	 * @return A unique String representation of the root object of this argument
	 */
	public String getRootReference() {
		return getRootArgumentClass().getSimpleName().toLowerCase() + "_" + getRootArgumentId();
	}

	@Override
	public String toString() {
		return invocationSequence.toString();
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null || !(object instanceof Argument)) return false;
		return invocationSequence.equals(((Argument)object).getInvocationSequence());
	}
	
	@Override
	public int hashCode() {
		return invocationSequence.hashCode();
	}
}
