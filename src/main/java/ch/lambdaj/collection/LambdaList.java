package ch.lambdaj.collection;

import org.hamcrest.*;

import java.util.*;

import ch.lambdaj.*;

/**
 * @author Gianfranco Tognana
 * @author Mario Fusco
 */
public class LambdaList<T> extends LambdaCollection<T> implements List<T> {

	public static <T> LambdaList<T> with(List<? extends T> inner) {
		return new LambdaList<T>(inner, null);
	}

	public static <T> LambdaList<T> with(List<? extends T> inner, Class<T> type) {
		return new LambdaList<T>(inner, type);
	}

	LambdaList(List<? extends T> inner, Class<T> type) {
        super(inner, type);
	}

    private List<T> innerList() {
        return (List<T>)inner;
    }

    public void add(int index, T element) {
        innerList().add(index, element);
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        return innerList().addAll(index, c);
    }

    public T get(int index) {
        return innerList().get(index);
    }

    public int indexOf(Object o) {
        return innerList().indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return innerList().lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        return innerList().listIterator();
    }

    public ListIterator<T> listIterator(int index) {
        return innerList().listIterator(index);
    }

    public T remove(int index) {
        return innerList().remove(index);
    }

    public T set(int index, T element) {
        return innerList().set(index, element);
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return innerList().subList(fromIndex, toIndex);
    }

    public LambdaList<T> filter(Matcher<?> matcher) {
        return (LambdaList<T>)super.filter(matcher);
    }

}
