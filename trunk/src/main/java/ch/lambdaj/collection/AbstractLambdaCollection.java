package ch.lambdaj.collection;

import ch.lambdaj.function.aggregate.*;
import ch.lambdaj.function.convert.*;
import ch.lambdaj.*;

import java.util.*;

import org.hamcrest.*;

public class AbstractLambdaCollection<T> {

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

    public <T> T aggregate(Aggregator<T> aggregator) {
        return Lambda.aggregate(getInner(), aggregator);
    }

    public <T> T aggregate(T argument, Aggregator<T> aggregator) {
        return Lambda.aggregate(getInner(), aggregator, argument);
    }

    public <F> LambdaIterable<F> convert(Converter<T, F> converter) {
        return new LambdaIterable<F>(Lambda.convert(getInner(), converter), null);
    }

    public String join() {
        return Lambda.join(getInner());
    }

    public String join(String separator) {
        return Lambda.join(getInner(), separator);
    }

    public T foreach() {
        return Lambda.forEach(innerIterator);
    }

    public T foreach(Matcher<T> matcher) {
        return Lambda.forEach((List<T>)Lambda.select(getInner(), matcher));
    }

    public <K> LambdaMap<K, T> map(Converter<T, K> converter) {
        return new LambdaMap<K, T>(Lambda.map(getInner(), converter));
    }

    @SuppressWarnings("unchecked")
    public <K> LambdaMap<K, T> map(K argument) {
        return new LambdaMap<K, T>((Map<K, T>) Lambda.index(getInner(), argument));
    }

    public boolean exists(Matcher<T> matcher) {
        return Lambda.selectFirst(getInner(), matcher) != null;
    }

    public T first(Matcher<?> matcher) {
        return (T)Lambda.selectFirst(getInner(), matcher);
    }

    @Override
    public final boolean equals(Object o) {
        return getInner().equals(o);
    }

    @Override
    public final int hashCode() {
        return getInner().hashCode();
    }
}
