package ch.lambdaj.relational;

import java.util.Arrays;

import static ch.lambdaj.Lambda.*;
import ch.lambdaj.function.aggregate.*;

public final class Relational {
    //-- (Union) ---------------------------------------------------------------
    /**
     * Unions two iterables together
     * @param first The first iterable to be unioned
     * @param second The second iterable to be unioned
     * @return an iterable with all elements of first followed by all elements of second
     */
    public static <T> Iterable<T> union(final Iterable<T> first, final Iterable<T> second) {
        return aggregate(Arrays.asList(first, second), new Union<T>());
    }

    /**
     * Unions an iterable of iterables together in the order of appearance
     * @param iterables The list of iterables
     * @return an iterable with all elements of the given list of iterables
     */
    public static <T> Iterable<T> union(final Iterable<? extends Iterable<T>> iterables) {
        return aggregate(iterables, new Union<T>());
    }
}
