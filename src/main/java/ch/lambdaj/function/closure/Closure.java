package ch.lambdaj.function.closure;

import java.util.*;

public class Closure extends AbstractClosure {

	public Object apply(Object... params) {
		return closeOne(params);
	}
	
	public List<Object> each(Object... params) {
		return closeAll(params);
	}
	
	public List<Object> each(Iterable<?>... params) {
		return closeAll(params);
	}
}
