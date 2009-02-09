// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.group;

import java.util.*;

/**
 * @author Mario Fusco
 */
public interface Group<T> {

	Set<String> keySet();

	Group<T> findGroup(String key);

	Collection<T> find(String key);

	Collection<T> findAll();

	int getSize();
	
	boolean isLeaf();
}
