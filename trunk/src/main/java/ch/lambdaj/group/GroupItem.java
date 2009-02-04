// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.group;

import java.util.*;

/**
 * @author Mario Fusco
 */
@SuppressWarnings("serial")
public class GroupItem<T> extends TreeMap<String, Object> implements Iterable<T> {

	private static final String CHILDREN_NODE = "children";
	private String childrenNodeName = CHILDREN_NODE;

	private boolean isLeaf = true;

	GroupItem() {}

	GroupItem(String childrenNodeName) {
		if (childrenNodeName != null) this.childrenNodeName = childrenNodeName;
	}

	private String getChildrenNodeName() {
		return childrenNodeName;
	}

	private Collection<T> getChildren() {
		Collection<T> children = (Collection<T>) get(getChildrenNodeName());
		if (children == null) {
			children = new LinkedList<T>();
			put(getChildrenNodeName(), children);
		}
		return children;
	}

	Group<T> asGroup() {
		return isLeaf ? null : (Group<T>) get(getChildrenNodeName());
	}

	public Iterator<T> iterator() {
		if (isLeaf) return getChildren().iterator();
		Collection<T> leafs = new LinkedList<T>();
		for (T item : asGroup().findAll()) leafs.add(item);
		return leafs.iterator();
	}

	void addChild(T child) {
		if (!isLeaf) throw new IllegalStateException("cannot add a child to a non-leaf group");
		getChildren().add(child);
	}

	public void setChildren(Group<T> children) {
		isLeaf = false;
		put(getChildrenNodeName(), children);
	}

	void setProperty(String key, String value) {
		put(key, value);
	}
}
