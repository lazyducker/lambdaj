package ch.lambdaj.group;

import java.util.*;

public class LeafGroup<T> implements Group<T> {

	private List<T> list;
	private Map<String, Object> headMap = new TreeMap<String, Object>();
	
	@SuppressWarnings("unchecked")
	LeafGroup(Map<String, Object> map, String childrenNodeName) {
		headMap = new TreeMap<String, Object>(map);
		list = (List<T>)headMap.remove(childrenNodeName);
	}
	
	public List<T> find(String key) {
		return list;
	}

	public List<T> find(Object key) {
		return find(key.toString());
	}
	
	public List<T> findAll() {
		return list;
	}

	public Group<T> findGroup(String key) {
		return this;
	}
	
	public Group<T> findGroup(Object key) {
		return findGroup(key.toString());
	}
	
	@Override
	public List<Group<T>> subgroups() {
		return new ArrayList<Group<T>>();
	}

	public int getSize() {
		return list.size();
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
