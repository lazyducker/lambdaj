package ch.lambdaj.function.closure;

import java.util.*;

/**
 * @author Mario Fusco
 */
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

	public Closure curry(Object curry, int position) {
		return curry(new Closure(), curry, position);
	}
}
