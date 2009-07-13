package ch.lambdaj;

import static ch.lambdaj.Lambda.*;
import static java.util.Arrays.*;
import static org.junit.Assert.*;

import java.io.*;

import org.junit.*;

import ch.lambdaj.function.closure.*;

/**
 * @author Mario Fusco
 */
public class ClosureTest {
	
	@Test
	public void testSystemOut() {
		Closure1<String> println = closure(String.class); { of(System.out).println((String)null); } 
		println.each("mickey mouse", "donald duck", "uncle scrooge");
	}
	
	@Test
	public void testOnList() {
		StringWriter sw = new StringWriter();
		Closure writer = closure(); { of(sw).write((String)null); } 
		writer.each(asList("first", "second", "third"));
		assertEquals("firstsecondthird", sw.toString());
	}

	@Test
	public void testOnArray() {
		StringWriter sw = new StringWriter();
		Closure writer = closure(); { of(sw).append((String)null); } 
		writer.each("first", "second", "third");
		assertEquals("firstsecondthird", sw.toString());
		
		writer.apply("forth");
		assertEquals("firstsecondthirdforth", sw.toString());
		
		try {
			writer.apply(3);
			fail("An invocation with the wrong parameter type must fail");
		} catch (WrongClosureInvocationException wcie) { }
		
		try {
			writer.apply("fifth", "sixth");
			fail("An invocation with the wrong parameter number must fail");
		} catch (WrongClosureInvocationException wcie) { }
	}

	@Test
	public void testTypedOnList() {
		StringWriter sw = new StringWriter();
		Closure1<String> writer = closure(String.class); { of(sw).append((String)null); } 
		writer.each(asList("first", "second", "third"));
		assertEquals("firstsecondthird", sw.toString());
		writer.apply("forth");
		assertEquals("firstsecondthirdforth", sw.toString());
	}

	@Test
	public void testTypedOnArray() {
		StringBuilder sb = new StringBuilder();
		Closure1<String> appender = closure(String.class); { 
			try {
				of(sb, Appendable.class).append((String)null);
			} catch (IOException e) { } 
		} 
		appender.each("first", "second", "third");
		assertEquals("firstsecondthird", sb.toString());
	}
	
	public Integer addOnInteger(Integer val1, Integer val2) {
		return val1 + val2;
	}

	@Test
	public void testAddOnInteger() {
		Closure2<Integer, Integer> adder = closure(Integer.class, Integer.class); { 
			of(this).addOnInteger((Integer)null, (Integer)null); 
		} 
		int sum = (Integer)adder.apply(2, 3);
		assertEquals(2 + 3, sum);
	}

	public int addOnInt(int val1, int val2, int val3) {
		return val1 + val2 + val3;
	}

	@Test
	public void testAdd3OnInt() {
		Closure3<Integer, Integer, Integer> adder = closure(Integer.class, Integer.class, Integer.class); { 
			of(this).addOnInt(0, 0, 0); 
		} 
		int sum = (Integer)adder.apply(2, 3, 4);
		assertEquals(2 + 3 + 4, sum);
	}

	@Test
	public void testAdd2OnInt() {
		Closure2<Integer, Integer> adder = closure(Integer.class, Integer.class); { 
			of(this).addOnInt(0, 0, 4); 
		} 
		int sum = (Integer)adder.apply(2, 3);
		assertEquals(2 + 3 + 4, sum);
	}
}
