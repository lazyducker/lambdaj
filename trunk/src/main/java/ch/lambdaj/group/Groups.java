// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.group;

import java.util.*;

/**
 * @author Mario Fusco
 */
public final class Groups {
	
	private Groups() { }

	public static <T> Group<T> group(Iterable<T> iterable, Collection<GroupCondition> conditions) {
		return group(iterable, conditions.toArray(new GroupCondition[conditions.size()]));
	}

	public static <T> Group<T> group(Iterable<T> iterable, String... groupers) {
		GroupCondition[] conditions = new GroupCondition[groupers.length];
		int i = 0;
		for (String grouper : groupers) conditions[i++] = new GroupCondition(grouper);
		return group(iterable, conditions);
	}

	public static <T> Group<T> group(Iterable<T> iterable, GroupCondition... conditions) {
		GroupCondition condition = conditions[0];
		GroupImpl<T> group = new GroupImpl<T>(condition);
		for (T item : iterable)	group.add(item);

		if (conditions.length > 1) {
			GroupCondition[] newConditions = new GroupCondition[conditions.length - 1];
			System.arraycopy(conditions, 1, newConditions, 0, newConditions.length);
			for (GroupItem<T> groupItem : group) groupItem.setChildren(group(groupItem, newConditions));
		}

		return group;
	}
}
