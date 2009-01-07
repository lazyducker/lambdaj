package ch.lambdaj.group;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class HashBranchGroup<T> extends HashMap<String, Group<T>> implements BranchGroup<T> {
	
	private Collection<T> leafs = new LinkedList<T>();
	
	public HashBranchGroup() { }
	
	public HashBranchGroup(Iterable<T> iterable) { 
		for (T item : iterable) leafs.add(item);
	}
	
	public void add(String key, T value) {
		Group<T> group = get(key);
		if (group == null) {
			group = new LinkedLeafGroup<T>();
			put(key, group);
		}
		group.add(value);
	}

	public Iterator<T> iterator() {
		return leafs.iterator();
	}

	public boolean add(T item) {
		return leafs.add(item);
	}
}
