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
	
	private static AtomicInteger argumentCounter = new AtomicInteger(Integer.MIN_VALUE);
	
	@SuppressWarnings("unchecked")
	public static <T> T createArgument(Class<T> clazz) {
		Integer rootArgumentId = argumentCounter.addAndGet(1);
		return (T)createArgument(rootArgumentId, clazz, new InvocationSequence(clazz));
	}
	
	private static Map<InvocationSequence, Object> placeholderByInvocation = new HashMap<InvocationSequence, Object>();
    
	static Object createArgument(Integer rootArgumentId, Class<?> clazz, InvocationSequence invocationSequence) {
		Object placeholder = placeholderByInvocation.get(invocationSequence);
		boolean isNewPlaceholder = placeholder == null;
		
		if (isNewPlaceholder) {
			placeholder = createPlaceholder(rootArgumentId, clazz, invocationSequence);
	    	placeholderByInvocation.put(invocationSequence, placeholder);
		}
		
		if (isNewPlaceholder || isBoolean(placeholder))
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
	
	private static Map<Object, Argument> argumentsByPlaceholder = new HashMap<Object, Argument>();
	
    private static void bindArgument(Object placeholder, Argument argument) {
    	if (isBoolean(placeholder))	booleanArguments.get().setArgument(placeholder, argument);
    	else argumentsByPlaceholder.put(placeholder, argument);
    }
    
    public static Argument actualArgument(Object placeholder) {
    	Argument actualArgument = isBoolean(placeholder) ? booleanArguments.get().getArgument(placeholder) : argumentsByPlaceholder.get(placeholder);
    	if (actualArgument == null) throw new RuntimeException("Unable to convert the placeholder " + placeholder + " in a valid argument");
    	return actualArgument;
    }
    
	private static ThreadLocal<BooleanArgumentHolder> booleanArguments = new ThreadLocal<BooleanArgumentHolder>() {
        protected BooleanArgumentHolder initialValue() {
            return new BooleanArgumentHolder();
        }
    };
    
    private static boolean isBoolean(Object placeholder) {
    	return placeholder != null && (placeholder instanceof Boolean || placeholder.getClass() == Boolean.TYPE);
    }
    
    private static final class BooleanArgumentHolder {
    	
    	private boolean placeholder = true;
    	
    	private Argument[] arguments = new Argument[2];

    	private int booleanToInt(Object placeholder) {
        	return ((Boolean)placeholder).booleanValue() ? 1 : 0;
        }
    	
    	public void setArgument(Object placeholder, Argument argument) {
    		arguments[booleanToInt(placeholder)] = argument;
    	}

    	public Argument getArgument(Object placeholder) {
    		return arguments[booleanToInt(placeholder)];
    	}
    	
    	public boolean getNextPlaceholder() {
    		placeholder = !placeholder;
    		return placeholder;
    	}
    }
    
	// ////////////////////////////////////////////////////////////////////////
	// /// Placeholders
	// ////////////////////////////////////////////////////////////////////////
	
    private static AtomicInteger placeholderCounter = new AtomicInteger(Integer.MIN_VALUE);
    
    static int getNextPlaceholderId() {
    	return placeholderCounter.addAndGet(1);
    }
    
    public static Object createArgumentPlaceholder(Class<?> clazz) {
    	if (Boolean.class.isAssignableFrom(clazz) || clazz == Boolean.TYPE) 
    		return booleanArguments.get().getNextPlaceholder();
	
    	Integer placeholderId = placeholderCounter.addAndGet(1);
		if (clazz.isPrimitive() || Number.class.isAssignableFrom(clazz)) 
			return getPrimitivePlaceHolder(clazz, placeholderId);
		
		if (clazz == String.class) return String.valueOf(placeholderId);
		if (Date.class.isAssignableFrom(clazz)) return new Date(placeholderId);
		if (clazz.isArray()) return new Object[0];

		try {
			return clazz.newInstance();
		} catch (Exception e) {
			return null;
		}
	}
	
    private static Object getPrimitivePlaceHolder(Class<?> clazz, Integer placeholderId) {
		try {
			return ArgumentsFactory.class.getMethod(clazz.getSimpleName().substring(0, 3).toLowerCase() + "Placeholder", Integer.class).invoke(null, placeholderId);
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

	public static Void voiPlaceholder(Integer i) {
		return null;
	}
}
