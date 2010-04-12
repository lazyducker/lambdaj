package ch.lambdaj.collection;

import static ch.lambdaj.util.iterator.IteratorFactory.discoverGenericType;
import static ch.lambdaj.Lambda.*;

import ch.lambdaj.function.aggregate.*;
import ch.lambdaj.function.convert.*;
import ch.lambdaj.*;

import java.util.*;

import org.hamcrest.*;

/**
 * @author Gianfranco Tognana
 * @author Mario Fusco
 */
public class LambdaIterable<T> implements Iterable<T> {

    protected Iterable<T> inner;
    protected Class<T> type;

    protected LambdaIterable(Iterable<? extends T> inner, Class<T> type) {
        this.inner = (Iterable<T>)inner;
        this.type = type;
    }

    protected Class<T> getType() {
        if (type == null) type = (Class<T>)discoverGenericType(inner);
        return type;
    }

    public Iterator<T> iterator() {
        return inner.iterator();
    }

    public boolean equals(Object o) {
        return inner.equals(o);
    }

    public int hashCode() {
        return inner.hashCode();
    }

    public T aggregate(Aggregator<T> aggregator) {
        return aggregateFrom(inner, getType(), aggregator);
    }

    public <T> T aggregate(T argument, Aggregator<T> aggregator) {
        return Lambda.aggregate(inner, aggregator, argument);
    }

    public <T> LambdaList<T> convert(Converter<T, T> converter) {
        return new LambdaList<T>(Lambda.convert(inner, converter), null);
    }

    public String join() {
        return Lambda.join(inner);
    }

    public String join(String separator) {
        return Lambda.join(inner, separator);
    }

    public LambdaIterable<T> distinct(Matcher<T>... matcher) {
        inner = new LinkedList<T>(selectDistinct(inner));
        return this;
    }

    public boolean exists(Matcher<T> matcher) {
        return Lambda.filter(matcher, inner) != null;
    }

    public LambdaIterable<T> filter(Matcher<?> matcher) {
        inner = Lambda.filter(matcher, inner);
        return this;
    }

    public T first(Matcher<T> matcher) {
        return (T)selectFirst(inner, matcher);
    }

    public T foreach() {
        return forEach(inner);
    }

    public T foreach(Matcher<T> matcher) {
        return forEach(select(inner, matcher));
    }

    @SuppressWarnings("unchecked")
    public <A> Map<A, T> map(A argument) {
        return new HashMap<A, T>((Map<A, T>) index(inner, argument));
    }
}
