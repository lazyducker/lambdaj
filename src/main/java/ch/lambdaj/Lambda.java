// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj;

import java.util.*;

import org.hamcrest.*;

import ch.lambdaj.function.aggregate.*;
import ch.lambdaj.function.argument.*;
import ch.lambdaj.function.compare.*;
import ch.lambdaj.function.convert.*;
import ch.lambdaj.proxy.*;

/**
 * @author Mario Fusco
 * @author Luca Marrocco
 */
@SuppressWarnings("unchecked")
public final class Lambda {
	
	private Lambda() { }
	
	/**
	 * Constructs a proxy object that mocks the given Class registering all the subsequent invocations on the object.
	 * @param clazz The class of the object to be mocked
	 * @return An object of the given class that register all the invocations made on it
	 */
	public static <T> T on(Class<T> clazz) {
		return ArgumentsFactory.createArgument(clazz);
	}

	/**
	 * Transforms a collection of Ts in a single object having the same methods of a single instance of T.
	 * That allows to invoke a method on each T in the collection with a single strong typed method call as in the following example:
	 * <p/>
	 * <code>
	 * 		List<Person> personInFamily = asList(new Person("Domenico"), new Person("Mario"), new Person("Irma"));
	 *		forEach(personInFamily).setLastName("Fusco");
	 * </code>
	 * <p/>
	 * The actual class of T is inferred from the class of the first iterable's item, but you can
	 * specify a particular class by using the overloaded method.
	 * @param <T> The type of the items in the iterable
	 * @param iterable The iterable to be transformed
	 * @return An object that proxies all the item in the iterable or null if the iterable is null or empty
	 */
	public static <T> T forEach(Iterable<? extends T> iterable) {
		if (iterable == null) return null;
		Iterator<? extends T> iterator = iterable.iterator();
		return (iterator.hasNext()) ? forEach(iterable, iterator.next().getClass()) : null;
	}

	/**
	 * Transforms a collection of Ts in a single object having the same methods of a single instance of T.
	 * That allows to invoke a method on each T in the collection with a single strong typed method call as in the following example:
	 * <p/>
	 * <code>
	 * 		List<Person> personInFamily = asList(new Person("Domenico"), new Person("Mario"), new Person("Irma"));
	 *		forEach(personInFamily, Person.class).setLastName("Fusco");
	 * </code>
	 * <p/>
	 * The given class represents the proxied by the returned object, so it should be a superclass of all the objects in the iterable.
	 * @param <T> The type of the items in the iterable
	 * @param iterable The iterable to be transformed
	 * @param clazz The class proxied by the returned object
	 * @return An object that proxies all the item in the iterable. If the given iterable is null or empty it returns
	 * an instance of T that actually proxies an empty Iterable of Ts
	 */
	public static <T> T forEach(Iterable<? extends T> iterable, Class<?> clazz) {
		return ProxyIterator.createProxyIterator(iterable, clazz);
	}

	// ////////////////////////////////////////////////////////////////////////
	// /// Collection
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Collects the items in the given iterable putting them in a List.
	 * Actually it treats also Maps as Iterables by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the forEach one.
	 * @param iterable The iterable of which the items should be collected
	 * @return A List containing all the items collected from the give iterable
	 * @throws A RuntimeException if the iterable is not an Iterable or a Map
	 */
	public static <T> List<? extends T> collect(Object iterable) {
		if (!(iterable instanceof Iterable) && !(iterable instanceof Map)) 
			throw new RuntimeException(iterable + " is not an iterable");
		List<T> collected = new LinkedList<T>();
		for (Object item : (Iterable<?>) iterable) {
			if (item instanceof Iterable) collected.addAll((Collection<T>) collect(item));
			else if (item instanceof Map) collected.addAll((Collection<T>) collect(((Map<?,?>)item).values()));
			else collected.add((T) item);
		}
		return collected;
	}
	
	/**
	 * For each item in the given iterable collects the value defined by the given argument and put them in a List.
	 * For example the following code:
	 * <p/>
	 * <code>
	 * 		List<Person> myFriends = asList(new Person("Biagio", 39), new Person("Luca", 29), new Person("Celestino", 29));
	 *		List<Integer> ages = collect(meAndMyFriends, on(Person.class).getAge());
	 * </code>
	 * <p/>
	 * extracts the ages of all the Persons in the list and put them in a List of Integer.
	 * <p/>
	 * Actually it treats also Maps as Iterables by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the forEach one.
	 * @param iterable The iterable of which the items should be collected
	 * @param argument An argument defined using the on method 
	 * @return A List containing all the items collected from the give iterable
	 * @throws A RuntimeException if the iterable is not an Iterable or a Map
	 */
	public static <T> List<T> collect(Object iterable, T argument) {
		return (List<T>)collect(convert(iterable, new ArgumentConverter<Object, Object>(argument)));
	}

	// ////////////////////////////////////////////////////////////////////////
	// /// Selection
	// ////////////////////////////////////////////////////////////////////////


	public static <T> List<T> filter(Matcher<?> matcher, Iterable<T> iterable) {
		return select(iterable, matcher);
	}

	public static <T> List<T> select(Iterable<T> iterable, Matcher<?> matcher) {
		List<T> collected = new LinkedList<T>();
		if (iterable == null) return collected;
		for (T item : iterable) if (matcher.matches(item)) collected.add(item);
		return collected;
	}

	public static <T> List<T> select(Object iterable, Matcher<?> matcher) {
		return select((Iterable<T>) iterable, matcher);
	}
	
	public static <T> T selectUnique(Object iterable, Matcher<?> matcher) {
		return selectUnique((Iterable<T>) iterable, matcher);
	}

	public static <T> T selectUnique(Iterable<T> iterable, Matcher<?> matcher) {
		T unique = null;
		if (iterable == null) return unique;
		Iterator<T> iterator = iterable.iterator();
		while (iterator.hasNext()) {
			T item = iterator.next();
			if (matcher.matches(item)) {
				unique = item;
				break;
			}
		}
		if (unique == null) return null;
		while (iterator.hasNext()) {
			if (matcher.matches(iterator.next())) throw new RuntimeException("Not unique item");
		}
		return unique;
	}

	public static <T> T selectFirst(Object iterable, Matcher<?> matcher) {
		return selectFirst((Iterable<T>) iterable, matcher);
	}

	public static <T> T selectFirst(Iterable<T> iterable, Matcher<?> matcher) {
		if (iterable == null) return null;
		for (T item : iterable) if (matcher.matches(item)) return item;
		return null;
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

	private static final Sum Sum = new Sum();

	private static final Min Min = new Min();

	private static final Max Max = new Max();

	private static final Concat Concat = new Concat();

	/**
	 * Aggregates the items in the given iterable using the given aggregator function.
	 * Note that this method accepts an Object in order to be used in conjunction with the forEach one.
	 * @param iterable The iterable of numbers to be summed
	 * @param aggregator The function that defines how the objects in this iterable have to be aggregated
	 * @return The result of the aggregation of all the items in the given iterable
	 * @throws A RuntimeException if the iterable is not an Iterable
	 */
	public static <T> T aggregate(Object iterable, Aggregator<T> aggregator) {
		T result = aggregator.emptyItem();
		if (iterable != null) for (T item : (Iterable<T>) iterable)
			result = aggregator.aggregate(result, item);
		return result;
	}

	public static <T> T aggregate(Object iterable, Aggregator<T> aggregator, Object argument) {
		if (!(iterable instanceof Iterable)) throw new RuntimeException(iterable + " is not an iterable");
		return aggregate(convert(iterable, new ArgumentConverter<Object, Object>(argument)), aggregator);
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

	/**
	 * Sums the items in the given iterable of Numbers or the iterable itself if it actually is already a single number.
	 * Note that this method accepts an Object in order to be used in conjunction with the forEach one.
	 * @param iterable The iterable of numbers to be summed
	 * @return The sum of all the Number in the given iterable or the iterable itself if it actually is already a single number
	 * @throws A RuntimeException if the iterable is not an Iterable
	 */
	public static Number sum(Object iterable) {
		if (iterable instanceof Number) return (Number)iterable;
		return aggregate(iterable, Sum);
	}

	/**
	 * Sums the property values of the items in the given iterable defined by the given argument.
	 * Note that this method accepts an Object in order to be used in conjunction with the forEach one.
	 * @param iterable The iterable of items containing the property of which the values have to be summed.
	 * @return The sum of the property values extracted from all the items in the given iterable 
	 * @throws A RuntimeException if the iterable is not an Iterable
	 */
	public static <T> T sum(Object iterable, T argument) {
		return (T)aggregate(iterable, Sum, argument);
	}
	
	/**
	 * Returns a lambda function defined as:
	 * <p/>
	 * 		sumFrom : (+, iterable) => lambda : (convert : object => number) => number
	 * <p/>
	 * It is then possibly to curry this function by selecting the convert function that defines of each item must be converted in a number.
	 * This is done by invoking on that returned object the method that returns the values of the property to be summed as in the following example
	 * <p/>
	 * <code>
	 * 		int totalAge = sumFrom(persons).getAge()
	 * </code>
	 * <p/>
	 * @param iterable The iterable of the objects to containing the property to be summed.
	 * @return A proxy of the class of the first object in the iterable representing a sum lambda function or null if the iterable is empty
	 */
	public static <T> T sumFrom(Iterable<T> iterable) {
		return aggregateFrom(iterable, Sum);
	}

	public static <T> T sumFrom(Iterable<T> iterable, Class<?> t) {
		return aggregateFrom(iterable, t, Sum);
	}

	// -- (Min) ---------------------------------------------------------------

	public static <T> T min(Object iterable) {
		return (T) aggregate((Iterable<T>) iterable, Min);
	}

	public static <T> T min(Object iterable, T argument) {
		return (T)aggregate(iterable, Min, argument);
	}
	
	public static <T> T minFrom(Iterable<T> iterable) {
		return (T) aggregateFrom(iterable, Min);
	}

	public static <T> T minFrom(Iterable<T> iterable, Class<?> t) {
		return (T) aggregateFrom(iterable, t, Min);
	}

	// -- (Max) ---------------------------------------------------------------

	public static <T> T max(Object iterable) {
		return (T) aggregate((Iterable<T>) iterable, Max);
	}

	public static <T> T max(Object iterable, T argument) {
		return (T)aggregate(iterable, Max, argument);
	}
	
	public static <T> T maxFrom(Iterable<T> iterable) {
		return (T) aggregateFrom(iterable, Max);
	}

	public static <T> T maxFrom(Iterable<T> iterable, Class<?> t) {
		return (T) aggregateFrom(iterable, t, Max);
	}

	// -- (Concat) ------------------------------------------------------------

	public static <T> T joinFrom(Iterable<T> c) {
		return aggregateFrom(c, Concat);
	}

	public static <T> T joinFrom(Iterable<T> c, String separator) {
		return aggregateFrom(c, new Concat(separator));
	}

	public static <T> T joinFrom(Iterable<T> c, Class<?> t) {
		return aggregateFrom(c, t, Concat);
	}

	public static <T> T joinFrom(Iterable<T> c, Class<?> t, String separator) {
		return aggregateFrom(c, t, new Concat(separator));
	}

	public static String join(Object iterable) {
		return join(iterable, ", ");
	}
	
	public static String join(Object iterable, String separator) {
		return iterable instanceof Iterable ? (String) aggregate((Iterable<?>) iterable, new Concat(separator)) : (iterable == null ? "" : iterable.toString());
	}

	// ////////////////////////////////////////////////////////////////////////
	// /// Conversion
	// ////////////////////////////////////////////////////////////////////////

	public static <F, T> List<T> convert(Object iterable, Converter<F, T> convertor) {
		List<T> collected = new ArrayList<T>();
		if (iterable != null) for (F item : (Iterable<F>) iterable)
			collected.add(convertor.convert(item));
		return collected;
	}

	public static <F, T> List<T> extract(Object iterable, T argument) {
		return convert(iterable, new ArgumentConverter<F, T>(argument));
	}
	
	public static <F, T> List<T> extractProperty(Object iterable, String propertyName) {
		return convert(iterable, new PropertyExtractor<F, T>(propertyName));
	}
	
	public static <F, T> Map<T, F> map(Object iterable, Converter<F, T> convertor) {
		Map<T, F> map = new HashMap<T, F>();
		if (iterable != null) for (F item : (Iterable<F>) iterable)
			map.put(convertor.convert(item), item);
		return map;
	}
	
	public static <F, T> Map<T, F> index(Object iterable, T argument) {
		return map(iterable, new ArgumentConverter<F, T>(argument));
	}
}