// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj;

import static ch.lambdaj.Lambda.*;
import static java.util.Arrays.*;

import java.io.*;
import java.util.*;

import org.junit.*;
import static org.junit.Assert.*;

import ch.lambdaj.function.closure.*;
import ch.lambdaj.function.convert.*;
import ch.lambdaj.mock.*;

/**
 * @author Mario Fusco
 */
public class ClosureTest {
	
	public Integer add(Integer val1, Integer val2) {
		return val1 + val2;
	}

	public int doNonCommutativeOpOnInt(int val1, int val2, int val3, int val4) {
		return (val1 - val2) * (val3 - val4);
	}

    public interface NonCommutativeDoer {
		int nonCommutativeDoOnInt(int val1, int val2, int val3, int val4);
	}

	@Test
	public void testSystemOut() {
		Closure1<String> println = closure(String.class); { of(System.out).println(var(String.class)); }
		println.each("mickey mouse", "donald duck", "uncle scrooge");
	}
	
	@Test
	public void testOnList() {
		StringWriter sw = new StringWriter();
		Closure writer = closure(); { of(sw).write(var(String.class)); }
        assertEquals(1, writer.getFreeVarsNumber());
		writer.each(asList("first", "second", "third"));
		assertEquals("firstsecondthird", sw.toString());
	}

	@Test
	public void testOnArray() {
		StringWriter sw = new StringWriter();
		Closure writer = closure(); { of(sw).append(var(String.class)); }
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
		Closure1<String> writer = closure(String.class); { of(sw).append(var(String.class)); }
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
				of(sb, Appendable.class).append(var(String.class));
			} catch (IOException e) { } 
		} 
		appender.each("first", "second", "third");
		assertEquals("firstsecondthird", sb.toString());
	}
	
	@Test
	public void testAddOnInteger() {
		Closure2<Integer, Integer> adder = closure(Integer.class, Integer.class); { 
			of(this).add(var(Integer.class), var(Integer.class));
		} 
		int sum = (Integer)adder.apply(2, 3);
		assertEquals(2 + 3, sum);
	}

	@Test
	public void testDo4OnInt() {
		Closure4<Integer, Integer, Integer, Integer> adder = closure(Integer.class, Integer.class, Integer.class, Integer.class); {
			of(this).doNonCommutativeOpOnInt(var(Integer.class), var(Integer.class), var(Integer.class), var(Integer.class));
		} 
		int result = (Integer)adder.apply(5, 2, 4, 3);
		assertEquals((5 - 2) * (4 - 3), result);
	}

	@Test
	public void testDo2OnInt() {
		Closure2<Integer, Integer> adder = closure(Integer.class, Integer.class); { 
			of(this).doNonCommutativeOpOnInt(var(Integer.class), 2, var(Integer.class), 3);
		} 
		int result = (Integer)adder.apply(5, 4);
        assertEquals((5 - 2) * (4 - 3), result);
	}

	@Test
	public void testCurry() {
        Closure4<Integer, Integer, Integer, Integer> closure4 = closure(Integer.class, Integer.class, Integer.class, Integer.class); {
            of(this).doNonCommutativeOpOnInt(var(Integer.class), var(Integer.class), var(Integer.class), var(Integer.class));
        }

        assertEquals(4, closure4.getFreeVarsNumber());
        int result = (Integer)closure4.apply(5, 2, 7, 3);
        assertEquals((5 - 2) * (7 - 3), result);

        result = (Integer)closure4.curry1(5).apply(4, 7, 3);
        assertEquals((5 - 4) * (7 - 3), result);
        result = (Integer)closure4.curry2(2).apply(5, 9, 3);
        assertEquals((5 - 2) * (9 - 3), result);
        result = (Integer)closure4.curry3(7).apply(5, 2, 1);
        assertEquals((5 - 2) * (7 - 1), result);
        result = (Integer)closure4.curry4(1).apply(5, 2, 6);
        assertEquals((5 - 2) * (6 - 1), result);

        Closure3<Integer, Integer, Integer> closure3 = closure(Integer.class, Integer.class, Integer.class); {
            of(this).doNonCommutativeOpOnInt(var(Integer.class), var(Integer.class), var(Integer.class), 0);
        }
        assertEquals(3, closure3.getFreeVarsNumber());
		result = (Integer)closure3.apply(5, 2, 4);
		assertEquals((5 - 2) * 4, result);
        result = (Integer)closure3.curry1(8).apply(3, 4);
        assertEquals((8 - 3) * 4, result);
        result = (Integer)closure3.curry3(4).apply(5, 2);
        assertEquals((5 - 2) * 4, result);

		Closure2<Integer, Integer> closure2 = closure3.curry2(2);
		result = (Integer)closure2.apply(7, 3);
        assertEquals(2, closure2.getFreeVarsNumber());
		assertEquals((7 - 2) * 3, result);

		Closure1<Integer> closure1 = closure2.curry2(5);
		result = (Integer)closure1.apply(4);
        assertEquals(1, closure1.getFreeVarsNumber());
		assertEquals((4 - 2) * 5, result);

        Converter<Integer, Integer> converter = closure1.cast(Converter.class);
        assertEquals((4 - 2) * 5, converter.convert(4).intValue());

        Iterator<?> results = closure1.each(4, 5, 6).iterator();
        assertEquals((4 - 2) * 5, results.next());
        assertEquals((5 - 2) * 5, results.next());
        assertEquals((6 - 2) * 5, results.next());
        assertFalse(results.hasNext());

		Closure0 closure0 = closure1.curry(9);
		result = (Integer)closure0.apply();
        assertEquals(0, closure0.getFreeVarsNumber());
		assertEquals((9 - 2) * 5, result);
	}

    @Test
    public void testCast() {
        Closure4<Integer, Integer, Integer, Integer> closure4 = closure(Integer.class, Integer.class, Integer.class, Integer.class); {
            of(this).doNonCommutativeOpOnInt(var(Integer.class), var(Integer.class), var(Integer.class), var(Integer.class));
        }
        NonCommutativeDoer doer = closure4.cast(NonCommutativeDoer.class);
        int result = doer.nonCommutativeDoOnInt(5, 2, 4, 3);
        assertEquals((5 - 2) * (4 - 3), result);
    }

	@Test
	public void testWrongCurry() {
		Closure closure = closure(); { of(this).doNonCommutativeOpOnInt(var(Integer.class), var(Integer.class), var(Integer.class), 10); }
		try {
			closure.curry(3, 4);
			fail("Curry on wrong argument position must fail");
		} catch (IllegalArgumentException iae) { }
	}

	@Test
	public void testWrongCast() {
		Closure closure = closure(); { of(this).doNonCommutativeOpOnInt(var(Integer.class), var(Integer.class), var(Integer.class), 10); }
		try {
			closure.cast(String.class);
			fail("Closure cast on concrete class must fail");
		} catch (IllegalArgumentException iae) { }
		try {
			closure.cast(Iterator.class);
			fail("Closure cast on interface having more than one method  must fail");
		} catch (IllegalArgumentException iae) { }
	}

	@Test
	public void testClosureOnNonFinalArgument() {
		Person me = new Person("Mario", "Fusco");
		Closure2<Person, Integer> ageSetter = closure(Person.class, Integer.class); { 
			of(this).setAgeOnPerson(var(Person.class), var(Integer.class));
		}
		ageSetter.apply(me, 35);
		assertEquals(35, me.getAge());
		
		Closure1<Integer> ageSetterOnMyself = ageSetter.curry1(me);
		ageSetterOnMyself.apply(36);
		assertEquals(36, me.getAge());
	}
	
	public void setAgeOnPerson(Person person, int age) {
		person.setAge(age);
	}
}
