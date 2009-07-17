package ch.lambdaj.function.closure;

import java.util.*;

public class Closure3<A, B, C> extends AbstractClosure {

	public Object apply(A param1, B param2, C param3) {
		return closeOne(param1, param2, param3);
	}
	
	public List<Object> each(Iterable<? extends A> params1, Iterable<? extends B> params2, Iterable<? extends C> params3) {
		return closeAll(params1, params2, params3);
	}
	
	public Closure2<B, C> curry1(A curry) {
		return curry(new Closure2<B, C>(), curry, 1);
	}
	
	public Closure2<A, C> curry2(B curry) {
		return curry(new Closure2<A, C>(), curry, 2);
	}
	
	public Closure2<A, B> curry3(C curry) {
		return curry(new Closure2<A, B>(), curry, 3);
	}
}
