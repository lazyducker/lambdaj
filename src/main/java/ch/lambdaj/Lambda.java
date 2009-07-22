// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;
import static ch.lambdaj.function.closure.ClosuresFactory.*;
import static ch.lambdaj.function.matcher.HasArgumentWithValue.*;

import java.lang.reflect.Array;
import java.util.*;

import org.hamcrest.*;

import ch.lambdaj.function.aggregate.*;
import ch.lambdaj.function.argument.*;
import ch.lambdaj.function.closure.*;
import ch.lambdaj.function.compare.*;
import ch.lambdaj.function.convert.*;
import ch.lambdaj.function.matcher.*;
import ch.lambdaj.proxy.*;

/**
 * This class consists exclusively of static methods that allow to use all the core features of the lambdaj library.
 * @author Mario Fusco
 */
@SuppressWarnings("unchecked")
public final class Lambda {

	private Lambda() { }

	/**
	 * Constructs a proxy object that mocks the given Class registering all the subsequent invocations on the object.
	 * @param clazz The class of the object to be mocked
	 * @return An object of the given class that register all the invocations made on it
	 */
	public static <T> T on(final Class<T> clazz) {
		return createArgument(clazz);
	}

	/**
	 * Returns the actual argument of the methods invocation sequence defined through the {@link Lambda#on(Class)} method.
	 * @param argumentPlaceholder The placeholder for this argument created using the {@link Lambda#on(Class)} method
	 */
	public static <T> Argument<T> argument(final T argumentPlaceholder) {
		return actualArgument(argumentPlaceholder);
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
	 * @throws An IllegalArgumentException if the iterable is null or empty
	 */
	public static <T> T forEach(final Iterable<? extends T> iterable) {
		if (iterable == null) 
                    throw new IllegalArgumentException("The iterable cannot be null");
		
		Iterator<? extends T> iterator = iterable.iterator();
		if (!iterator.hasNext())
                    throw new IllegalArgumentException("forEach() is unable to introspect on an empty iterator. Use the overloaded method accepting a class instead");

		return forEach(iterable, (Class<T>)iterator.next().getClass());
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
	 * This overloaded version should be always used when it is not insured that the given iterable is null or empty.
	 * @param <T> The type of the items in the iterable
	 * @param iterable The iterable to be transformed
	 * @param clazz The class proxied by the returned object
	 * @return An object that proxies all the item in the iterable. If the given iterable is null or empty it returns
	 * an instance of T that actually proxies an empty Iterable of Ts
	 */
	public static <T> T forEach(final Iterable<? extends T> iterable, final Class<T> clazz) {
		return ProxyIterator.createProxyIterator(iterable, clazz);
	}

	// ////////////////////////////////////////////////////////////////////////
	// /// Collection
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Collects the items in the given iterable putting them in a List.
	 * Actually it treats also Maps as Iterables by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of which the items should be collected
	 * @return A List containing all the items collected from the give iterable
	 * @throws A IllegalArgumentException if the iterable is not an Iterable or a Map
	 */
        public static <T> List<? extends T> collect(final Object iterable) {
            if (!(iterable instanceof Iterable) && !(iterable instanceof Map)) {
                throw new IllegalArgumentException(iterable + " is not an iterable");
            }

            List<T> collected = new ArrayList<T>();
            for (Object item : (Iterable<?>) iterable) {
                if (item instanceof Iterable) {
                    collected.addAll((Collection<T>) collect(item));
                } else if (item instanceof Map) {
                    collected.addAll((Collection<T>) collect(((Map<?, ?>) item).values()));
                } else {
                    collected.add((T) item);
                }
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
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of which the items should be collected
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method
	 * @return A List containing all the items collected from the give iterable
	 * @throws A RuntimeException if the iterable is not an Iterable or a Map
	 */
	public static <T> List<T> collect(final Object iterable, final T argument) {
		return (List<T>)collect(convert(iterable, new ArgumentConverter<Object, T>(argument)));
	}

	// ////////////////////////////////////////////////////////////////////////
	// /// Sort
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Sorts all the items in the given iterable on the respective values of the given argument.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects to be sorted
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method
	 * @return A List with the same items of the given iterable sorted on the respective value of the given argument
	 */
	public static <T> List<T> sort(final Object iterable, final Object argument) {
		return sort(iterable, argument, null);
	}

	/**
	 * Sorts all the items in the given iterable on the respective values of the given argument comparing them with the given comparator.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects to be sorted
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method
	 * @param comparator The comparator to determine the order of the list. A null value indicates that the elements' natural ordering should be used
	 * @return A List with the same items of the given iterable sorted on the respective value of the given argument
	 */
	public static <T, A> List<T> sort(final Object iterable, final A argument, final Comparator<A> comparator) {
		List<T> sorted = new ArrayList<T>();
		for (T item : (Iterable<T>)iterable) {
                    sorted.add(item);
                }
		Collections.sort(sorted, new ArgumentComparator<T, A>(argument, comparator));
		return sorted;
	}

	// ////////////////////////////////////////////////////////////////////////
	// /// Selection
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Filters all the objects in the given iterable that match the given hamcrest Matcher
	 * @param iterable The iterable of objects to be filtered
	 * @param matcher The hamcrest Matcher used to filter the given iterable
	 * @return A sublist of the given iterable containing all the objects that match the given hamcrest Matcher
	 */
	public static <T> List<T> filter(final Matcher<?> matcher, final Iterable<T> iterable) {
		return select(iterable, matcher);
	}

	/**
	 * Selects all the objects in the given iterable that match the given hamcrest Matcher
	 * @param iterable The iterable of objects to be filtered
	 * @param matcher The hamcrest Matcher used to filter the given iterable
	 * @return A sublist of the given iterable containing all the objects that match the given hamcrest Matcher
	 */
	public static <T> List<T> select(final Iterable<T> iterable, final Matcher<?> matcher) {
		List<T> collected = new ArrayList<T>();
		if (iterable == null)
                    return collected;

		for (T item : iterable) {
                    if (matcher.matches(item))
                        collected.add(item);
		}
		return collected;
	}

	/**
	 * Selects all the objects in the given iterable that match the given hamcrest Matcher
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects to be filtered
	 * @param matcher The hamcrest Matcher used to filter the given iterable
	 * @return A sublist of the given iterable containing all the objects that match the given hamcrest Matcher
	 */
	public static <T> List<T> select(final Object iterable, final Matcher<?> matcher) {
		return select((Iterable<T>) iterable, matcher);
	}

	/**
	 * Selects the unique object in the given iterable that matches the given hamcrest Matcher
	 * @param iterable The iterable of objects to be filtered
	 * @param matcher The hamcrest Matcher used to filter the given iterable
	 * @return The only object in the given iterable that matches the given hamcrest Matcher or null if there is no such object
	 * @throws A Runtime Exception if there is more than one object that matches the given hamcrest Matcher
	 */
	public static <T> T selectUnique(final Object iterable, final Matcher<?> matcher) {
		return selectUnique((Iterable<T>) iterable, matcher);
	}

	/**
	 * Selects the unique object in the given iterable that matches the given hamcrest Matcher
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects to be filtered
	 * @param matcher The hamcrest Matcher used to filter the given iterable
	 * @return The only object in the given iterable that matches the given hamcrest Matcher or null if there is no such object
	 * @throws A Runtime Exception if there is more than one object that matches the given hamcrest Matcher
	 */
	public static <T> T selectUnique(final Iterable<T> iterable, final Matcher<?> matcher) {
	        T unique = null;
		if (iterable == null)
                        return unique;
        
		Iterator<T> iterator = iterable.iterator();
		while (iterator.hasNext() && unique == null) {
			T item = iterator.next();
			if (matcher.matches(item)) 
                            unique = item;
		}
		while (iterator.hasNext()) {
			if (matcher.matches(iterator.next()))
			    throw new RuntimeException("Not unique item");
		}
		return unique;
	}

	/**
	 * Selects the first object in the given iterable that matches the given hamcrest Matcher
	 * @param iterable The iterable of objects to be filtered
	 * @param matcher The hamcrest Matcher used to filter the given iterable
	 * @return The first object in the given iterable that matches the given hamcrest Matcher or null if there is no such object
	 */
	public static <T> T selectFirst(final Object iterable, final Matcher<?> matcher) {
		return selectFirst((Iterable<T>) iterable, matcher);
	}

	/**
	 * Selects the first object in the given iterable that matches the given hamcrest Matcher
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects to be filtered
	 * @param matcher The hamcrest Matcher used to filter the given iterable
	 * @return The first object in the given iterable that matches the given hamcrest Matcher or null if there is no such object
	 */
	public static <T> T selectFirst(final Iterable<T> iterable, final Matcher<?> matcher) {
		if (iterable == null)
                    return null;
		
		for (T item : iterable) {
                    if (matcher.matches(item)) {
                        return item;
                    }
                }
		return null;
	}

	/**
	 * Filters away all the duplicated items in the given iterable.
	 * @param iterable The iterable of objects to be filtered
	 * @return A Collection with the same items of the given iterable but containing no duplicate elements
	 */
	public static <T> Collection<T> selectDistinct(final Iterable<T> iterable) {
		return selectDistinct(iterable, (Comparator<T>) null);
	}

	/**
	 * Filters away all the duplicated items in the given iterable.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects to be filtered
	 * @return A Collection with the same items of the given iterable but containing no duplicate elements
	 */
	public static <T> Collection<T> selectDistinct(final Object iterable) {
		return selectDistinct(iterable, (Comparator<T>) null);
	}

	/**
	 * Selects all the items in the given iterable having a different value in the named property.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects to be filtered
	 * @param propertyName The name of the item's property on which the item must have no duplicated value
	 * @return A Collection with the same items of the given iterable but containing no duplicate values on the named property
	 */
	public static <T> Collection<T> selectDistinct(final Object iterable, final String propertyName) {
		return selectDistinct(iterable, new PropertyComparator<T>(propertyName));
	}

	/**
	 * Selects all the items in the given iterable having a different value on the given argument defined using the on method.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects to be filtered
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method
	 * @return A Collection with the same items of the given iterable but containing no duplicate values on the given argument
	 */
	public static <T, A> Collection<T> selectDistinctArgument(final Object iterable, final A argument) {
		return selectDistinct(iterable, new ArgumentComparator<T, A>(argument));
	}

	/**
	 * Filters away all the duplicated items in the given iterable based on the given comparator.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects to be filtered
	 * @param comparator The comparator used to decide if 2 items are different or not
	 * @return A Collection with the same items of the given iterable but containing no duplicate elements
	 */
	public static <T> Collection<T> selectDistinct(final Object iterable, final Comparator<T> comparator) {
		Set<T> collected = comparator == null ? new HashSet<T>() : new TreeSet<T>(comparator);
		if (iterable != null) {
                    for (T item : (Iterable<T>) iterable) {
                        collected.add(item);
                    }
                }
		return collected;
	}

	/**
	 * Selects the item in the given iterable having the lowest value on the given argument defined using the on method.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects to be filtered
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method
	 * @return The item in the given iterable with the minimum value on the given argument
	 */
	public static <T, A> T selectMin(final Object iterable, final A argument) {
		return aggregate(iterable, new MinOnArgument<T, A>(argument));
	}

	/**
	 * Selects the item in the given iterable having the highest value on the given argument defined using the on method.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects to be filtered
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method
	 * @return The item in the given iterable with the maximum value on the given argument
	 */
	public static <T, A> T selectMax(final Object iterable, final A argument) {
		return aggregate(iterable, new MaxOnArgument<T, A>(argument));
	}

	// ////////////////////////////////////////////////////////////////////////
	// /// Aggregation
	// ////////////////////////////////////////////////////////////////////////

	private static final Sum Sum = new Sum();
	private static final SumInteger SumInteger = new SumInteger();
	private static final SumLong SumLong = new SumLong();
	private static final SumDouble SumDouble = new SumDouble();

	private static Aggregator<? extends Number> getSumAggregator(final Object object) {
		if (object instanceof Integer) {
            return SumInteger;
        }
		if (object instanceof Double) {
            return SumDouble;
        }
		if (object instanceof Long) {
            return SumLong;
        }
		return Sum;
	}

	private static final Min Min = new Min();

	private static final Max Max = new Max();

	private static final Concat Concat = new Concat();

	/**
	 * Aggregates the items in the given iterable using the given {@link Aggregator}.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of numbers to be summed
	 * @param aggregator The function that defines how the objects in this iterable have to be aggregated
	 * @return The result of the aggregation of all the items in the given iterable
	 * @throws A RuntimeException if the iterable is not an Iterable
	 */
	public static <T> T aggregate(final Object iterable, final Aggregator<T> aggregator) {
		T result = aggregator.emptyItem();
		if (iterable != null) {
                    for (T item : (Iterable<T>) iterable) {
                        result = aggregator.aggregate(result, item);
                    }
                }
		return result;
	}

	/**
	 * For each item in the given iterable collects the value defined by the given argument and
	 * then aggregates them iterable using the given {@link Aggregator}.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of numbers to be summed
	 * @param aggregator The function that defines how the objects in this iterable have to be aggregated
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method
	 * @return The result of the aggregation of all the items in the given iterable
	 * @throws A RuntimeException if the iterable is not an Iterable
	 */
	public static <T, A> T aggregate(final Object iterable, final Aggregator<T> aggregator, final A argument) {
		if (!(iterable instanceof Iterable)) 
                    throw new RuntimeException(iterable + " is not an iterable");
		
		return aggregate(convert(iterable, new ArgumentConverter<T, A>(argument)), aggregator);
	}

	/**
	 * Returns a lambda function defined as:
	 * <p/>
	 * 		aggregateFrom : (aggregator, iterable) => lambda : (convert : object => object) => object
	 * <p/>
	 * It is then possibly to curry this function by selecting the convert function that defines how each item must be converted in the object to be aggregated.
	 * This is done by invoking on that returned object the method that returns the values of the property to be aggregated.
	 * @param iterable The iterable of the objects to containing the property to be aggregated.
	 * @param aggregator The function that defines how the objects in this iterable have to be aggregated
	 * @return A proxy of the class of the first object in the iterable representing an aggregation lambda function
	 * @throws An IllegalArgumentException if the iterable is null or empty
	 */
	public static <T, A> T aggregateFrom(final Iterable<T> iterable, final Aggregator<A> aggregator) {
		if (iterable == null) 
                    throw new IllegalArgumentException("The iterable cannot be null");

		Iterator<T> iterator = iterable.iterator();
		if (!iterator.hasNext())
		    throw new IllegalArgumentException("aggregateFrom() is unable to introspect on an empty iterator. Use the overloaded method accepting a class instead");
        
		return aggregateFrom(iterable, iterator.next().getClass(), aggregator);
	}

	/**
	 * Returns a lambda function defined as:
	 * <p/>
	 * 		aggregateFrom : (aggregator, iterable) => lambda : (convert : object => object) => object
	 * <p/>
	 * It is then possibly to curry this function by selecting the convert function that defines how each item must be converted in the object to be aggregated.
	 * This is done by invoking on that returned object the method that returns the values of the property to be aggregated.
	 * This overloaded version should be always used when it is not insured that the given iterable is null or empty.
	 * @param iterable The iterable of the objects to containing the property to be aggregated.
	 * @param clazz The class proxied by the returned object
	 * @param aggregator The function that defines how the objects in this iterable have to be aggregated
	 * @return A proxy of the class of the first object in the iterable representing an aggregation lambda function
	 */
	public static <T, A> T aggregateFrom(final Iterable<T> iterable, final Class<?> clazz, final Aggregator<A> aggregator) {
		return ProxyAggregator.createProxyAggregator(iterable, aggregator, clazz);
	}

	// -- (Sum) ---------------------------------------------------------------

	/**
	 * Sums the items in the given iterable of Numbers or the iterable itself if it actually is already a single number.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of numbers to be summed
	 * @return The sum of all the Number in the given iterable or the iterable itself if it actually is already a single number
	 * @throws An IllegalArgumentException if the iterable is not neither an Iterable nor a Number
	 */
	public static Number sum(final Object iterable) {
		if (iterable instanceof Number) 
                    return (Number)iterable;

		if (!(iterable instanceof Iterable))
                    return 0.0;
        
		Iterator<?> iterator = ((Iterable<?>)iterable).iterator();
		return iterator.hasNext() ? aggregate(iterable, getSumAggregator(iterator.next())) : 0.0;
	}

	/**
	 * Sums the property values of the items in the given iterable defined by the given argument.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of items containing the property of which the values have to be summed.
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method
	 * @return The sum of the property values extracted from all the items in the given iterable
	 * @throws An IllegalArgumentException if the iterable is not an Iterable
	 */
	public static <T> T sum(final Object iterable, final T argument) {
		return (T)aggregate(iterable, getSumAggregator(argument), argument);
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
	 * 		int totalAge = sumFrom(persons).getAge();
	 * </code>
	 * <p/>
	 * The actual class of T is inferred from the class of the first iterable's item, but you can
	 * specify a particular class by using the overloaded method.
	 * @param iterable The iterable of the objects to containing the property to be summed.
	 * @return A proxy of the class of the first object in the iterable representing a sum lambda function
	 * @throws An IllegalArgumentException if the iterable is null or empty
	 */
	public static <T> T sumFrom(final Iterable<T> iterable) {
		return aggregateFrom(iterable, Sum);
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
	 * 		int totalAge = sumFrom(persons, Person.class).getAge();
	 * </code>
	 * <p/>
	 * This overloaded version should be always used when it is not insured that the given iterable is null or empty.
	 * @param iterable The iterable of the objects to containing the property to be summed.
	 * @param clazz The class proxied by the returned object
	 * @return A proxy of the class of the first object in the iterable representing a sum lambda function
	 */
	public static <T> T sumFrom(final Iterable<T> iterable, final Class<?> clazz) {
		return aggregateFrom(iterable, clazz, Sum);
	}

	// -- (Min) ---------------------------------------------------------------

	/**
	 * Finds the minimum item in the given iterable.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of numbers to be summed
	 * @return The minimum of all the Object in the given iterable
	 * @throws An IllegalArgumentException if the iterable is not an Iterable
	 */
	public static <T> T min(final Object iterable) {
		return (T) aggregate((Iterable<T>) iterable, Min);
	}

	/**
	 * Finds the minimum item in the given iterable defined by the given argument.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects on which the minimum should be found
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method
	 * @return The minimum of all the Object in the given iterable
	 * @throws An IllegalArgumentException if the iterable is not an Iterable
	 */
	public static <T> T min(final Object iterable, final T argument) {
		return (T)aggregate(iterable, Min, argument);
	}

	/**
	 * Returns a lambda function defined as:
	 * <p/>
	 * 		minFrom : (min, iterable) => lambda : (convert : object => object) => object
	 * <p/>
	 * It is then possibly to curry this function by selecting the convert function that defines how each item
	 * must be converted in the object of which a minimum value needs to be found.
	 * This is done by invoking on that returned object the method that returns the values of the property to be aggregated.
	 * <p/>
	 * <code>
	 * 		int minAge = maxFrom(persons).getAge();
	 * </code>
	 * <p/>
	 * The actual class of T is inferred from the class of the first iterable's item, but you can
	 * specify a particular class by using the overloaded method.
	 * @param iterable The iterable of objects on which the minimum should be found
	 * @return A proxy of the class of the first object in the iterable representing a min lambda function
	 * @throws An IllegalArgumentException if the iterable is null or empty
	 */
	public static <T> T minFrom(final Iterable<T> iterable) {
		return (T) aggregateFrom(iterable, Min);
	}

	/**
	 * Returns a lambda function defined as:
	 * <p/>
	 * 		minFrom : (min, iterable) => lambda : (convert : object => object) => object
	 * <p/>
	 * It is then possibly to curry this function by selecting the convert function that defines how each item
	 * must be converted in the object of which a minimum value needs to be found.
	 * This is done by invoking on that returned object the method that returns the values of the property to be aggregated.
	 * <p/>
	 * <code>
	 * 		int minAge = minFrom(persons).getAge();
	 * </code>
	 * <p/>
	 * This overloaded version should be always used when it is not insured that the given iterable is null or empty.
	 * @param iterable The iterable of the objects containing the property of which the minimum should be found.
	 * @param clazz The class proxied by the returned object
	 * @return A proxy of the class of the first object in the iterable representing a min lambda function
	 */
	public static <T> T minFrom(final Iterable<T> iterable, final Class<?> clazz) {
		return (T) aggregateFrom(iterable, clazz, Min);
	}

	// -- (Max) ---------------------------------------------------------------

	/**
	 * Finds the maximum item in the given iterable.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects on which the maximum should be found
	 * @return The maximum of all the Object in the given iterable
	 * @throws An IllegalArgumentException if the iterable is not an Iterable
	 */
	public static <T> T max(final Object iterable) {
		return (T) aggregate((Iterable<T>) iterable, Max);
	}

	/**
	 * Finds the maximum item in the given iterable defined by the given argument.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects on which the maximum should be found
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method
	 * @return The maximum of all the Object in the given iterable
	 * @throws An IllegalArgumentException if the iterable is not an Iterable
	 */
	public static <T> T max(final Object iterable, final T argument) {
		return (T)aggregate(iterable, Max, argument);
	}

	/**
	 * Returns a lambda function defined as:
	 * <p/>
	 * 		maxFrom : (max, iterable) => lambda : (convert : object => object) => object
	 * <p/>
	 * It is then possibly to curry this function by selecting the convert function that defines how each item
	 * must be converted in the object of which a maximum value needs to be found.
	 * This is done by invoking on that returned object the method that returns the values of the property to be aggregated.
	 * <p/>
	 * <code>
	 * 		int maxAge = maxFrom(persons).getAge();
	 * </code>
	 * <p/>
	 * The actual class of T is inferred from the class of the first iterable's item, but you can
	 * specify a particular class by using the overloaded method.
	 * @param iterable The iterable of objects on which the maximum should be found
	 * @return A proxy of the class of the first object in the iterable representing a max lambda function
	 * @throws An IllegalArgumentException if the iterable is null or empty
	 */
	public static <T> T maxFrom(final Iterable<T> iterable) {
		return (T) aggregateFrom(iterable, Max);
	}

	/**
	 * Returns a lambda function defined as:
	 * <p/>
	 * 		maxFrom : (max, iterable) => lambda : (convert : object => object) => object
	 * <p/>
	 * It is then possibly to curry this function by selecting the convert function that defines how each item
	 * must be converted in the object of which a maximum value needs to be found.
	 * This is done by invoking on that returned object the method that returns the values of the property to be aggregated.
	 * <p/>
	 * <code>
	 * 		int maxAge = maxFrom(persons).getAge();
	 * </code>
	 * <p/>
	 * This overloaded version should be always used when it is not insured that the given iterable is null or empty.
	 * @param iterable The iterable of the objects containing the property of which the maximum should be found.
	 * @param clazz The class proxied by the returned object
	 * @return A proxy of the class of the first object in the iterable representing a max lambda function
	 */
	public static <T> T maxFrom(final Iterable<T> iterable, final Class<?> clazz) {
		return (T) aggregateFrom(iterable, clazz, Max);
	}

	// -- (Join) --------------------------------------------------------------

	/**
	 * Returns a lambda function defined as:
	 * <p/>
	 * 		joinFrom : (concat, iterable) => lambda : (convert : object => object) => string
	 * <p/>
	 * It is then possibly to curry this function by selecting the convert function that defines of each item must be converted in a String.
	 * This is done by invoking on that returned object the method that returns the values of the property to be summed as in the following example
	 * <p/>
	 * <code>
	 * 		String names = joinFrom(persons).getFirstName();
	 * </code>
	 * <p/>
	 * The actual class of T is inferred from the class of the first iterable's item, but you can
	 * specify a particular class by using the overloaded method.
	 * @param iterable The iterable of the objects to containing the property to be joined.
	 * @return A proxy of the class of the first object in the iterable representing a join lambda function
	 * @throws An IllegalArgumentException if the iterable is null or empty
	 */
	public static <T> T joinFrom(final Iterable<T> iterable) {
		return aggregateFrom(iterable, Concat);
	}

	/**
	 * Returns a lambda function defined as:
	 * <p/>
	 * 		joinFrom : (concat, iterable) => lambda : (convert : object => object) => string
	 * <p/>
	 * It is then possibly to curry this function by selecting the convert function that defines of each item must be converted in a String.
	 * This is done by invoking on that returned object the method that returns the values of the property to be summed as in the following example
	 * <p/>
	 * <code>
	 * 		String names = joinFrom(persons, " - ").getFirstName();
	 * </code>
	 * <p/>
	 * The actual class of T is inferred from the class of the first iterable's item, but you can
	 * specify a particular class by using the overloaded method.
	 * @param iterable The iterable of the objects to containing the property to be joined.
	 * @param separator The String used to separe the Strings produced by this lambda function
	 * @return A proxy of the class of the first object in the iterable representing a join lambda function
	 * @throws An IllegalArgumentException if the iterable is null or empty
	 */
	public static <T> T joinFrom(final Iterable<T> iterable, final String separator) {
		return aggregateFrom(iterable, new Concat(separator));
	}

	/**
	 * Returns a lambda function defined as:
	 * <p/>
	 * 		joinFrom : (concat, iterable) => lambda : (convert : object => object) => string
	 * <p/>
	 * It is then possibly to curry this function by selecting the convert function that defines of each item must be converted in a String.
	 * This is done by invoking on that returned object the method that returns the values of the property to be summed as in the following example
	 * <p/>
	 * <code>
	 * 		String names = joinFrom(persons, Person.class).getFirstName();
	 * </code>
	 * <p/>
	 * The actual class of T is inferred from the class of the first iterable's item, but you can
	 * specify a particular class by using the overloaded method.
	 * This overloaded version should be always used when it is not insured that the given iterable is null or empty.
	 * @param iterable The iterable of the objects to containing the property to be joined.
	 * @param clazz The class proxied by the returned object
	 * @return A proxy of the class of the first object in the iterable representing a join lambda function
	 */
	public static <T> T joinFrom(final Iterable<T> iterable, final Class<?> clazz) {
		return aggregateFrom(iterable, clazz, Concat);
	}

	/**
	 * Returns a lambda function defined as:
	 * <p/>
	 * 		joinFrom : (concat, iterable) => lambda : (convert : object => object) => string
	 * <p/>
	 * It is then possibly to curry this function by selecting the convert function that defines of each item must be converted in a String.
	 * This is done by invoking on that returned object the method that returns the values of the property to be summed as in the following example
	 * <p/>
	 * <code>
	 * 		String names = joinFrom(persons, Person.class, " - ").getFirstName();
	 * </code>
	 * <p/>
	 * The actual class of T is inferred from the class of the first iterable's item, but you can
	 * specify a particular class by using the overloaded method.
	 * This overloaded version should be always used when it is not insured that the given iterable is null or empty.
	 * @param iterable The iterable of the objects to containing the property to be joined.
	 * @param clazz The class proxied by the returned object
	 * @param separator The String used to separe the Strings produced by this lambda function
	 * @return A proxy of the class of the first object in the iterable representing a join lambda function
	 */
	public static <T> T joinFrom(final Iterable<T> iterable, final Class<?> clazz, final String separator) {
		return aggregateFrom(iterable, clazz, new Concat(separator));
	}

	/**
	 * Joins all the object in the given iterable by concatenating all their String representation.
	 * It invokes toString() an all the objects and concatening them using the default separator ", ".
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable containing the objects to be joined
	 * @return The concatenation of the String representation of all the objects in the given iterable or an empty String if the iterable is null or empty
	 */
	public static String join(final Object iterable) {
		return join(iterable, ", ");
	}

	/**
	 * Joins all the object in the given iterable by concatenating all their String representation.
	 * It invokes toString() an all the objects and concatening them using the given separator.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable containing the objects to be joined
	 * @param separator The String used to separe the item's String representation
	 * @return The concatenation of the String representation of all the objects in the given iterable or an empty String if the iterable is null or empty
	 */
	public static String join(final Object iterable, final String separator) {
		return iterable instanceof Iterable ? (String) aggregate(iterable, new Concat(separator)) : (iterable == null ? "" : iterable.toString());
	}

	// ////////////////////////////////////////////////////////////////////////
	// /// Conversion
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Converts all the object in the iterable using the given {@link Converter}.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable containing the objects to be converted
	 * @param converter The converter that specifies how each object in the iterable must be converted
	 * @return A list containing all the objects in the given iterable converted using the given {@link Converter}
	 */
	public static <F, T> List<T> convert(final Object iterable, final Converter<F, T> converter) {
		List<T> collected = new ArrayList<T>();
		if (iterable != null) {
                    for (F item : (Iterable<F>) iterable) {
                        collected.add(converter.convert(item));
                    }
                }
		return collected;
	}

	/**
	 * Converts all the object in the iterable extracting the property defined by the given argument.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable containing the objects to be converted
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method
	 * @return A list containing the argument's value extracted from the object in the given iterable
	 */
	public static <F, T> List<T> extract(final Object iterable, final T argument) {
		return convert(iterable, new ArgumentConverter<F, T>(argument));
	}

	/**
	 * Converts all the object in the iterable in its String representation.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable containing the objects to be converted in strings
	 * @return A list containing the String representation of the objects in the given iterable
	 */
	public static List<String> extractString(final Object iterable) {
		return convert(iterable, new DefaultStringConverter());
	}

	/**
	 * Converts all the object in the iterable extracting the named property.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable containing the objects to be converted
	 * @param propertyName The name of the item's property on which the item must have no duplicated value
	 * @return A list containing the property's value extracted from the object in the given iterable
	 */
	public static <F, T> List<T> extractProperty(final Object iterable, final String propertyName) {
		return convert(iterable, new PropertyExtractor<F, T>(propertyName));
	}

	/**
	 * Maps the objects in the given iterable on the value extracted using the given {@link Converter}.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable containing the objects to be mapped
	 * @param converter The converter that specifies the key on which each object should be mapped
	 * @return A map having as keys the argument value extracted from the objects in the given iterable and as values the corresponding objects
	 */
	public static <F, T> Map<T, F> map(final Object iterable, final Converter<F, T> converter) {
		Map<T, F> map = new HashMap<T, F>();
		if (iterable != null) {
                    for (F item : (Iterable<F>) iterable) {
                        map.put(converter.convert(item), item);
                    }
                }
		return map;
	}

	/**
	 * Indexes the objects in the given iterable on the value of their argument.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable containing the objects to be indexed
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method
	 * @return A map having as keys the argument value extracted from the objects in the given iterable and as values the corresponding objects
	 */
	public static <F, T> Map<T, F> index(final Object iterable, final T argument) {
		return map(iterable, new ArgumentConverter<F, T>(argument));
	}

	// ////////////////////////////////////////////////////////////////////////
	// /// Matcher
	// ////////////////////////////////////////////////////////////////////////

	public static <T> HasArgumentWithValue<T, Boolean> having(final Boolean argument) {
        	return havingValue(argument);
        }
    
        public static <T, A> HasArgumentWithValue<T, A> having(final A argument, final Matcher<? extends Object> value) {
        	return havingValue(argument, value);
        }

	// ////////////////////////////////////////////////////////////////////////
	// /// Closure
	// ////////////////////////////////////////////////////////////////////////

	public static <T> T of(final T closed) {
		return of(closed, (Class<T>)closed.getClass());
	}

	public static <T> T of(final T closed, final Class<T> closedClass) {
		return bindClosure(closed, closedClass);
	}

	public static Closure closure() {
		return createClosure();
	}

	public static <A> Closure1<A> closure(final Class<A> type1) {
		return createClosure(type1);
	}

	public static <A, B> Closure2<A, B> closure(final Class<A> type1, final Class<B> type2) {
		return createClosure(type1, type2);
	}

	public static <A, B, C> Closure3<A, B, C> closure(final Class<A> type1, final Class<B> type2, final Class<C> type3) {
		return createClosure(type1, type2, type3);
	}
}