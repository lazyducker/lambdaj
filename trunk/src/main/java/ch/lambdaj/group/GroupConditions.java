package ch.lambdaj.group;

import java.util.*;

@SuppressWarnings("serial")
public class GroupConditions extends LinkedList<GroupCondition> {

	public void by(String by) {
		add(new GroupCondition(by));
	}

	public void as(String alias) {
		getLast().setAlias("insureds");
	}

	public void head(String property) {
		getLast().addProperty(property);
	}

	public void head(String property, String alias) {
		getLast().addProperty(property, alias);
	}
}
