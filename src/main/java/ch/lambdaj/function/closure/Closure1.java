package ch.lambdaj.function.closure;

import java.util.*;

/**
 * @author Mario Fusco
 */
public class Closure1<A> extends AbstractClosure {

	public Object apply(A param) {
		return closeOne(param);
	}
	
	public List<Object> each(A... params) {
		return closeAll(params);
	}
	
	public List<Object> each(Iterable<? extends A> params) {
		return closeAll(params);
	}
	
	public Closure0 curry(A curry) {
		return curry(new Closure0(), curry, 1);
	}
}
