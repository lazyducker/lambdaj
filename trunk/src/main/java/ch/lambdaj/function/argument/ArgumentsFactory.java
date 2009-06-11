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
	
	@SuppressWarnings("unchecked")
	public static <T> T createArgument(Class<T> clazz) {
		return (T)createArgument(clazz, new InvocationSequence(clazz));
	}
	
	private static Map<InvocationSequence, Object> placeholderByInvocation = new WeakHashMap<InvocationSequence, Object>();
    
	static Object createArgument(Class<?> clazz, InvocationSequence invocationSequence) {
		Object placeholder = placeholderByInvocation.get(invocationSequence);
		boolean isNewPlaceholder = placeholder == null;
		
		if (isNewPlaceholder) {
			placeholder = createPlaceholder(clazz, invocationSequence);
	    	placeholderByInvocation.put(invocationSequence, placeholder);
		}
		
		if (isNewPlaceholder || isLimitedValues(placeholder))
			bindArgument(placeholder, new Argument(invocationSequence));
		
		return placeholder;
	}
	
	private static Object createPlaceholder(Class<?> clazz, InvocationSequence invocationSequence) {
		return !Modifier.isFinal(clazz.getModifiers()) ? 
				ProxyUtil.createProxy(new ProxyArgument(clazz, invocationSequence), clazz) : 
				createArgumentPlaceholder(clazz);
	}

	// ////////////////////////////////////////////////////////////////////////
	// /// Arguments
	// ////////////////////////////////////////////////////////////////////////
	
	private static Map<Object, Argument> argumentsByPlaceholder = new WeakHashMap<Object, Argument>();
	
    private static void bindArgument(Object placeholder, Argument argument) {
    	if (isLimitedValues(placeholder)) limitedValuesArguments.get().setArgument(placeholder, argument);
    	else argumentsByPlaceholder.put(placeholder, argument);
    }
    
    public static Argument actualArgument(Object placeholder) {
    	if (placeholder instanceof Argument) return (Argument)placeholder;
    	Argument actualArgument = isLimitedValues(placeholder) ? limitedValuesArguments.get().getArgument(placeholder) : argumentsByPlaceholder.get(placeholder);
    	if (actualArgument == null) throw new RuntimeException("Unable to convert the placeholder " + placeholder + " in a valid argument");
    	return actualArgument;
    }
    
	private static ThreadLocal<LimitedValuesArgumentHolder> limitedValuesArguments = new ThreadLocal<LimitedValuesArgumentHolder>() {
        protected LimitedValuesArgumentHolder initialValue() {
            return new LimitedValuesArgumentHolder();
        }
    };
    
    private static boolean isLimitedValues(Object placeholder) {
    	return placeholder != null && isLimitedValues(placeholder.getClass());
    }
    
    private static boolean isLimitedValues(Class<?> clazz) {
    	return clazz == Boolean.TYPE || clazz == Boolean.class || clazz.isEnum();
    }
    
    private static final class LimitedValuesArgumentHolder {
    	
    	private boolean booleanPlaceholder = true;
    	private Argument[] booleanArguments = new Argument[2];

    	private int enumPlaceholder = 0;
    	private Map<Object, Argument> enumArguments = new HashMap<Object, Argument>();
    	
    	private int booleanToInt(Object placeholder) {
        	return ((Boolean)placeholder).booleanValue() ? 1 : 0;
        }
    	
    	public void setArgument(Object placeholder, Argument argument) {
    		if (placeholder.getClass().isEnum()) enumArguments.put(placeholder, argument);
    		else booleanArguments[booleanToInt(placeholder)] = argument;
    	}

    	public Argument getArgument(Object placeholder) {
    		return placeholder.getClass().isEnum() ? enumArguments.get(placeholder) : booleanArguments[booleanToInt(placeholder)];
    	}
    	
    	@SuppressWarnings("unchecked")
    	public Object getNextPlaceholder(Class<?> clazz) {
    		return clazz.isEnum() ? getNextEnumPlaceholder((Class<? extends Enum>)clazz) : getNextBooleanPlaceholder(); 
    	}
    	
    	private boolean getNextBooleanPlaceholder() {
    		booleanPlaceholder = !booleanPlaceholder;
    		return booleanPlaceholder;
    	}
    	
    	private <E extends Enum<E>> Enum<E> getNextEnumPlaceholder(Class<E> clazz) {
    		List<E> enums = new ArrayList<E>(EnumSet.allOf(clazz));
    		return enums.get(enumPlaceholder++ % enums.size());
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
    	if (isLimitedValues(clazz)) 
    		return limitedValuesArguments.get().getNextPlaceholder(clazz);
	
    	Integer placeholderId = placeholderCounter.addAndGet(1);
		if (clazz.isPrimitive() || Number.class.isAssignableFrom(clazz)) 
			return getPrimitivePlaceHolder(clazz, placeholderId);
		
		if (clazz == String.class) return String.valueOf(placeholderId);
		if (Date.class.isAssignableFrom(clazz)) return new Date(placeholderId);

		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("It is not possible to create a placeholder for class: " + clazz.getName());
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
