// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.group;

import java.util.*;

/**
 *
 * @author Mario Fusco
 */
public abstract class AbstractGroupCondition<T> implements GroupCondition {

	protected final Map<String, T> additionalProperties = new TreeMap<String, T>();
	
	private String alias;

	public String getAlias() {
		return alias;
	}

	public GroupCondition as(String alias) {
		this.alias = alias;
		return this;
	}

	public Collection<String> getAdditionalPropertyNames() {
		return additionalProperties.keySet();
	}

    public String getGroupValueAsString(Object item) {
        return asNotNullString(getGroupValue(item));
    }

	protected String asNotNullString(Object value) {
		return value == null ? "" : value.toString();
	}

    public <I> GroupItem<I> create(I item, String key) {
        GroupItem<I> groupItem = new GroupItem<I>(getAlias());
        groupItem.put(getGroupName(), key);
        for (String propertyName : getAdditionalPropertyNames())
            groupItem.put(propertyName, getAdditionalPropertyValue(propertyName, item));
        return groupItem;
    }
}
