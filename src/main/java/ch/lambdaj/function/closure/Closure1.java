package ch.lambdaj.function.closure;

import java.util.*;

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
}
