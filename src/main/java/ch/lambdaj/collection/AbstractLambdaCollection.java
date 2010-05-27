package ch.lambdaj.collection;

import ch.lambdaj.function.aggregate.*;
import ch.lambdaj.function.convert.*;
import ch.lambdaj.*;
import ch.lambdaj.group.*;

import java.util.*;

import org.hamcrest.*;

class AbstractLambdaCollection<T> {

    protected final Iterable<? extends T> innerIterable;
    protected final Iterator<? extends T> innerIterator;

    protected AbstractLambdaCollection(Iterator<? extends T> innerIterator) {
        this(null, innerIterator);
    }

    protected AbstractLambdaCollection(Iterable<? extends T> innerIterable) {
        this(innerIterable, innerIterable.iterator());
    }

    private AbstractLambdaCollection(Iterable<? extends T> innerIterable, Iterator<? extends T> innerIterator) {
        this.innerIterable = innerIterable;
        this.innerIterator = innerIterator;
    }

    private Object getInner() {
        return innerIterable != null ? innerIterable : innerIterator;
    }

    public T aggregate(Aggregator<T> aggregator) {
        return Lambda.aggregate(getInner(), aggregator);
    }

    public T aggregate(T argument, Aggregator<T> aggregator) {
        return Lambda.aggregate(getInner(), aggregator, argument);
    }

    /**
     * Finds the minimum item in this iterable defined by the given argument.
     * @param argument An argument defined using the {@link Lambda#on(Class)} method
     * @return The minimum of all the Object in the given iterable
     */
    public <A> A min(A argument) {
        return Lambda.min(getInner(), argument);
    }

    /**
     * Finds the maximum item in this iterable defined by the given argument.
     * @param argument An argument defined using the {@link Lambda#on(Class)} method
     * @return The maximum of all the Object in the given iterable
     */
    public <A> A max(A argument) {
        return Lambda.max(getInner(), argument);
    }

    /**
     * Sums the property values of the items in this iterable defined by the given argument.
     * @param argument An argument defined using the {@link Lambda#on(Class)} method
     * @return The sum of the property values extracted from all the items in the given iterable
     */
    public <A> A sum(A argument) {
        return Lambda.sum(getInner(), argument);
    }

    /**
     * Selects the item in this iterable having the lowest value on the given argument defined using the on method.
     * @param argument An argument defined using the {@link Lambda#on(Class)} method
     * @return The item in the given iterable with the minimum value on the given argument
     */
    public <A> T selectMin(A argument) {
        return (T)Lambda.selectMin(getInner(), argument);
    }

    /**
     * Selects the item in this iterable having the highest value on the given argument defined using the on method.
     * @param argument An argument defined using the {@link Lambda#on(Class)} method
     * @return The item in the given iterable with the maximum value on the given argument
     */
    public <A> T selectMax(A argument) {
        return (T)Lambda.selectMax(getInner(), argument);
    }

    /**
     * Joins all the object in this iterable by concatenating all their String representation.
     * It invokes toString() an all the objects and concatening them using the default separator ", ".
     * @return The concatenation of the String representation of all the objects in the given iterable or an empty String if the iterable is null or empty
     */
    public String join() {
        return Lambda.join(getInner());
    }

    /**
     * Joins all the object in this iterable by concatenating all their String representation.
     * It invokes toString() an all the objects and concatening them using the given separator.
     * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
     * @param separator The String used to separe the item's String representation
     * @return The concatenation of the String representation of all the objects in the given iterable or an empty String if the iterable is null or empty
     */
    public String join(String separator) {
        return Lambda.join(getInner(), separator);
    }

    /**
     * Transforms this iterable in a single object having the same methods of a single object in this iterable.
     * That allows to invoke a method on each T in the collection with a single strong typed method call.
     * The actual class of T is inferred from the class of the first iterable's item, but you can
     * specify a particular class by using the overloaded method.
     * @return An object that proxies all the item in the iterator or null if the iterator is null or empty
     */
    public T forEach() {
        return Lambda.forEach(innerIterator);
    }

    /**
     * Transforms the subset of objects in this iterable that match the given Mathcer
     * in a single object having the same methods of a single object in this iterable.
     * That allows to invoke a method on each T in the collection with a single strong typed method call.
     * The actual class of T is inferred from the class of the first iterable's item, but you can
     * specify a particular class by using the overloaded method.
     * @return An object that proxies all the item in the iterator or null if the iterator is null or empty
     */
    public T forEach(Matcher<?> matcher) {
        return Lambda.forEach((List<T>)Lambda.select(getInner(), matcher));
    }

    public <K> LambdaMap<K, T> map(Converter<T, K> converter) {
        return new LambdaMap<K, T>(Lambda.map(getInner(), converter));
    }

    /**
     * Indexes the objects in this iterable based on the value of their argument.
     * @param argument An argument defined using the {@link Lambda#on(Class)} method
     * @return A map having as keys the argument value extracted from the objects in the given iterable and as values the corresponding objects
     */
    public <A> LambdaMap<A, T> index(A argument) {
        return new LambdaMap<A, T>((Map<A, T>)Lambda.index(getInner(), argument));
    }

    @SuppressWarnings("unchecked")
    public <K> LambdaMap<K, T> map(K argument) {
        return new LambdaMap<K, T>((Map<K, T>) Lambda.index(getInner(), argument));
    }

    public boolean exists(Matcher<T> matcher) {
        return Lambda.selectFirst(getInner(), matcher) != null;
    }

    /**
     * Selects the first object in this iterable that matches the given hamcrest Matcher
     * @param matcher The hamcrest Matcher used to filter the given iterable
     * @return The first object in the given iterable that matches the given hamcrest Matcher or null if there is no such object
     */
    public T first(Matcher<?> matcher) {
        return (T)Lambda.selectFirst(getInner(), matcher);
    }

    /**
     * Selects the unique object in this iterable that matches the given hamcrest Matcher
     * @param matcher The hamcrest Matcher used to filter the given iterable
     * @return The only object in the given iterable that matches the given hamcrest Matcher or null if there is no such object
     * @throws RuntimeException if there is more than one object that matches the given hamcrest Matcher
     */
    public T unique(Matcher<?> matcher) {
        return (T)Lambda.selectUnique(getInner(), matcher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object o) {
        return getInner().equals(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        return getInner().hashCode();
    }
}
