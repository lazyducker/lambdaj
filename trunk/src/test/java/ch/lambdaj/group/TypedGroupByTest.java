package ch.lambdaj.group;

import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.Strings.*;
import static ch.lambdaj.group.Groups.*;
import static java.util.Arrays.*;

import java.util.*;

import org.junit.*;

import ch.lambdaj.mock.*;

public class TypedGroupByTest {

	private Person me = new Person("Mario", "Fusco", 35);
	private Person luca = new Person("Luca", "Marrocco", 29);
	private Person biagio = new Person("Biagio", "Beatrice", 39);
	private Person celestino = new Person("Celestino", "Bellone", 29);

	@Test
	public void testGroupByAge() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		
		Group<Person> group = group(meAndMyFriends, by(on(Person.class).getAge()));
		String json = json(group);
		System.out.println(json);
	}
	
	@Test
	public void testGroupByAgeAsPersons() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		
		Group<Person> group = group(meAndMyFriends, by(on(Person.class).getAge()).as("persons"));
		String json = json(group);
		System.out.println(json);
	}

	@Test
	public void testGroupByAgeAsPersonsWithFirstName() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		
		Group<Person> group = group(meAndMyFriends, by(on(Person.class).getAge()).as("persons").head(on(Person.class).getFirstName()));
		String json = json(group);
		System.out.println(json);
	}
	
	@Test
	public void testGroupByAgeAsPersonsWithName() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		
		Group<Person> group = group(meAndMyFriends, by(on(Person.class).getAge()).as("persons").head(on(Person.class).getFirstName(), "name"));
		String json = json(group);
		System.out.println(json);
	}
}
