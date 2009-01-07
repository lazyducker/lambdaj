package ch.lambdaj;

import java.util.*;

import ch.lambdaj.group.*;
import ch.lambdaj.util.*;

public class Mapper {

	public static <T> Group<T> group(Iterable<T> iterable, Collection<String> groupers) {
		return group(iterable, groupers.toArray(new String[groupers.size()]));
	}
	
	public static <T> Group<T> group(Iterable<T> iterable, String... groupers) {
		BranchGroup<T> group = new HashBranchGroup<T>(iterable); 
		String grouper = groupers[0];
		for (T item : group) {
			Object value = IntrospectionUtil.getPropertyValue(item, grouper);
			String valueAsString = value == null ? "" : value.toString();
			group.add(valueAsString, item);
		}
		if (groupers.length > 1) {
			String[] newGroupers = new String[groupers.length-1];
			for (int i = 0; i < newGroupers.length; i++) newGroupers[i] = groupers[i+1];
			for (Map.Entry<String, Group<T>> groupEntry : group.entrySet()) 
				groupEntry.setValue(group(groupEntry.getValue(), newGroupers));
		}
		return group;
	}
}
