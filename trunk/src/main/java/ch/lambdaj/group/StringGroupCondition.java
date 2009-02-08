// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.group;


/**
 * @author Mario Fusco
 */
public class StringGroupCondition extends AbstractGroupCondition<String> {

	private static final long serialVersionUID = 1L;

	private String groupBy;

	public StringGroupCondition(String groupBy) {
		this(groupBy, null);
	}

	public StringGroupCondition(String groupBy, String alias) {
		this.groupBy = groupBy;
		as(alias);
	}

	public String getGroupName() {
		return groupBy;
	}
	
	public String getGroupValue(Object item) {
		return asStringValue(item, groupBy);
	}

	public StringGroupCondition head(String name) {
		return head(name, name);
	}

	public StringGroupCondition head(String name, String alias) {
		additionalProperties.put(name, alias);
		return this;
	}

	public String getAdditionalPropertyValue(String name, Object item) {
		return asStringValue(item, additionalProperties.get(name));
	}
}
