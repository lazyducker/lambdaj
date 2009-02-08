package ch.lambdaj.group;

import java.io.*;
import java.util.*;

public interface GroupCondition extends Serializable {

	String getGroupName();
	String getGroupValue(Object item);

	String getAlias();
	GroupCondition as(String alias);

	Collection<String> getAdditionalPropertyNames();
	String getAdditionalPropertyValue(String name, Object item);
}
