package ch.lambdaj.collection;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import ch.lambdaj.mock.*;
import static ch.lambdaj.function.matcher.HasNestedPropertyWithValue.hasNestedProperty;
import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.collection.LambdaCollections.with;

import java.util.*;
import java.lang.reflect.*;

public class LambdaCollectionsTest {

    private final Person me = new Person("Mario", "Fusco", 35);
    private final Person luca = new Person("Luca", "Marrocco", 29);
    private final Person biagio = new Person("Biagio", "Beatrice", 39);
    private final Person celestino = new Person("Celestino", "Bellone", 29);

    @Test
    public void testForEach() {
        LambdaList<Person> personInFamily = with(new Person("Domenico"), new Person("Mario"), new Person("Irma"));
        personInFamily.forEach().setLastName("Fusco");
        for (Person person : personInFamily) assertEquals("Fusco", person.getLastName());
    }

    @Test
    public void testForEachForProxy() {
        IPerson dad = (IPerson) Proxy.newProxyInstance(Person.class.getClassLoader(), new Class<?>[]{ IPerson.class }, new PersonProxy("Domenico"));
        IPerson me = (IPerson)Proxy.newProxyInstance(Person.class.getClassLoader(), new Class<?>[]{ IPerson.class }, new PersonProxy("Mario"));
        IPerson sister = (IPerson)Proxy.newProxyInstance(Person.class.getClassLoader(), new Class<?>[]{ IPerson.class }, new PersonProxy("Irma"));

        LambdaList<IPerson> personInFamily = with(dad, me, sister);
        personInFamily.forEach().setLastName("Fusco");
        for (IPerson person : personInFamily) assertEquals("Di Fusco", person.getLastName());
    }

    private static final class PersonProxy implements InvocationHandler {

        private final IPerson person;

        private PersonProxy(String firstName) {
            this(new Person(firstName));
        }

        private PersonProxy(IPerson person) {
            this.person = person;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("setLastName")) {
                String lastName = (String)args[0];
                person.setLastName("Di " + lastName);
                return null;
            }
            return method.invoke(person, args);
        }
    }

    public static class NoEmptyConstructorPerson extends Person {
        public NoEmptyConstructorPerson(String firstName) {
            super(firstName);
        }
    }

    @Test
    public void testSelectPersonWith4LettersName() {
        LambdaList<Person> family = with(new Person("Domenico"), new Person("Mario"), new Person("Irma"));
        Collection<Person> results = family.filter(hasNestedProperty("firstName.length", equalTo(4)));
        assertThat(results.size(), is(equalTo(1)));
        assertThat(results.iterator().next().getFirstName(), is(equalTo("Irma")));
    }

    @Test
    public void testSortOnAge() {
        LambdaList<Person> meAndMyFriends = with(me, luca, biagio, celestino);

        List<Person> sorted = meAndMyFriends.sort(on(Person.class).getAge());
        assertSame(luca, sorted.get(0));
        assertSame(celestino, sorted.get(1));
        assertSame(me, sorted.get(2));
        assertSame(biagio, sorted.get(3));
    }

    @Test
    public void testFindOldest() {
        LambdaList<Person> meAndMyFriends = with(me, luca, biagio, celestino);

        Person oldest = meAndMyFriends.selectMax(on(Person.class).getAge());
        assertSame(biagio, oldest);

        Person alphabeticalFirst = meAndMyFriends.selectMin(on(Person.class).getFirstName());
        assertSame(biagio, alphabeticalFirst);
    }

    @Test
    public void testSelectWithHaving() {
        LambdaList<Person> meAndMyFriends = with(me, luca, biagio, celestino);

        Collection<Person> friends29aged = meAndMyFriends.filter(having(on(Person.class).getAge(), is(equalTo(29))));
        assertEquals(2, friends29aged.size());
        Iterator<Person> friendsIterator = friends29aged.iterator();
        assertSame(luca, friendsIterator.next());
        assertSame(celestino, friendsIterator.next());
    }

/*
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

        Collection<Integer> distinctAges = selectDistinct(extract(meAndMyFriends, on(Person.class).getAge()));
        assertEquals(3, distinctAges.size());
        assertTrue(distinctAges.contains(me.getAge()) && distinctAges.contains(biagio.getAge()) && distinctAges.contains(luca.getAge()));
    }

    @Test
    public void testProject() {
        List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
        List<Map<String, Object>> projection = project(meAndMyFriends, as("name", on(Person.class).getFirstName()), as(on(Person.class).getAge()));

        assertEquals(meAndMyFriends.size(), projection.size());
        for (int i = 0; i < meAndMyFriends.size(); i++) {
            assertEquals(meAndMyFriends.get(i).getFirstName(), projection.get(i).get("name"));
            assertEquals(meAndMyFriends.get(i).getAge(), projection.get(i).get("age"));
        }
    }

    @Test
    public void testProjectDto() {
        List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
        List<PersonDto> meAndMyFriendsDto = project(meAndMyFriends, PersonDto.class, on(Person.class).getFirstName(), on(Person.class).getAge());

        assertEquals(meAndMyFriends.size(), meAndMyFriendsDto.size());
        for (int i = 0; i < meAndMyFriends.size(); i++) {
            assertEquals(meAndMyFriends.get(i).getFirstName(), meAndMyFriendsDto.get(i).getName());
            assertEquals(meAndMyFriends.get(i).getAge(), meAndMyFriendsDto.get(i).getAge());
        }
    }
*/
    @Test
    public void testTypedSumMinMax() {
        LambdaList<Person> meAndMyFriends = with(me, luca, biagio, celestino);
        assertThat(meAndMyFriends.sum(on(Person.class).getAge()), is(equalTo(35+29+39+29)));
        assertThat(meAndMyFriends.min(on(Person.class).getAge()), is(equalTo(29)));
        assertThat(meAndMyFriends.max(on(Person.class).getAge()), is(equalTo(39)));
    }

    @Test
    public void testTypedSum2() {
        LambdaList<Person> myFriends = with(luca, biagio, celestino);
        myFriends.forEach().setBestFriend(me);

        int totalBestFriendAge = myFriends.sum(on(Person.class).getBestFriend().getAge());
        assertThat(totalBestFriendAge, is(equalTo(35*3)));
    }
    @Test
    public void testExtractAges() {
        LambdaList<Person> meAndMyFriends = with(me, luca, biagio, celestino);

        List<Integer> ages = meAndMyFriends.extract(on(Person.class).getAge());
        assertThat(ages.get(0), is(equalTo(35)));
        assertThat(ages.get(1), is(equalTo(29)));
        assertThat(ages.get(2), is(equalTo(39)));
        assertThat(ages.get(3), is(equalTo(29)));
    }

    @Test
    public void testSelectFirst() {
        LambdaList<Person> meAndMyFriends = with(me, luca, biagio, celestino);

        Person person34Aged = meAndMyFriends.first(having(on(Person.class).getAge(), equalTo(34)));
        assertNull(person34Aged);

        Person person29Aged = meAndMyFriends.first(having(on(Person.class).getAge(), equalTo(29)));
        assertSame(luca, person29Aged);
    }

    @Test
    public void testSelectUnique() {
        LambdaList<Person> meAndMyFriends = with(me, luca, biagio, celestino);

        Person person34Aged = meAndMyFriends.unique(having(on(Person.class).getAge(), equalTo(34)));
        assertNull(person34Aged);

        Person person35Aged = meAndMyFriends.unique(having(on(Person.class).getAge(), equalTo(35)));
        assertSame(me, person35Aged);

        try {
            meAndMyFriends.unique(having(on(Person.class).getAge(), equalTo(29)));
            fail("unique must fail if there is more than one object that matches");            
        } catch (Exception e) { }
    }

    @Test
    public void testSelectStringsThatEndsWithD() {
        LambdaList<String> strings = with("first", "second", "third");
        Collection<String> results = strings.filter(endsWith("d"));

        assertThat(results.size(), is(equalTo(2)));
        assertThat(results, hasItems("second", "third"));
    }

/*
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
*/

    @Test
    public void testJoin() {
        LambdaList<String> strings = with("first", "second", "third");
        String result = strings.join();
        assertThat(result, is(equalTo("first, second, third")));
    }

    @Test
    public void testJoinFrom() {
        LambdaList<Exposure> exposures = with(new Exposure("france", "first"), new Exposure("brazil", "second"));

        String result = exposures.joinFrom().getCountryName();
        assertThat(result, is(equalTo("france, brazil")));

        result = exposures.joinFrom(" - ").getCountryName();
        assertThat(result, is(equalTo("france - brazil")));
    }

    @Test
    public void testExtract() {
        LambdaList<Exposure> exposures = with(new Exposure("france", "first"), new Exposure("brazil", "second"));
        Collection<String> countries = exposures.extract(on(Exposure.class).getCountryName());
        assertThat(countries, hasItem("france"));
        assertThat(countries, hasItem("brazil"));
    }

/*
    @Test
    public void testConvert() {
        List<String> strings = asList("first", "second", "third", "four");
        Collection<Integer> lengths = convert(strings, new StringLengthConverter());
        int i = 0;
        for (int length : lengths) assertEquals(strings.get(i++).length(), length);
    }

    @Test
    public void testConvertMap() {
        List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
        Map<String, Person> map = index(meAndMyFriends, on(Person.class).getFirstName());
        Map<String, String> convertedMap = convertMap(map, on(Person.class).getLastName());
        assertEquals("Fusco", convertedMap.get("Mario"));
    }
*/
    @Test
    public void testIndex() {
        Exposure frenchExposure = new Exposure("france", "first");
        Exposure brazilianExposure = new Exposure("brazil", "second");
        LambdaList<Exposure> exposures = with(frenchExposure, brazilianExposure);
        Map<String, Exposure> indexed = exposures.index(on(Exposure.class).getCountryName());
        assertSame(frenchExposure, indexed.get("france"));
        assertSame(brazilianExposure, indexed.get("brazil"));
    }
}
