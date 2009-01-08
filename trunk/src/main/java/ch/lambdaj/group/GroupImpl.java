package ch.lambdaj.group;

import java.util.*;

import ch.lambdaj.util.*;

@SuppressWarnings("serial")
public class GroupImpl<T> extends LinkedList<GroupItem<T>> implements Group<T> {

	private Map<String, GroupItem<T>> groupsMap = new HashMap<String, GroupItem<T>>();
	
	private GroupCondition groupCondition;
	
	public GroupImpl(GroupCondition groupCondition) {
		this.groupCondition = groupCondition;
	}
	
	void add(T item) {
		String key = asStringValue(item, groupCondition.getGroupBy());
		GroupItem<T> groupItem = findOrCreate(item, key);
		groupItem.addChild(item);
	}
	
	private String asStringValue(T item, String propertyName) {
		Object value = IntrospectionUtil.getPropertyValue(item, propertyName);
		return value == null ? "" : value.toString();
	}
	
	private GroupItem<T> findOrCreate(T item, String key) {
		GroupItem<T> groupItem = groupsMap.get(key);
		if (groupItem == null) {
			groupItem = new GroupItem<T>(groupCondition.getAlias());
			groupItem.setProperty(groupCondition.getGroupBy(), key);
			for (Map.Entry<String, String> entry : groupCondition.getAdditionalProperties().entrySet())
				groupItem.setProperty(entry.getValue(), asStringValue(item, entry.getKey()));
			groupsMap.put(key, groupItem);
			add(groupItem);
		}
		return groupItem;
	}
	
	public Set<String> keySet() {
		return groupsMap.keySet();
	}
	
	public Group<T> findGroup(String key) {
		GroupItem<T> groupItem = groupsMap.get(key);
		return groupItem == null ? null : groupItem.asGroup();
	}
	
	public Iterable<T> find(String key) {
		GroupItem<T> groupItem = groupsMap.get(key);
		return groupItem == null ? new LinkedList<T>() : groupItem;
	}
	
	public Iterable<T> findAll() {
		Collection<T> allItems = new LinkedList<T>();
		for (GroupItem<T> groupItem : this) for (T item : groupItem) allItems.add(item);
		return allItems;
	}
}
