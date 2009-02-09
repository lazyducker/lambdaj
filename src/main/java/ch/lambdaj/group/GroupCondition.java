package ch.lambdaj.group;

import java.util.*;

public interface GroupCondition {

	String getGroupName();
	String getGroupValue(Object item);

	String getAlias();
	GroupCondition as(String alias);

	Collection<String> getAdditionalPropertyNames();
	String getAdditionalPropertyValue(String name, Object item);
}
