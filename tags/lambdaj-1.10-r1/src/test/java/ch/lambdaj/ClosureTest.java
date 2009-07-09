package ch.lambdaj;

import static ch.lambdaj.Lambda.*;
import static java.util.Arrays.*;

import org.junit.*;

import ch.lambdaj.function.closure.*;

/**
 * @author Mario Fusco
 */
public class ClosureTest {
	
	@Test
	public void testOnList() {
		Closure println = closure(); { of(System.out).println((String)null); } 
		println.each(asList("mickey mouse", "donald duck", "uncle scrooge"));
	}

	@Test
	public void testOnArray() {
		Closure println = closure(); { of(System.out).println((String)null); } 
		println.each("mickey mouse", "donald duck", "uncle scrooge");
	}
}
