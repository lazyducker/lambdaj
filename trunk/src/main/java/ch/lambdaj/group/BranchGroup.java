package ch.lambdaj.group;

import java.util.Map;

public interface BranchGroup<T> extends Group<T>, Map<String, Group<T>> {

	void add(String key, T value);
}
