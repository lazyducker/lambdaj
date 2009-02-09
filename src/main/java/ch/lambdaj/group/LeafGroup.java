package ch.lambdaj.group;

import java.util.*;

public class LeafGroup<T> implements Group<T> {

	private Collection<T> collection;
	
	LeafGroup(Collection<T> collection) {
		this.collection = collection;
	}
	
	public Collection<T> find(String key) {
		return collection;
	}

	public Collection<T> findAll() {
		return collection;
	}

	public Group<T> findGroup(String key) {
		return this;
	}

	public int getSize() {
		return collection.size();
	}

	public boolean isLeaf() {
		return true;
	}

	public Set<String> keySet() {
		return new HashSet<String>();
	}
}
