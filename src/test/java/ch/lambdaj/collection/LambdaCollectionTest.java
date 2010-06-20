package ch.lambdaj.collection;

import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.function.aggregate.Money.money;
import static java.util.Arrays.asList;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.*;

import org.hamcrest.*;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

import ch.lambdaj.function.aggregate.Money;
import ch.lambdaj.function.convert.*;
import ch.lambdaj.mock.Person;
import static ch.lambdaj.collection.LambdaCollections.with;

public class LambdaCollectionTest {

    @Test
    public void test1() {
        Collection<Integer> numbers = asList(1, 2, 3, 4, 5);
        Collection<Integer> result = with(numbers).filter(greaterThan(3));
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    Matcher<Integer> Odd = new TypeSafeMatcher<Integer>() {
        public boolean matchesSafely(Integer item) {
            return item % 2 == 1;
        }

        public void describeTo(Description description) {
            description.appendText("odd()");
        }
    };

    @Test
    public void test2() {
        Collection<Integer> numbers = asList(1, 2, 3, 4, 5);
        LambdaCollection<Integer> result = with(numbers).filter(Odd).append(2, 4);
        assertNotNull(result);
        assertEquals(5, result.size());
    }

    Person me = new Person("Mario", "Fusco", 35);
    Person biagio = new Person("Biagio", "Beatrice", 39);
    Person celestino = new Person("Celestino", "Bellone", 29);
    Collection<Person> meAndMyFriends = asList(me, biagio, celestino);

    @Test
    public void test3() {
        Collection<Person> oldFriends = with(meAndMyFriends).filter(having(on(Person.class).getAge(), greaterThan(30)));
        assertNotNull(oldFriends);
        assertEquals(2, oldFriends.size());
    }
/*
    @Test
    public void test4() {
        Premium endorsementPremium1 = premiumOf("1,311,314,330.89 GBP");
        Premium endorsementPremium2 = premiumOf("96,563,001.19 GBP");
        Premium endorsementPremium3 = premiumOf("4,443,725.16 GBP");

        Collection<Premium> premiums = asList(endorsementPremium1, endorsementPremium2, endorsementPremium3);

        Premium totalizer = with(premiums, Premium.class).aggregate(new Money.MoneyAggregator());

        assertEquals(money("1,412,321,057.24 GBP").getValue(), totalizer.getPremium().getValue(), 0.000001);
    }

    @Test
    public void testAppend() {
        Premium p1 = premiumOf("1,311,314,330.89 GBP");
        Premium p2 = premiumOf("96,563,001.19 GBP");
        Premium p3 = premiumOf("4,443,725.16 GBP");

        List<Premium> totalizer = with(p1, p2, p3).append(asList(p1, p2)).append(p1, p2).filter(having(on(Premium.class).getPremium(), equalTo(money("96,563,001.19 GBP"))));

        assertNotNull(totalizer);
        assertEquals(3, totalizer.size());
    }
*/
    @Test
    public void testConcatNull1() {
        String result = with(meAndMyFriends).join();
        assertEquals(result, "Mario Fusco, Biagio Beatrice, Celestino Bellone");
    }

    @Test
    public void testMap() {
        Map<String, Person> personsByName = with(meAndMyFriends).map(on(Person.class).getFirstName());

        assertNotNull(personsByName);
        assertEquals(3, personsByName.size());
    }

    @Test
    public void testMap1() {
        Map<Integer, Person> personsByAge = with(meAndMyFriends).map(on(Person.class).getAge());

        assertNotNull(personsByAge);
        assertEquals(3, personsByAge.size());
    }

    @Test
    public void testMap2() {
        Map<Integer, Person> personsByAge = with(meAndMyFriends).map(on(Person.class).getAge()).convertValues(new MapConverter());

        assertNotNull(personsByAge);
        assertEquals(3, personsByAge.size());
    }

    private static class MapConverter implements Converter<Person, Person> {
        public Person convert(Person from) {
        System.out.println(from);
            return from;
    }

    }

    public static Premium premiumOf(final String value) {
        return new Premium() {
            public Money getPremium() {
                return money(value);
            }
        };
    }

    public interface Premium {
        Money getPremium();
    }

    public Person convert(Person from) {
      return null;
    }
}
