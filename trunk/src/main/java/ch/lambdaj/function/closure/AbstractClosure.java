package ch.lambdaj.function.closure;

import static ch.lambdaj.function.closure.ClosuresFactory.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author Mario Fusco
 */
abstract class AbstractClosure {

	private Object closed;
	
	private List<Method> methodList = new ArrayList<Method>();
	private List<Object[]> argsList = new ArrayList<Object[]>();
	
	private Object[] curriedParams;
	private boolean[] curriedParamsFlags;

	private int unboundParamsCount = 0;
	
	AbstractClosure() { }

	void setClosed(Object closed) {
		this.closed = closed;
	}
	
	void registerInvocation(Method method, Object[] args) {
		methodList.add(method);
		if (args != null) for (Object arg : args) if (isClosureArgPlaceholder(arg)) unboundParamsCount++;
		argsList.add(args);
	}
	
	protected Object closeOne(Object... params) {
		List<Object[]> boundParams = bindParams(params);
		Object result = closed;
		
		Iterator<Object[]> argsIterator = boundParams != null ? boundParams.iterator() : null;
		for (Method method : methodList) {
			if (result == null) return null;
			try {
				result = method.invoke(result, argsIterator != null ? argsIterator.next() : null);
			} catch (Exception e) {
				throw new WrongClosureInvocationException("Error invoking " + method + " on " + result, e);
			}
		}
		
		return result;
	}

	protected List<Object> closeAll(Object... params) {
		List<Object> results = new ArrayList<Object>();
		for (Object param : params) results.add(closeOne(param));
		return results;
	}
	
	protected List<Object> closeAll(Iterable<?>... params) {
		List<Object> results = new ArrayList<Object>();
		
		int length = params.length;
		Iterator<?>[] iterators = new Iterator<?>[length];
		for (int i = 0; i < length; i++) iterators[i] = params[i].iterator();

		boolean finished = false;
		while (true) {
			Object[] paramSet = new Object[length];
			for (int i = 0; i < length; i++) {
				if (!iterators[i].hasNext()) {
					finished = true;
					break;
				}
				paramSet[i] = iterators[i].next();
			}
			if (finished) break;
			results.add(closeOne(paramSet));
		}
		
		return results;
	}
	
	private List<Object[]> bindParams(Object... params) {
		if (params == null || params.length == 0) {
			if (unboundParamsCount != 0) 
				throw new WrongClosureInvocationException("Closure invoked without params instead of the expected " + unboundParamsCount);
			if (curriedParams == null) return null;
		}
		if (unboundParamsCount != params.length)
			throw new WrongClosureInvocationException("Closure invoked with " + params.length + " params instead of the expected " + unboundParamsCount);
		
		int paramCounter = 0; 
		int curriedParamCounter = 0;
		List<Object[]> boundParams = new ArrayList<Object[]>();
		for (Object[] args : argsList) {
			if (args == null) boundParams.add(null);
			else {
				Object[] objs = new Object[args.length];
				for (int i = 0; i < args.length; i++) {
					if (!isClosureArgPlaceholder(args[i])) objs[i] = args[i];
					else if (curriedParams != null && curriedParamsFlags[curriedParamCounter]) objs[i] = curriedParams[curriedParamCounter++];
					else {
						objs[i] = params[paramCounter++];
						curriedParamCounter++;
					}
				} 
				boundParams.add(objs);
			}
		}
		return boundParams;
	}
	
	protected <T extends AbstractClosure> T curry(T curriedClosure, Object curried, int position) {
		curriedClosure.closed = closed;
		curriedClosure.methodList = methodList;
		curriedClosure.argsList = argsList;
		curriedClosure.curriedParams = curriedParams;
		curriedClosure.curriedParamsFlags = curriedParamsFlags;
		curriedClosure.unboundParamsCount = unboundParamsCount;

		curriedClosure.curryParam(curried, position);
		return curriedClosure;
	}
	
	private void curryParam(Object curried, int position) {
		if (curriedParams == null) {
			curriedParams = new Object[unboundParamsCount];
			curriedParamsFlags = new boolean[unboundParamsCount];
		}
		
		for (int i = 0; i < curriedParams.length; i++) {
			if (curriedParamsFlags[i]) continue;
			if (--position == 0) {
				curriedParams[i] = curried;
				curriedParamsFlags[i] = true;
				unboundParamsCount--;
				return;
			}
		}
		
		throw new IllegalArgumentException("Trying to curry this closure on an already bound or unexisting paramater");
	}
	
	@SuppressWarnings("unchecked")
	public <T> T as(Class<T> asInterface) {
		if (!asInterface.isInterface()) throw new IllegalArgumentException("Cannot cast a closure to the concrete class " + asInterface.getName());
		Method[] methods = asInterface.getMethods();
		if (methods.length != 1) throw new IllegalArgumentException("Cannot cast a closure to an interface with more than one method");
		
		return (T)Proxy.newProxyInstance(asInterface.getClassLoader(), new Class<?>[] { asInterface }, 
			new InvocationHandler() { 
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
					return closeOne(args);
				}
			}
		);
	}
}
