package ch.lambdaj;

import java.lang.reflect.*;
import java.util.*;

import org.hamcrest.*;

import ch.lambdaj.function.aggregator.*;
import ch.lambdaj.function.comparator.*;
import ch.lambdaj.function.converter.*;
import ch.lambdaj.proxy.*;

@SuppressWarnings("unchecked")
public class Lambda {

	public static final <T> T by(T t) {
		T result = null;
		try {
			result = (T) t.getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public static <T> T from(Iterable<T> iterable) {
		if (iterable == null) return null;
		Iterator<T> iterator = iterable.iterator();
		return (iterator.hasNext()) ? from(iterable, iterator.next().getClass()) : null;
	}

	public static <T> T from(Iterable<T> iterable, Class<?> clazz) {
		return ProxyIterator.createProxyIterator(iterable, clazz);
	}

	public static <T> T[] toArray(Collection<T> c) {
		if (c == null || c.isEmpty()) return null;
		return toArray(c, c.iterator().next().getClass());
	}

	public static <T> T[] toArray(Collection<T> c, Class<?> t) {
		return c.toArray((T[]) Array.newInstance(t, c == null ? 0 : c.size()));
	}

	// ////////////////////////////////////////////////////////////////////////
	// /// Collection
	// ////////////////////////////////////////////////////////////////////////

	public static <T> List<? extends T> collect(Object iterable) {
		List<T> collected = new LinkedList<T>();
		for (Object item : (Iterable<?>) iterable) {
			if (item instanceof Iterable) collected.addAll((Collection<T>) collect(item));
			else
				collected.add((T) item);
		}
		return collected;
	}

	// ////////////////////////////////////////////////////////////////////////
	// /// Selection
	// ////////////////////////////////////////////////////////////////////////

	public static <T> Collection<T> select(Class<T> tClass, Object iterable, Matcher<?> matcher) {
		return select((Iterable<T>) iterable, matcher);
	}

	public static <T> Collection<T> select(Iterable<T> iterable, Matcher<?> matcher) {
		Collection<T> collected = new LinkedList<T>();
		if (iterable == null) return collected;
		for (T item : iterable)
			if (matcher.matches(item)) collected.add(item);
		return collected;
	}

	public static <T> Collection<T> select(Object iterable, Matcher<?> matcher) {
		return select((Iterable<T>) iterable, matcher);
	}

	public static <T> T selectUnique(Object iterable, Matcher<?> matcher) {
		return selectUnique((Iterable<T>) iterable, matcher);
	}

	public static <T> T selectUnique(Iterable<T> iterable, Matcher<?> matcher) {
		return (T) unique(select(iterable, matcher));
	}

	public static <T> T unique(Collection<T> collected) {
		if (collected.isEmpty()) return null;
		if (collected.size() > 1) throw new RuntimeException("Not unique item. Found: " + collected);
		return collected.iterator().next();
	}

	public static <T> T selectFirst(Object iterable, Matcher<?> matcher) {
		return selectFirst((Iterable<T>) iterable, matcher);
	}

	public static <T> T first(Collection<T> collected) {
		if (collected.isEmpty()) return null;
		return collected.iterator().next();
	}

	public static <T> T selectFirst(Iterable<T> iterable, Matcher<?> matcher) {
		return (T) first(select(iterable, matcher));
	}

	public static <T> Collection<T> selectDistinct(Iterable<T> iterable) {
		return selectDistinct(iterable, (Comparator<T>) null);
	}

	public static <T> Collection<T> selectDistinct(Object iterable) {
		return selectDistinct((Iterable<T>) iterable, (Comparator<T>) null);
	}

	public static <T> Collection<T> selectDistinct(Iterable<T> iterable, String propertyName) {
		return selectDistinct(iterable, new PropertyComparator<T>(propertyName));
	}

	public static <T> Collection<T> selectDistinct(Object iterable, String propertyName) {
		return selectDistinct((Iterable<T>) iterable, new PropertyComparator<T>(propertyName));
	}

	public static <T> Collection<T> selectDistinct(Object iterable, Comparator<T> comparator) {
		Set<T> collected = comparator == null ? new HashSet<T>() : new TreeSet<T>(comparator);
		if (iterable != null) for (T item : (Iterable<T>) iterable)
			collected.add(item);
		return collected;
	}

	// ////////////////////////////////////////////////////////////////////////
	// /// Aggregation
	// ////////////////////////////////////////////////////////////////////////

	private static final Summer SUMMER = new Summer();
	private static final MinFinder MIN_FINDER = new MinFinder();
	private static final MaxFinder MAX_FINDER = new MaxFinder();
	private static final StringConcatenator STRING_CONCATENATOR = new StringConcatenator();

	public static <T> T aggregate(Object iterable, Aggregator<T> aggregator) {
		T result = aggregator.emptyItem();
		if (iterable != null) for (T item : (Iterable<T>) iterable)
			result = aggregator.aggregate(result, item);
		return result;
	}

	public static <T, A> T aggregateFrom(Iterable<T> iterable, Aggregator<A> a) {
		if (iterable == null) return null;
		Iterator<T> i = iterable.iterator();
		return i.hasNext() ? aggregateFrom(iterable, i.next().getClass(), a) : null;
	}

	public static <T, A> T aggregateFrom(Iterable<T> i, Class<?> c, Aggregator<A> a) {
		return (T) ProxyAggregator.createProxyAggregator(i, a, c);
	}

	// -- (Sum) ---------------------------------------------------------------

	public static Number sum(Object iterable) {
		return aggregate((Iterable<Number>) iterable, SUMMER);
	}

	public static <T> T sumFrom(Iterable<T> c) {
		return aggregateFrom(c, SUMMER);
	}

	public static <T> T sumFrom(Iterable<T> c, Class<?> t) {
		return aggregateFrom(c, t, SUMMER);
	}

	// -- (Min) ---------------------------------------------------------------

	public static <T> T min(Object iterable) {
		return (T) aggregate((Iterable<T>) iterable, MIN_FINDER);
	}

	public static <T> T minFrom(Iterable<T> c) {
		return (T) aggregateFrom(c, MIN_FINDER);
	}

	public static <T> T minFrom(Iterable<T> c, Class<?> t) {
		return (T) aggregateFrom(c, t, MIN_FINDER);
	}

	// -- (Max) ---------------------------------------------------------------

	public static <T> T max(Object iterable) {
		return (T) aggregate((Iterable<T>) iterable, MAX_FINDER);
	}

	public static <T> T maxFrom(Iterable<T> c) {
		return (T) aggregateFrom(c, MAX_FINDER);
	}

	public static <T> T maxFrom(Iterable<T> c, Class<?> t) {
		return (T) aggregateFrom(c, t, MAX_FINDER);
	}

	// -- (Concat) ------------------------------------------------------------

	public static <T> T joinFrom(Iterable<T> c) {
		return aggregateFrom(c, STRING_CONCATENATOR);
	}

	public static <T> T joinFrom(Iterable<T> c, String separator) {
		return aggregateFrom(c, new StringConcatenator(separator));
	}

	public static <T> T joinFrom(Iterable<T> c, Class<?> t) {
		return aggregateFrom(c, t, STRING_CONCATENATOR);
	}

	public static <T> T joinFrom(Iterable<T> c, Class<?> t, String separator) {
		return aggregateFrom(c, t, new StringConcatenator(separator));
	}

	public static String join(Object iterable) {
		return (String) aggregate((Iterable<?>) iterable, STRING_CONCATENATOR);
	}

	private static void flatten(List collection, Iterable iterable) {
		for(Object object: iterable) {
			if(object instanceof Iterable) {
				flatten(collection, (Iterable) object);
			}
			else if(object instanceof Map) {
				flatten(collection, ((Map) object).values());
			}
			else {
				collection.add(object);
			}
		}
	}
	public static List flatten(Iterable iterable) {
		List collection = new LinkedList();
		flatten(collection, iterable);
		return collection;
	}

	public static String join(Object iterable, String separator) {
		return (String) aggregate((Iterable<?>) iterable, new StringConcatenator(separator));
	}

	// ////////////////////////////////////////////////////////////////////////
	// /// Conversion
	// ////////////////////////////////////////////////////////////////////////

	public static <F, T> Collection<T> convert(Object iterable, Converter<F, T> convertor) {
		Collection<T> collected = new ArrayList<T>();
		if (iterable != null) for (F item : (Iterable<F>) iterable)
			collected.add(convertor.convert(item));
		return collected;
	}

	public static <F, T> Collection<T> extract(Object iterable, String propertyName) {
		return convert((Iterable<F>) iterable, new PropertyExtractor<F, T>(propertyName));
	}
}
