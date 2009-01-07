package ch.lambdaj;

import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.Matchers.*;
import static java.util.Arrays.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.*;

import org.jmock.*;
import org.junit.*;

import ch.lambdaj.function.aggregator.*;
import ch.lambdaj.function.converter.*;
import ch.lambdaj.mock.*;

public class LambdaTest {

	@Test
	public void testSelect() {
		List<String> strings = Arrays.asList("pippo", "pluto", "topolino");

		Collection<String> results = select(strings, startsWith("p"));

		assertEquals(results.size(), 2);
		Iterator<String> i = results.iterator();
		assertEquals(i.next(), "pippo");
		assertEquals(i.next(), "pluto");
	}

	@Test
	public void testSelectUnique() {
		List<CharSequence> strings = new ArrayList<CharSequence>();
		strings.add("pippo");
		strings.add("pluto");
		strings.add("topolino");

		CharSequence result = selectUnique(from(strings).subSequence(0, 1), equalTo("t"));

		assertEquals(result, "t");
	}

	@Test
	public void testSelectFirst() {
		List<CharSequence> strings = new ArrayList<CharSequence>();
		strings.add("pippo");
		strings.add("pippo");

		CharSequence result = selectFirst(from(strings).subSequence(0, 5), equalTo("pippo"));

		assertEquals(result, "pippo");
	}

	@Test
	public void testSelectDistinct() {
		List<String> strings = new ArrayList<String>();
		strings.add("pippo");
		strings.add("pluto");
		strings.add("topolino");
		strings.add("pippo");
		strings.add("pluto");

		Collection<String> results = selectDistinct(strings);
		assertEquals(3, results.size());

		results = selectDistinct(strings, new Comparator<String>() {
			public int compare(String s1, String s2) {
				return s1.length() - s2.length();
			}
		});
		assertEquals(2, results.size());
	}

	@Test
	public void testAggregate() {
		List<String> strings = Arrays.asList("pippo", "pluto", "topolino");

		int totalLenght = aggregate(convert(strings, new StringLengthConverter()), new Summer()).intValue();
		assertEquals(18, totalLenght);
	}

	@Test
	public void testSum() {
		List<String> strings = Arrays.asList("pippo", "pluto", "topolino");

		int totalLenght = sum(extract(strings, "length")).intValue();
		assertEquals(totalLenght, 18);
	}

	@Test
	public void testTypedSum() {
		List<CharSequence> strings = new ArrayList<CharSequence>();
		strings.add("pippo");
		strings.add("pluto");
		strings.add("topolino");

		int totalLenght = sumFrom(strings).length();
		assertEquals(totalLenght, 18);
	}

	@Test
	public void testLength() {
		List<String> strings = Arrays.asList("pippo", "pluto", "topolino");

		Collection<String> results = select(strings, where("length", equalTo(5)));

		assertEquals(results.size(), 2);
		Iterator<String> i = results.iterator();
		assertEquals(i.next(), "pippo");
		assertEquals(i.next(), "pluto");
	}

	@Test
	public void testConcat() {
		List<String> strings = Arrays.asList("pippo", "pluto", "topolino");
		String result = join(from(strings));
		assertEquals(result, "pippo, pluto, topolino");
	}

	@Test
	public void testConvertAndConcat() {
		List<String> strings = Arrays.asList("pippo", "pluto", "topolino");
		String result = join(convert(strings, new Converter<String, String>() {
			public String convert(String from) {
				return from.substring(0, 2);
			}
		}));
		assertEquals(result, "pi, pl, to");
	}
	
	@Test
	public void testConcatFrom() {
		List<Exposure> exposures = Arrays.asList(new Exposure("france", "pippo"), new Exposure("brazil", "pluto"));
		String result = joinFrom(exposures).getCountryName();
		assertEquals(result, "france, brazil");
	}
	
	public static class StringWrapper {
		private String string;

		public StringWrapper() {}

		public StringWrapper(String string) {
			this.string = string;
		}

		public int length() {
			return string.length();
		}

		public StringWrapper subString(int begin, int end) {
			return new StringWrapper(string.substring(begin, end));
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof StringWrapper ? string.equals(((StringWrapper) obj).string) : false;
		}

		@Override
		public int hashCode() {
			return string.hashCode();
		}

		@Override
		public String toString() {
			return string;
		}
	}

	@Test
	public void testSumFromConcreteClass() {
		List<StringWrapper> strings = new ArrayList<StringWrapper>();
		strings.add(new StringWrapper("pippo"));
		strings.add(new StringWrapper("pluto"));
		strings.add(new StringWrapper("topolino"));

		int totalLenght = sum(extract(strings, "length")).intValue();
		assertEquals(totalLenght, 18);
	}

	@Test
	public void testConcatFromConcreteClass() {
		List<StringWrapper> strings = new ArrayList<StringWrapper>();
		strings.add(new StringWrapper("pippo"));
		strings.add(new StringWrapper("pluto"));
		strings.add(new StringWrapper("topolino"));

		String result = join(from(strings).subString(1, 3));
		assertEquals(result, "ip, lu, op");
	}

	@Test
	public void testMinMaxUsingMockedComparable() {
		Mockery context = new Mockery();
		final Comparable lesser = context.mock(Comparable.class, "lesser");
		final Comparable greater = context.mock(Comparable.class, "greater");
		List comparables = asList(lesser, greater);

		context.checking(new Expectations() {
			{
				allowing(lesser).compareTo(with(any(Object.class)));
				will(returnValue(-1));
				allowing(greater).compareTo(with(any(Object.class)));
				will(returnValue(1));
			}
		});

		assertThat((Comparable) min(from(comparables)), is(equalTo(lesser)));
		assertThat((Comparable) max(from(comparables)), is(equalTo(greater)));
	}

	@Test
	public void testConcatUsingMockedString() {
		Mockery context = new Mockery();
		final ReturnAString aString = context.mock(ReturnAString.class, "aString");
		final ReturnAString anotherString = context.mock(ReturnAString.class, "anotherString");
		List<ReturnAString> strings = asList(aString, anotherString);

		context.checking(new Expectations() {
			{
				allowing(aString).aString();
				will(returnValue("aString"));
				allowing(anotherString).aString();
				will(returnValue("anotherString"));
			}
		});

		assertThat(joinFrom(strings, ":").aString(), is(equalTo("aString:anotherString")));
	}

	@Test
	public void testJoinStrings() {
		assertThat(join(from(asList("many", "strings"))), is(equalTo("many, strings")));
	}
}