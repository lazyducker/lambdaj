package ch.lambdaj.group;

import java.util.Set;


public interface Group<T> extends Iterable<T> {

	boolean add(T item);
	
	Group<T> get(Object key);

	Set<String> keySet();
}
