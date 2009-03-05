package ch.lambdaj.function.argument;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import ch.lambdaj.proxy.*;

public final class ArgumentsFactory {

	private ArgumentsFactory() { }
	
	// ////////////////////////////////////////////////////////////////////////
	// /// Factory
	// ////////////////////////////////////////////////////////////////////////
	
	private static AtomicInteger argumentCounter = new AtomicInteger();
	
	@SuppressWarnings("unchecked")
	public static <T> T createArgument(Class<T> clazz) {
		Integer rootArgumentId = argumentCounter.addAndGet(1);
		return (T)createArgument(rootArgumentId, clazz, new InvocationSequence(clazz));
	}
	
	private static Map<InvocationSequence, Object> placeholderByInvocation = new HashMap<InvocationSequence, Object>();
    
	static Object createArgument(Integer rootArgumentId, Class<?> clazz, InvocationSequence invocationSequence) {
		Object placeholder = placeholderByInvocation.get(invocationSequence);
		
		if (placeholder == null) {
			placeholder = createPlaceholder(rootArgumentId, clazz, invocationSequence);
	    	placeholderByInvocation.put(invocationSequence, placeholder);
		}
		
    	bindArgument(placeholder, new Argument(rootArgumentId, invocationSequence));
		return placeholder;
	}
	
	private static Object createPlaceholder(Integer rootArgumentId, Class<?> clazz, InvocationSequence invocationSequence) {
		return !Modifier.isFinal(clazz.getModifiers()) ? 
				ProxyUtil.createProxy(new ProxyArgument(rootArgumentId, clazz, invocationSequence), clazz) : 
				createArgumentPlaceholder(clazz);
	}

	// ////////////////////////////////////////////////////////////////////////
	// /// Arguments
	// ////////////////////////////////////////////////////////////////////////
	
	private static ThreadLocal<Map<Object, Argument>> argumentsStore = new ThreadLocal<Map<Object, Argument>>() {
        protected Map<Object, Argument> initialValue() {
            return new HashMap<Object, Argument>();
        }
    };
    
    private static void bindArgument(Object placeholder, Argument argument) {
    	argumentsStore.get().put(placeholder, argument);
    }
    
    public static Argument actualArgument(Object placeholder) {
    	Argument actualArgument = argumentsStore.get().get(placeholder);
    	if (actualArgument == null) throw new RuntimeException("Unable to convert the placeholder " + placeholder + " in a valid argument");
    	return actualArgument;
    }
    
	// ////////////////////////////////////////////////////////////////////////
	// /// Placeholders
	// ////////////////////////////////////////////////////////////////////////
	
    private static AtomicInteger placeholderCounter = new AtomicInteger(Integer.MIN_VALUE);
    
    static int getNextPlaceholderId() {
    	return placeholderCounter.addAndGet(1);
    }
    
    public static Object createArgumentPlaceholder(Class<?> clazz) {
		Integer i = placeholderCounter.addAndGet(1);
		if (clazz.isPrimitive() || Number.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz)) return getPrimitivePlaceHolder(clazz, i);
		if (clazz == String.class) return String.valueOf(i);
		if (clazz.isArray()) return new Object[0];

		try {
			return clazz.newInstance();
		} catch (Exception e) {
			return null;
		}
	}
	
    private static Object getPrimitivePlaceHolder(Class<?> clazz, Integer i) {
		try {
			return ArgumentsFactory.class.getMethod(clazz.getSimpleName().substring(0, 3).toLowerCase() + "Placeholder", Integer.class).invoke(null, i);
		} catch (Exception e) {
			throw new RuntimeException("Unable to create placeholder", e);
		}
	}
	
	public static Integer intPlaceholder(Integer i) {
		return i;
	}

	public static Character chaPlaceholder(Integer i) {
		return Character.forDigit(i % Character.MAX_RADIX, Character.MAX_RADIX);
	}
	
	public static Byte bytPlaceholder(Integer i) {
		return Byte.valueOf(i.byteValue());
	}
	
	public static Short shoPlaceholder(Integer i) {
		return Short.valueOf(i.shortValue());
	}
	
	public static Long lonPlaceholder(Integer i) {
		return Long.valueOf(i.longValue());
	}

	public static Float floPlaceholder(Integer i) {
		return Float.valueOf(i.floatValue());
	}
	
	public static Double douPlaceholder(Integer i) {
		return Double.valueOf(i.doubleValue());
	}

	public static Boolean booPlaceholder(Integer i) {
		return i % 2 == 0 ? Boolean.TRUE : Boolean.FALSE;
	}

	public static Void voiPlaceholder(Integer i) {
		return null;
	}
}
