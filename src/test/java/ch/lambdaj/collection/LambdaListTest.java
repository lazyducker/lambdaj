package ch.lambdaj.collection;

import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.function.aggregate.Money.money;
import static java.util.Arrays.asList;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;

import java.util.*;

import org.hamcrest.*;
import org.junit.Test;

import ch.lambdaj.function.aggregate.Money;
import ch.lambdaj.mock.Person;
import static ch.lambdaj.collection.LambdaCollections.with;

public class LambdaListTest {

	@Test
	public void test1() {
		List<Integer> result = with(asList(1, 2, 3, 4, 5)).filter(greaterThan(3));
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
		List<Integer> result = with(asList(1, 2, 3, 4, 5)).filter(Odd);
		assertNotNull(result);
		assertEquals(3, result.size());
	}

	Person me = new Person("Mario", "Fusco", 35);
	Person luca = new Person("Luca", "Marrocco", 29);
	Person biagio = new Person("Biagio", "Beatrice", 39);
	Person celestino = new Person("Celestino", "Bellone", 29);
	List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);

	@Test
	public void test3() {
		List<Person> oldFriends = with(meAndMyFriends).filter(having(on(Person.class).getAge(), greaterThan(30)));
		assertNotNull(oldFriends);
		assertEquals(2, oldFriends.size());
	}
/*
	@Test
	public void test4() {
		Premium endorsementPremium1 = premiumOf("1,311,314,330.89 GBP");
		Premium endorsementPremium2 = premiumOf("96,563,001.19 GBP");
		Premium endorsementPremium3 = premiumOf("4,443,725.16 GBP");

		List<Premium> premiums = asList(endorsementPremium1, endorsementPremium2, endorsementPremium3);

		Premium totalizer = with(premiums, Premium.class).aggregate(new MoneyAggregator());

		assertEquals(money("1,412,321,057.24 GBP").getValue(), totalizer.getPremium().getValue(), 0.000001);
	}
*/
	@Test
	public void testConcatNull1() {
		String result = with(asList(null, "a")).join();
		assertEquals(result, "a");
	}

	@Test
	public void testConcatNull2() {
		String result = with(Arrays.<String> asList(null, null)).join();
		assertEquals(result, "");
	}

	@Test
	public void testConcatNull3() {
		String result = with(asList("a", null, "b")).join("-");
		assertEquals(result, "a-b");
	}

	@Test
	public void testMap() {
		Map<String, Person> personsByName = with(meAndMyFriends).map(on(Person.class).getFirstName());

		assertNotNull(personsByName);
		assertEquals(4, personsByName.size());
	}

	@Test
	public void testMap1() {
		Map<Integer, Person> personsByAge = with(meAndMyFriends).map(on(Person.class).getAge());

		assertNotNull(personsByAge);
		assertEquals(3, personsByAge.size());
	}
	
	@Test
	public void testMap2() {
		Map<Integer, Person> personsByAge = with(meAndMyFriends).map(on(Person.class).getAge());

		assertNotNull(personsByAge);
		assertEquals(3, personsByAge.size());
	}
	
	private Premium premiumOf(final String value) {
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
	  // TODO Auto-generated method stub
	  return null;
  }

}
