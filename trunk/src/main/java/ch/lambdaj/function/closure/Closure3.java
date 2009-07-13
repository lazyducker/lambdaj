package ch.lambdaj.function.closure;

import java.util.*;

public class Closure3<A, B, C> extends AbstractClosure {

	public Object apply(A param1, B param2, C param3) {
		return closeOne(param1, param2, param3);
	}
	
	public List<Object> each(Iterable<? extends A> params1, Iterable<? extends B> params2, Iterable<? extends C> params3) {
		return closeAll(params1, params2, params3);
	}
}
