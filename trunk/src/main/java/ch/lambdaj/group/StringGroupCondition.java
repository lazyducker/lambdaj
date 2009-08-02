// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.group;

import static ch.lambdaj.util.IntrospectionUtil.getPropertyValue;


/**
 * @author Mario Fusco
 */
public class StringGroupCondition extends AbstractGroupCondition<String> {

	private final String groupBy;

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
	
	public Object getGroupValue(Object item) {
        return getPropertyValue(item, groupBy);
	}

	public StringGroupCondition head(String name) {
		return head(name, name);
	}

	public StringGroupCondition head(String name, String alias) {
		additionalProperties.put(name, alias);
		return this;
	}

	public String getAdditionalPropertyValue(String name, Object item) {
        return asNotNullString(getPropertyValue(item, additionalProperties.get(name)));
	}
}
