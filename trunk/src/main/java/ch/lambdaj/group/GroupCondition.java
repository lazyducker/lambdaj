// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.group;

import java.util.*;

/**
 * @author Mario Fusco
 */
public class GroupCondition {

	private String groupBy;

	private String alias;

	private Map<String, String> additionalProperties = new HashMap<String, String>();

	public GroupCondition(String groupBy) {
		this(groupBy, null);
	}

	public GroupCondition(String groupBy, String alias) {
		this.groupBy = groupBy;
		this.alias = alias;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public GroupCondition addProperty(String name) {
		additionalProperties.put(name, name);
		return this;
	}

	public GroupCondition addProperty(String name, String alias) {
		additionalProperties.put(name, alias);
		return this;
	}

	public Map<String, String> getAdditionalProperties() {
		return additionalProperties;
	}
}
