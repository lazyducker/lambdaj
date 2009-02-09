// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.group;

import java.util.*;

/**
 * @author Mario Fusco
 */
public class GroupItem<T> extends TreeMap<String, Object> implements Iterable<T> {

	private static final long serialVersionUID = 1L;
	
	private static final String CHILDREN_NODE = "children";
	private String childrenNodeName = CHILDREN_NODE;

	private boolean leaf = true;

	GroupItem() {}

	GroupItem(String childrenNodeName) {
		if (childrenNodeName != null) this.childrenNodeName = childrenNodeName;
	}

	private String getChildrenNodeName() {
		return childrenNodeName;
	}

	@SuppressWarnings("unchecked")
	private Collection<T> getChildren() {
		Collection<T> children = (Collection<T>) get(getChildrenNodeName());
		if (children == null) {
			children = new LinkedList<T>();
			put(getChildrenNodeName(), children);
		}
		return children;
	}

	@SuppressWarnings("unchecked")
	Group<T> asGroup() {
		return leaf ? new LeafGroup<T>((Collection<T>)get(getChildrenNodeName())) : (Group<T>)get(getChildrenNodeName());
	}

	public Iterator<T> iterator() {
		return asCollection().iterator();
	}
	
	boolean isLeaf() {
		return leaf;
	}
	
	Collection<T> asCollection() {
		if (leaf) return getChildren();
		Collection<T> leafs = new LinkedList<T>();
		for (T item : asGroup().findAll()) leafs.add(item);
		return leafs;
	}

	void addChild(T child) {
		if (!leaf) throw new IllegalStateException("cannot add a child to a non-leaf group");
		getChildren().add(child);
	}

	public void setChildren(Group<T> children) {
		leaf = false;
		put(getChildrenNodeName(), children);
	}
}
