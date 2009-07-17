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
	
	public Integer add(Integer val1, Integer val2) {
		return val1 + val2;
	}

	@Test
	public void testAddOnInteger() {
		Closure2<Integer, Integer> adder = closure(Integer.class, Integer.class); { 
			of(this).add((Integer)null, (Integer)null); 
		} 
		int sum = (Integer)adder.apply(2, 3);
		assertEquals(2 + 3, sum);
	}

	public int nonCommutativeDoOnInt(int val1, int val2, int val3) {
		return (val1 - val2) * val3;
	}

	@Test
	public void testDo3OnInt() {
		Closure3<Integer, Integer, Integer> adder = closure(Integer.class, Integer.class, Integer.class); { 
			of(this).nonCommutativeDoOnInt(0, 0, 0); 
		} 
		int result = (Integer)adder.apply(5, 2, 4);
		assertEquals((5 - 2) * 4, result);
	}

	@Test
	public void testDo2OnInt() {
		Closure2<Integer, Integer> adder = closure(Integer.class, Integer.class); { 
			of(this).nonCommutativeDoOnInt(0, 2, 0); 
		} 
		int result = (Integer)adder.apply(5, 4);
		assertEquals((5 - 2) * 4, result);
	}

	@Test
	public void testCurry() {
		Closure3<Integer, Integer, Integer> closure3 = closure(Integer.class, Integer.class, Integer.class); { 
			of(this).nonCommutativeDoOnInt(0, 0, 0); 
		} 
		int result = (Integer)closure3.apply(5, 2, 4);
		assertEquals((5 - 2) * 4, result);
		
		Closure2<Integer, Integer> closure2 = closure3.curry2(2);
		result = (Integer)closure2.apply(7, 3);
		assertEquals((7 - 2) * 3, result);
		
		Closure1<Integer> closure1 = closure2.curry2(5);
		result = (Integer)closure1.apply(4);
		assertEquals((4 - 2) * 5, result);
		
		Closure0 closure0 = closure1.curry(9);
		result = (Integer)closure0.apply();
		assertEquals((9 - 2) * 5, result);
	}
}
