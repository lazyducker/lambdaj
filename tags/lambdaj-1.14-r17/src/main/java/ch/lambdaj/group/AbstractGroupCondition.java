package ch.lambdaj.group;

import static ch.lambdaj.util.IntrospectionUtil.*;

import java.util.*;

public abstract class AbstractGroupCondition<T> implements GroupCondition {

	protected Map<String, T> additionalProperties = new TreeMap<String, T>();
	
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

	protected String asStringValue(Object item, String propertyName) {
		return asNotNullString(getPropertyValue(item, propertyName));
	}
	
	protected String asNotNullString(Object value) {
		return value == null ? "" : value.toString();
	}
}
