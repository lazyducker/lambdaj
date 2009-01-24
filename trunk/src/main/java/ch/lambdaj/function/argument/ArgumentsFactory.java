package ch.lambdaj.function.argument;

import java.lang.reflect.*;
import java.util.*;

import ch.lambdaj.proxy.*;

public class ArgumentsFactory {

	private ArgumentsFactory() { }
	
	// ////////////////////////////////////////////////////////////////////////
	// /// Factory
	// ////////////////////////////////////////////////////////////////////////
	
	private static Integer argumentCounter = 0;
	
	public static <T> T on(Class<T> clazz) {
		Integer rootArgumentId = argumentCounter++;
		T argument = createArgument(rootArgumentId, clazz, new ArrayList<Invocation>());
		bindArgument(argument, new Argument(rootArgumentId, clazz));
		return argument;
	}
	
	@SuppressWarnings("unchecked")
	static <T> T createArgument(Integer rootArgumentId, Class<T> clazz, List<Invocation> invocationSequence) {
		return (T)ProxyUtil.createProxy(new ProxyArgument(rootArgumentId, clazz, invocationSequence), clazz);
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// /// Placeholders
	// ////////////////////////////////////////////////////////////////////////
	
	private static ThreadLocal<Map<Object, Argument>> argumentsStore = new ThreadLocal<Map<Object, Argument>>() {
        protected Map<Object, Argument> initialValue() {
            return new HashMap<Object, Argument>();
        }
    };
    
    static void bindArgument(Object placeholder, Argument argument) {
    	argumentsStore.get().put(placeholder, argument);
    }
    
    public static Argument actualArgument(Object placeholder) {
    	return argumentsStore.get().get(placeholder);
    }
}
