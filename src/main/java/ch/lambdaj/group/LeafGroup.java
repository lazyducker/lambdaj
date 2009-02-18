package ch.lambdaj.group;

import java.util.*;

public class LeafGroup<T> implements Group<T> {

	private Collection<T> collection;
	private Map<String, Object> headMap = new TreeMap<String, Object>();
	
	@SuppressWarnings("unchecked")
	LeafGroup(Map<String, Object> map, String childrenNodeName) {
		headMap = new TreeMap<String, Object>(map);
		collection = (Collection<T>)headMap.remove(childrenNodeName);
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

	public String getHeadValue(String key) {
		Object value = headMap.get(key);
		return value == null ? "" : value.toString();
	}

	public Set<String> getHeads() {
		return headMap.keySet();
	}
}
