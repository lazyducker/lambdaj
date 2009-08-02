// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.group;

import java.util.*;

/**
 * A condition that specifies how to group a list of objects.
 * @author Mario Fusco
 */
public interface GroupCondition {

	String getGroupName();
    Object getGroupValue(Object item);
	String getGroupValueAsString(Object item);

	String getAlias();
	GroupCondition as(String alias);

	Collection<String> getAdditionalPropertyNames();
	String getAdditionalPropertyValue(String name, Object item);

    <I> GroupItem<I> create(I item, String key);
}
