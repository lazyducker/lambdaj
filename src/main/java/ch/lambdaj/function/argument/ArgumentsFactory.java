package ch.lambdaj.function.argument;

import java.util.*;

import ch.lambdaj.proxy.*;

public class ArgumentsFactory {

	private ArgumentsFactory() { }
	
	// ////////////////////////////////////////////////////////////////////////
	// /// Factory
	// ////////////////////////////////////////////////////////////////////////
	
	private static Integer argumentCounter = 0;
	
	public static <T> T createArgument(Class<T> clazz) {
		Integer rootArgumentId = argumentCounter++;
		T argument = createArgument(rootArgumentId, clazz, new InvocationSequence(clazz));
		return argument;
	}
	
	@SuppressWarnings("unchecked")
	static <T> T createArgument(Integer rootArgumentId, Class<T> clazz, InvocationSequence invocationSequence) {
		T argument = (T)placeholderByInvocation.get(invocationSequence);
		if (argument == null) {
			argument = (T)ProxyUtil.createProxy(new ProxyArgument(rootArgumentId, clazz, invocationSequence), clazz);
			placeholderByInvocation.put(invocationSequence, argument);
			bindArgument(argument, new Argument(rootArgumentId, invocationSequence));
		}
		return argument;
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// /// Placeholders
	// ////////////////////////////////////////////////////////////////////////
	
	private static Map<InvocationSequence, Object> placeholderByInvocation = new HashMap<InvocationSequence, Object>();
    
	static void bindPlaceholder(InvocationSequence invocationSequence, Object placeholder, Argument argument) {
    	placeholderByInvocation.put(invocationSequence, placeholder);
    	bindArgument(placeholder, argument);
    }

    static Object getPlaceholder(InvocationSequence invocationSequence) {
    	return placeholderByInvocation.get(invocationSequence);
    }
    
	private static Map<Object, Argument> argumentByPlaceholder = new HashMap<Object, Argument>();
	
	private static void bindArgument(Object placeholder, Argument argument) {
    	argumentByPlaceholder.put(placeholder, argument);
    }
    
    public static Argument actualArgument(Object placeholder) {
    	return argumentByPlaceholder.get(placeholder);
    }
}
