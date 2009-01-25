// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj;

import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.function.argument.ArgumentsFactory.*;
import static ch.lambdaj.function.matcher.HasNestedPropertyWithValue.*;
import static ch.lambdaj.function.matcher.HasArgumentWithValue.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import ch.lambdaj.function.convert.*;
import ch.lambdaj.mock.*;

/**
 * @author Mario Fusco
 * @author Luca Marrocco
 */
public class LambdaTest {

	@Test
	public void testForEach() {
		List<Person> family = asList(new Person("Domenico"), new Person("Mario"), new Person("Irma"));
		forEach(family).setLastName("Fusco");
		for (Person person : family) assertEquals("Fusco", person.getLastName());
	}
	
	@Test
	public void testSelectPersonWith4LettersName() {
		List<Person> family = asList(new Person("Domenico"), new Person("Mario"), new Person("Irma"));
		Collection<Person> results = select(family, hasNestedProperty("firstName.length", equalTo(4)));
		assertThat(results.size(), is(equalTo(1)));
		assertThat(results.iterator().next().getFirstName(), is(equalTo("Irma")));
	}
	
	@Test
	public void testWhere() {
		Person me = new Person("Mario", "Fusco", 35);
		Person luca = new Person("Luca", "Marrocco", 29);
		Person biagio = new Person("Biagio", "Beatrice", 39);
		Person celestino = new Person("Celestino", "Bellone", 29);
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);

		Collection<Person> friends29aged = select(meAndMyFriends, where(on(Person.class).getAge(), is(equalTo(29))));
		assertEquals(2, friends29aged.size());
		Iterator<Person> friendsIterator = friends29aged.iterator();
		assertSame(luca, friendsIterator.next());
		assertSame(celestino, friendsIterator.next());
	}
	
	@Test
	public void testSumFrom() {
		Person me = new Person("Mario", "Fusco", 35);
		Person luca = new Person("Luca", "Marrocco", 29);
		Person biagio = new Person("Biagio", "Beatrice", 39);
		Person celestino = new Person("Celestino", "Bellone", 29);
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);

		int totalAge = sumFrom(meAndMyFriends).getAge();
		assertThat(totalAge, is(equalTo(35+29+39+29)));
	}
	
	@Test
	public void testTypedSum() {
		Person me = new Person("Mario", "Fusco", 35);
		Person luca = new Person("Luca", "Marrocco", 29);
		Person biagio = new Person("Biagio", "Beatrice", 39);
		Person celestino = new Person("Celestino", "Bellone", 29);
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);

		int totalAge = sum(meAndMyFriends, on(Person.class).getAge());
		assertThat(totalAge, is(equalTo(35+29+39+29)));
	}
	
	@Test
	public void testTypedSum2() {
		Person me = new Person("Mario", "Fusco", 35);
		Person luca = new Person("Luca", "Marrocco", 29);
		Person biagio = new Person("Biagio", "Beatrice", 39);
		Person celestino = new Person("Celestino", "Bellone", 29);
		
		List<Person> myFriends = asList(luca, biagio, celestino);
		forEach(myFriends).setBestFriend(me);

		int totalBestFriendAge = sum(myFriends, on(Person.class).getBestFriend().getAge());
		assertThat(totalBestFriendAge, is(equalTo(35*3)));
	}
	
	@Test
	public void testTypedMixedSums() {
		Person me = new Person("Mario", "Fusco", 35);
		Person luca = new Person("Luca", "Marrocco", 29);
		Person biagio = new Person("Biagio", "Beatrice", 39);
		Person celestino = new Person("Celestino", "Bellone", 29);
		
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
	public void testSelectStringsThatEndsWithD() {
		List<String> strings = asList("first", "second", "third");

		Collection<String> results = select(strings, endsWith("d"));

		assertThat(results.size(), is(equalTo(2)));
		assertThat(results, hasItems("second", "third"));
	}

	@Test
	public void testSelectUnique() {
		List<CharSequence> strings = new ArrayList<CharSequence>();

		strings.add("first");
		strings.add("second");
		strings.add("third");

		CharSequence result = selectUnique(forEach(strings).subSequence(0, 1), equalTo("t"));

		assertThat(result, is(equalTo((CharSequence) "t")));
	}

	@Test
	public void testSelectFirst() {
		List<CharSequence> strings = new ArrayList<CharSequence>();
		strings.add("first");
		strings.add("third");

		CharSequence result = selectFirst(forEach(strings).subSequence(0, 5), equalTo("first"));

		assertThat(result, is(equalTo((CharSequence) "first")));
	}

	@Test
	public void testSelectDistinct() {
		List<String> strings = new ArrayList<String>();
		strings.add("first");
		strings.add("second");
		strings.add("third");
		strings.add("first");
		strings.add("second");

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
	public void testMinMaxUsingMockedComparable() {
		Comparable lesser = new Long(1);
		Comparable greater = new Long(2);
		List comparables = asList(lesser, greater);

		assertThat((Comparable) min(forEach(comparables)), is(equalTo(lesser)));
		assertThat((Comparable) max(forEach(comparables)), is(equalTo(greater)));
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
		assertThat(join(emptyList()), is(equalTo("")));
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
		Collection<String> countries = extract(exposures, "countryName");
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
	public void testIndex() {
		Exposure frenchExposure = new Exposure("france", "first");
		Exposure brazilianExposure = new Exposure("brazil", "second");
		List<Exposure> exposures = asList(frenchExposure, brazilianExposure);
		Map<String, Exposure> indexed = index(exposures, "countryName");
		assertSame(frenchExposure, indexed.get("france"));
		assertSame(brazilianExposure, indexed.get("brazil"));
	}
}