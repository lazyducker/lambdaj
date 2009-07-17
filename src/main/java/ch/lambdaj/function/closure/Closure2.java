package ch.lambdaj.function.closure;

import java.util.*;

public class Closure2<A, B> extends AbstractClosure {

	public Object apply(A param1, B param2) {
		return closeOne(param1, param2);
	}
	
	public List<Object> each(Iterable<? extends A> params1, Iterable<? extends B> params2) {
		return closeAll(params1, params2);
	}

	public Closure1<B> curry1(A curry) {
		return curry(new Closure1<B>(), curry, 1);
	}
	
	public Closure1<A> curry2(B curry) {
		return curry(new Closure1<A>(), curry, 2);
	}
}
