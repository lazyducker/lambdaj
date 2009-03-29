// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj;

import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.function.matcher.HasArgumentWithValue.*;
import static ch.lambdaj.function.matcher.HasNestedPropertyWithValue.*;
import static java.util.Arrays.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.*;

import org.hamcrest.*;
import org.junit.*;

import ch.lambdaj.function.convert.*;
import ch.lambdaj.mock.*;
import ch.lambdaj.mock.Person.*;

/**
 * @author Mario Fusco
 * @author Luca Marrocco
 */
public class LambdaTest {

	private Person me = new Person("Mario", "Fusco", 35);
	private Person luca = new Person("Luca", "Marrocco", 29);
	private Person biagio = new Person("Biagio", "Beatrice", 39);
	private Person celestino = new Person("Celestino", "Bellone", 29);

	@Test
	public void testForEach() {
		List<Person> personInFamily = asList(new Person("Domenico"), new Person("Mario"), new Person("Irma"));
		forEach(personInFamily).setLastName("Fusco");
		for (Person person : personInFamily) assertEquals("Fusco", person.getLastName());
	}
	
	@Test
	public void testIllegalForEach() {
		try {
			forEach(null);
			fail("forEach on null should throw an exception");
		} catch (Exception e) { }
		try {
			forEach(new LinkedList<Person>());
			fail("forEach on empty iterable should throw an exception");
		} catch (Exception e) { }
	}
	
	@Test
	public void testSelectPersonWith4LettersName() {
		List<Person> family = asList(new Person("Domenico"), new Person("Mario"), new Person("Irma"));
		Collection<Person> results = select(family, hasNestedProperty("firstName.length", equalTo(4)));
		assertThat(results.size(), is(equalTo(1)));
		assertThat(results.iterator().next().getFirstName(), is(equalTo("Irma")));
	}
	
	@Test
	public void testFilterPersonWith4LettersName() {
		List<Person> family = asList(new Person("Domenico"), new Person("Mario"), new Person("Irma"));
		Collection<Person> results = filter(hasNestedProperty("firstName.length", equalTo(4)), family);
		assertThat(results.size(), is(equalTo(1)));
		assertThat(results.iterator().next().getFirstName(), is(equalTo("Irma")));
	}
	
	@Test
	public void testSortOnAge() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		
		List<Person> sorted = sort(meAndMyFriends, on(Person.class).getAge());
		assertSame(luca, sorted.get(0));
		assertSame(celestino, sorted.get(1));
		assertSame(me, sorted.get(2));
		assertSame(biagio, sorted.get(3));
	}
	
	@Test
	public void testSortOnNameLenght() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);

		List<Person> sorted = sort(meAndMyFriends, on(Person.class).getFirstName(), new Comparator<String>() {
			public int compare(String s1, String s2) { return s1.length() - s2.length(); }
		});
		assertSame(luca, sorted.get(0));
		assertSame(me, sorted.get(1));
		assertSame(biagio, sorted.get(2));
		assertSame(celestino, sorted.get(3));
	}
	
	@Test
	public void testSelectWithHaving() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);

		Collection<Person> friends29aged = select(meAndMyFriends, having(on(Person.class).getAge(), is(equalTo(29))));
		assertEquals(2, friends29aged.size());
		Iterator<Person> friendsIterator = friends29aged.iterator();
		assertSame(luca, friendsIterator.next());
		assertSame(celestino, friendsIterator.next());
	}
	
	@Test
	public void testSelectDistinctAge() {
		Object meAndMyFriends = asList(me, luca, biagio, celestino);
		Collection<Person> distinctAgePersons = selectDistinct(meAndMyFriends, "age");
		assertEquals(3, distinctAgePersons.size());
	}
	
	@Test
	public void testSelectDistinctOnAge() {
		Object meAndMyFriends = asList(me, luca, biagio, celestino);
		Collection<Person> distinctAgePersons = selectDistinctArgument(meAndMyFriends, on(Person.class).getAge());
		assertEquals(3, distinctAgePersons.size());
	}
	
	@Test
	public void testArgumentIdentity() {
		assertTrue(on(Person.class).getAge() == on(Person.class).getAge());
		assertTrue(on(Person.class).isYoungerThan(30) == on(Person.class).isYoungerThan(30));
		assertFalse(on(Person.class).isYoungerThan(25) == on(Person.class).isYoungerThan(30));
		assertTrue(on(Person.class).getGender() == on(Person.class).getGender());
	}
	
	@Test
	public void testSelectOnBooleanWithHaving() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);

		List<Person> youngFriends = select(meAndMyFriends, having(on(Person.class).isYoungerThan(30)));
		assertEquals(2, youngFriends.size());
		assertSame(luca, youngFriends.get(0));
		assertSame(celestino, youngFriends.get(1));

		List<Person> youngestFriends = select(meAndMyFriends, having(on(Person.class).isYoungerThan(25)));
		assertTrue(youngestFriends.isEmpty());

		List<Person> notSoYoungFriends = select(meAndMyFriends, having(on(Person.class).isYoungerThan(40)));
		assertEquals(4, notSoYoungFriends.size());
	}
	
	@Test
	public void testSelectOnDateWithHaving() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);

		List<Person> youngFriends = select(meAndMyFriends, having(on(Person.class).getBirthDate(), greaterThan(new GregorianCalendar(1975, 0, 1).getTime())));
		
		assertEquals(2, youngFriends.size());
		assertSame(luca, youngFriends.get(0));
		assertSame(celestino, youngFriends.get(1));
	}
	
	@Test
	public void testSelectOnEnumMustFailWithHaving() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		
		List<Person> maleFriends = select(meAndMyFriends, having(on(Person.class).getGender(), equalTo(Gender.MALE)));
		assertEquals(4, maleFriends.size());
	}
	
	@Test
	public void testFilter() {
		List<Integer> biggerThan3 = filter(greaterThan(3), asList(1, 2, 3, 4, 5));
		assertEquals(2, biggerThan3.size());
		assertEquals(4, (int)biggerThan3.get(0));
		assertEquals(5, (int)biggerThan3.get(1));
	}
	
	@Test
	public void testFilterOnCustomMatcher() {
		Matcher<Integer> odd = new TypeSafeMatcher<Integer>() {
			@Override
			public boolean matchesSafely(Integer item) {
				return item % 2 == 1;
			}
			public void describeTo(Description description) {
				description.appendText("odd()");
			}
		};

		List<Integer> odds = filter(odd, asList(1, 2, 3, 4, 5));
		assertEquals(3, odds.size());
		assertEquals(1, (int)odds.get(0));
		assertEquals(3, (int)odds.get(1));
		assertEquals(5, (int)odds.get(2));
	}
	
	@Test
	public void testFilterWithHaving() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);

		Collection<Person> oldFriends = filter(having(on(Person.class).getAge(), greaterThan(30)), meAndMyFriends);
		assertEquals(2, oldFriends.size());
		Iterator<Person> friendsIterator = oldFriends.iterator();
		assertSame(me, friendsIterator.next());
		assertSame(biagio, friendsIterator.next());
	}
	
	@Test
	public void testIllegalSumFrom() {
		try {
			sumFrom(null);
			fail("forEach on null should throw an exception");
		} catch (Exception e) { }
		try {
			sumFrom(new LinkedList<Person>());
			fail("forEach on empty iterable should throw an exception");
		} catch (Exception e) { }
	}
	
	@Test
	public void testSumMinMaxFrom() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);

		int totalAge = sumFrom(meAndMyFriends).getAge();
		assertThat(totalAge, is(equalTo(35+29+39+29)));
		
		int minAge = minFrom(meAndMyFriends).getAge();
		assertThat(minAge, is(equalTo(29)));

		int maxAge = maxFrom(meAndMyFriends).getAge();
		assertThat(maxAge, is(equalTo(39)));
	}
	
	@Test
	public void testEmptySumMinMaxFrom() {
		assertThat(0, is(equalTo(sumFrom(new ArrayList<Person>(), Person.class).getAge())));
		assertThat(0, is(equalTo(minFrom(new ArrayList<Person>(), Person.class).getAge())));
		assertThat(0, is(equalTo(maxFrom(new ArrayList<Person>(), Person.class).getAge())));
	}
	
	@Test
	public void testPlainSumMinMaxFrom() {
		List<Integer> ints = asList(1, 2, 3, 4, 5);
		assertThat(15, is(equalTo(sum(ints))));
		assertThat(1, is(equalTo(min(ints))));
		assertThat(5, is(equalTo(max(ints))));
	}
	
	@Test
	public void testTypedSumMinMax() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		assertThat(sum(meAndMyFriends, on(Person.class).getAge()), is(equalTo(35+29+39+29)));
		assertThat(min(meAndMyFriends, on(Person.class).getAge()), is(equalTo(29)));
		assertThat(max(meAndMyFriends, on(Person.class).getAge()), is(equalTo(39)));
	}
	
	@Test
	public void testTypedSum2() {
		List<Person> myFriends = asList(luca, biagio, celestino);
		forEach(myFriends).setBestFriend(me);

		int totalBestFriendAge = sum(myFriends, on(Person.class).getBestFriend().getAge());
		assertThat(totalBestFriendAge, is(equalTo(35*3)));
	}
	
	@Test
	public void testTypedMixedSums() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		int onPersonAge = on(Person.class).getAge();
		
		List<Person> myFriends = asList(luca, biagio, celestino);
		forEach(myFriends).setBestFriend(me);
		int onBestFriendsAge = on(Person.class).getBestFriend().getAge();

		int totalAge = sum(meAndMyFriends, onPersonAge);
		assertThat(totalAge, is(equalTo(35+29+39+29)));

		int totalBestFriendAge = sum(myFriends, onBestFriendsAge);
		assertThat(totalBestFriendAge, is(equalTo(35*3)));

		int totalMyFriendsAge = sum(myFriends, onPersonAge);
		assertThat(totalMyFriendsAge, is(equalTo(29+39+29)));
	}

	@Test
	public void testCollectAges() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		
		List<Integer> ages = collect(meAndMyFriends, on(Person.class).getAge());
		assertThat(ages.get(0), is(equalTo(35)));
		assertThat(ages.get(1), is(equalTo(29)));
		assertThat(ages.get(2), is(equalTo(39)));
		assertThat(ages.get(3), is(equalTo(29)));
	}
	
	@Test
	public void testInvalidCollect() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		try {
			collect(meAndMyFriends, 29);
			fail("collect on non valid argument must fail");
		} catch (Exception e) { }
		try {
			collect(me, on(Person.class).getAge());
			fail("collect on non iterable object must fail");
		} catch (Exception e) { }
	}
	
	@Test
	public void testSelectUnique() {
		Object meAndMyFriends = asList(me, luca, biagio, celestino);
		Person person34Aged = selectUnique(meAndMyFriends, having(on(Person.class).getAge(), equalTo(34)));
		assertNull(person34Aged);
		
		Person person35Aged = selectUnique(meAndMyFriends, having(on(Person.class).getAge(), equalTo(35)));
		assertSame(me, person35Aged);
		
		try {
			selectUnique(meAndMyFriends, having(on(Person.class).getAge(), equalTo(29)));
			fail("Should throw a RuntimeException since there are two 29 years old persons");
		} catch (Exception e) { }
	}

	@Test
	public void testSelectFirst() {
		Object meAndMyFriends = asList(me, luca, biagio, celestino);

		Person person34Aged = selectFirst(meAndMyFriends, having(on(Person.class).getAge(), equalTo(34)));
		assertNull(person34Aged);
		
		Person person29Aged = selectFirst(meAndMyFriends, having(on(Person.class).getAge(), equalTo(29)));
		assertSame(luca, person29Aged);
	}
	
	@Test
	public void testSelectStringsThatEndsWithD() {
		List<String> strings = asList("first", "second", "third");
		Collection<String> results = select(strings, endsWith("d"));

		assertThat(results.size(), is(equalTo(2)));
		assertThat(results, hasItems("second", "third"));
	}

	@Test
	public void testSelectDistinct() {
		Object strings = asList("first", "second", "third", "first", "second");

		Collection<String> results = selectDistinct(strings);
		assertThat(results.size(), is(equalTo(3)));

		results = selectDistinct(strings, new Comparator<String>() {
			public int compare(String s1, String s2) {
				return s1.length() - s2.length();
			}
		});
		assertThat(results.size(), is(equalTo(2)));
	}

	@Test
	public void testJoin() {
		List<String> strings = asList("first", "second", "third");
		String result = join(strings);
		assertThat(result, is(equalTo("first, second, third")));
	}

	@Test
	public void testJoinFrom() {
		List<Exposure> exposures = asList(new Exposure("france", "first"), new Exposure("brazil", "second"));
		String result = joinFrom(exposures).getCountryName();
		assertThat(result, is(equalTo("france, brazil")));
	}

	@Test
	public void testEmptyJoinFrom() {
		assertEquals("", joinFrom(new ArrayList<Person>(), Person.class).getLastName());
		assertEquals("", joinFrom(new ArrayList<Person>(), Person.class, " - ").getLastName());
	}
	
	@Test
	public void testSelectFranceExposures() {
		Exposure franceExposure = new Exposure("france", "first");
		Exposure brazilExposure = new Exposure("brazil", "second");
		Collection<Exposure> exposures = asList(franceExposure, brazilExposure);
		Collection<Exposure> result = select(exposures, hasProperty("countryName", is(equalTo("france"))));

		assertThat(result.size(), is(equalTo(1)));
		assertThat(result, hasItem(franceExposure));
	}

	@Test
	public void testConcatFromConcreteClass() {
		List<Text> strings = new ArrayList<Text>();
		strings.add(new Text("first"));
		strings.add(new Text("second"));
		strings.add(new Text("third"));

		String result = join(forEach(strings).subString(1, 3));
		assertThat(result, is(equalTo("ir, ec, hi")));
	}

	@Test
	public void testConcatUsingMockedString() {
		Text aText = new Text("a text");
		Text anotherText = new Text("another text");
		List<Text> strings = asList(aText, anotherText);

		assertThat(joinFrom(strings, "; ").toString(), is(equalTo("a text; another text")));
	}

	@Test
	public void testJoinStrings() {
		assertThat(join(forEach(asList("many", "strings"))), is(equalTo("many, strings")));
		assertThat(join(asList("many", "strings")), is(equalTo("many, strings")));
		assertThat(join(new ArrayList<String>()), is(equalTo("")));
		assertThat(join(null), is(equalTo("")));
		assertThat(join(""), is(equalTo("")));
		assertThat(join(1), is(equalTo("1")));
		assertThat(join(1l), is(equalTo("1")));
		assertThat(join(1f), is(equalTo("1.0")));
		assertThat(join(1d), is(equalTo("1.0")));
	}

	@Test
	public void testJoinEmptyStringWithSeparatorAlwaysProduceEmptyString() {
		assertThat(join("", ";"), is(equalTo("")));
		assertThat(join("", ","), is(equalTo("")));
		assertThat(join("", "%"), is(equalTo("")));
		assertThat(join("", ":"), is(equalTo("")));
		assertThat(join("", "$"), is(equalTo("")));
		assertThat(join("", "."), is(equalTo("")));
	}
	
	@Test
	public void testExtract() {
		List<Exposure> exposures = asList(new Exposure("france", "first"), new Exposure("brazil", "second"));
		Collection<String> countries = extract(exposures, on(Exposure.class).getCountryName());
		assertThat(countries, hasItem("france"));
		assertThat(countries, hasItem("brazil"));
	}

	@Test
	public void testExtractString() {
		List<Exposure> exposures = asList(new Exposure("france", "first"), new Exposure("brazil", "second"));
		Collection<String> countries = extractString(exposures);
		assertThat(countries, hasItem("france, first"));
		assertThat(countries, hasItem("brazil, second"));
	}

	@Test
	public void testExtractProperty() {
		List<Exposure> exposures = asList(new Exposure("france", "first"), new Exposure("brazil", "second"));
		Collection<String> countries = extractProperty(exposures, "countryName");
		assertThat(countries, hasItem("france"));
		assertThat(countries, hasItem("brazil"));
	}
	
	@Test
	public void testConvert() {
		List<String> strings = asList("first", "second", "third", "four");
		Collection<Integer> lengths = convert(strings, new StringLengthConverter());
		int i = 0;
		for (int length : lengths) assertEquals(strings.get(i++).length(), length);
	}

	@Test
	public void testStringPropertyExtractor() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		List<String> agesAsString = convert(meAndMyFriends, new StringPropertyExtractor("age"));
		for (int i = 0; i < meAndMyFriends.size(); i++)
			assertEquals(agesAsString.get(i), String.valueOf(meAndMyFriends.get(i).getAge()));
	}
	
	@Test
	public void testNestedStringPropertyExtractor() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		List<String> agesAsString = convert(meAndMyFriends, new StringPropertyExtractor("bestFriend.age"));
		for (int i = 0; i < meAndMyFriends.size(); i++) {
			Person bestFriend = meAndMyFriends.get(i).getBestFriend();
			if (bestFriend == null) assertEquals(agesAsString.get(i), "");
			else assertEquals(agesAsString.get(i), String.valueOf(bestFriend.getAge()));
		}
	}
	
	@Test
	public void testIndex() {
		Exposure frenchExposure = new Exposure("france", "first");
		Exposure brazilianExposure = new Exposure("brazil", "second");
		List<Exposure> exposures = asList(frenchExposure, brazilianExposure);
		Map<String, Exposure> indexed = index(exposures, on(Exposure.class).getCountryName());
		assertSame(frenchExposure, indexed.get("france"));
		assertSame(brazilianExposure, indexed.get("brazil"));
	}
}