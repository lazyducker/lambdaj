package ch.lambdaj.function.aggregate;

import java.util.*;

public class Union<T> implements Aggregator<Iterable<T>> {
    public Iterable<T> aggregate(final Iterable<T> first, final Iterable<T> second) {
        List<T> result = new ArrayList<T>();
        for(T item : first)
            result.add(item);
        for(T item : second)
            result.add(item);
        return result;
    }

    public Iterable<T> emptyItem() {
        return Collections.emptyList();
    }
}
