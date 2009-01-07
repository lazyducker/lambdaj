package ch.lambdaj;

import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.Matchers.*;
import static java.util.Arrays.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import ch.lambdaj.function.aggregate.*;
import ch.lambdaj.function.convert.*;
import ch.lambdaj.mock.*;

/**
 * @author Mario Fusco
 * @author Luca Marrocco
 */
public class LambdaTest {

	@Test
	public void testSelect() {
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

		CharSequence result = selectUnique(from(strings).subSequence(0, 1), equalTo("t"));

		assertThat(result, is(equalTo((CharSequence) "t")));
	}

	@Test
	public void testSelectFirst() {
		List<CharSequence> strings = new ArrayList<CharSequence>();
		strings.add("first");
		strings.add("first");

		CharSequence result = selectFirst(from(strings).subSequence(0, 5), equalTo("first"));

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
	public void testAggregate() {
		List<String> strings = asList("first", "second", "third");

		int totalLenght = aggregate(convert(strings, new StringLengthConverter()), new Summer()).intValue();
		
		assertThat(totalLenght, is(equalTo(16)));
	}

	@Test
	public void testSum() {
		List<String> strings = asList("first", "second", "third");

		int totalLenght = sum(extract(strings, "length")).intValue();
		
		assertThat(totalLenght, is(equalTo(16)));
	}

	@Test
	public void testTypedSum() {
		List<CharSequence> strings = new ArrayList<CharSequence>();
		strings.add("first");
		strings.add("second");
		strings.add("third");

		int totalLenght = sumFrom(strings).length();

		assertThat(totalLenght, is(equalTo(16)));
	}

	@Test
	public void testLength() {
		List<String> strings = asList("first", "second", "third");

		Collection<String> results = select(strings, where("length", equalTo(5)));

		assertThat(results.size(), is(equalTo(2)));
		assertThat(results, hasItems("first", "third"));
	}

	@Test
	public void testConcat() {
		List<String> strings = asList("first", "second", "third");
		String result = join(from(strings));
		assertThat(result, is(equalTo("first, second, third")));
	}

	@Test
	public void testConvertAndConcat() {
		List<String> strings = asList("first", "second", "third");
		String result = join(convert(strings, new Converter<String, String>() {
			public String convert(String from) {
				return from.substring(0, 2);
			}
		}));
		assertThat(result, is(equalTo("fi, se, th")));
	}
	
	@Test
	public void testConcatFrom() {
		List<Exposure> exposures = asList(new Exposure("france", "first"), new Exposure("brazil", "second"));
		String result = joinFrom(exposures).getCountryName();
		assertThat(result, is(equalTo("france, brazil")));
	}

	@Test
	public void testSumFromConcreteClass() {
		List<Text> strings = new ArrayList<Text>();
		strings.add(new Text("first"));
		strings.add(new Text("second"));
		strings.add(new Text("third"));
		int totalLenght = sum(extract(strings, "length")).intValue();

		assertThat(totalLenght, is(equalTo(16)));
	}

	@Test
	public void testConcatFromConcreteClass() {
		List<Text> strings = new ArrayList<Text>();
		strings.add(new Text("first"));
		strings.add(new Text("second"));
		strings.add(new Text("third"));

		String result = join(from(strings).subString(1, 3));
		assertThat(result, is(equalTo("ir, ec, hi")));
	}

	@Test
	public void testMinMaxUsingMockedComparable() {
		Comparable lesser = new Long(1);
		Comparable greater = new Long(2);
		List comparables = asList(lesser, greater);

		assertThat((Comparable) min(from(comparables)), is(equalTo(lesser)));
		assertThat((Comparable) max(from(comparables)), is(equalTo(greater)));
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
		assertThat(join(from(asList("many", "strings"))), is(equalTo("many, strings")));
		assertThat(join(asList("many", "strings")), is(equalTo("many, strings")));
	}
}