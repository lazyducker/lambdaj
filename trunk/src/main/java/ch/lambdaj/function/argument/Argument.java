package ch.lambdaj.function.argument;

import java.util.*;

public class Argument implements Iterable<Invocation> {

	private List<Invocation> invocationSequence = new ArrayList<Invocation>();
	
	private Integer rootArgumentId;
	
	private Argument(Integer rootArgumentId) {
		this.rootArgumentId = rootArgumentId;
	}
	
	Argument(Integer rootArgumentId, Class<?> invokedClass) {
		this(rootArgumentId);
		invocationSequence.add(new Invocation(invokedClass));
	}
	
	Argument(Integer rootArgumentId, List<Invocation> invocationSequence) {
		this(rootArgumentId);
		this.invocationSequence = invocationSequence;
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
		return invocationSequence.get(0).getInvokedClass();
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
}
