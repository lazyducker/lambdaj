package ch.lambdaj.group;

import java.util.LinkedList;
import java.util.Set;

@SuppressWarnings("serial")
public class LinkedLeafGroup<T> extends LinkedList<T> implements Group<T> {
	
	public LinkedLeafGroup() { }
	
	public LinkedLeafGroup(Iterable<T> iterable) { 
		for (T item : iterable) add(item);
	}

	public Group<T> get(Object key) {
		return this;
	}

	public Set<String> keySet() {
		return null;
	}
}
