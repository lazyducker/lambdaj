package ch.lambdaj.proxy;

import static ch.lambdaj.Lambda.aggregate;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;
import ch.lambdaj.function.aggregate.*;

public class ProxyAggregator<T, A> extends ProxyIterator<T> {

	private Aggregator<A> aggregator;
	
	protected ProxyAggregator(Iterable<T> proxiedCollection, Aggregator<A> aggregator) {
		super(proxiedCollection);
		this.aggregator = aggregator;
	}
	
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		return aggregate(collectValues(method, args), aggregator);
	}
	
	public static <T,A> T createProxyAggregator(Iterable<T> proxiedCollection, Aggregator<A> aggregator, Class<?> clazz) {
		return (T)createProxy(new ProxyAggregator<T,A>(proxiedCollection, aggregator), clazz);
	}
}
