// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.group;

import java.util.*;

/**
 * A set of object (optionally hierarchically) grouped on the values of one or more of their properties 
 * @author Mario Fusco
 */
public interface Group<T> {
	
	/**
	 * Returns the set of the keys of the subgroups of this group
	 */
	Set<String> keySet();

	/**
	 * Returns the subgroup indexed with the given key
	 * @param key The key that identifies the searched subgroup
	 * @return the subgroup with the given key or null if such a group doesn't exist
	 */
	Group<T> findGroup(String key);

	/**
	 * Returns all the items in the subgroup indexed with the given key
	 * @param key The key that identifies the searched subgroup
	 * @return all the object in the subgroup with the given key or an empty Collection if such group doesn't exist or is empty
	 */
	Collection<T> find(String key);

	/**
	 * Returns all the items in this group
	 */
	Collection<T> findAll();

	/**
	 * Returns how many items are present in this group.
	 */
	int getSize();
	
	/**
	 * Returns true if this group is a leaf or false if it has further subgroups
	 */
	boolean isLeaf();

	/**
	 * Returns the set of headers used to tag this group
	 */
	Set<String> getHeads();

	/**
	 * Returns the value of the tag with the given key
	 * @param key The key of the request tag value
	 */
	String getHeadValue(String key);
}
