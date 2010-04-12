package ch.lambdaj.collection;

import java.util.*;

import org.hamcrest.*;

/**
 * @author Gianfranco Tognana
 * @author Mario Fusco
 */
public class LambdaCollection<T> extends LambdaIterable<T> implements Collection<T> {

    protected LambdaCollection(Collection<? extends T> inner, Class<T> type) {
        super(inner, type);
    }

    private Collection<T> innerCollection() {
        return (Collection<T>)inner;
    }

    public LambdaCollection<T> remove(Matcher<T> matcher) {
        // TODO
        return this;
    }

    public boolean add(T e) {
        return innerCollection().add(e);
    }

    public boolean addAll(Collection<? extends T> c) {
        return innerCollection().addAll(c);
    }

    public void clear() {
        innerCollection().clear();
    }

    public boolean contains(Object o) {
        return innerCollection().contains(o);
    }

    public boolean containsAll(Collection<?> c) {
        return innerCollection().containsAll(c);
    }

    public boolean isEmpty() {
        return innerCollection().isEmpty();
    }

    public boolean remove(Object o) {
        return innerCollection().remove(o);
    }

    public boolean removeAll(Collection<?> c) {
        return innerCollection().removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return innerCollection().retainAll(c);
    }

    public int size() {
        return innerCollection().size();
    }

    public Object[] toArray() {
        return innerCollection().toArray();
    }

    public <T> T[] toArray(T[] a) {
        return innerCollection().toArray(a);
    }
}
