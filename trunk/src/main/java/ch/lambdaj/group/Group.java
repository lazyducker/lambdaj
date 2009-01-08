package ch.lambdaj.group;

import java.util.*;

public interface Group<T> {
	
	Set<String> keySet();
	
	Group<T> findGroup(String key);
	
	Iterable<T> find(String key);
	
	Iterable<T> findAll();

}
