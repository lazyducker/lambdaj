package ch.lambdaj.to.aggregate;

import static ch.lambdaj.to.aggregate.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.*;

/** @author Luca Marrocco */
public class MinTest {

	@Test
	public void testLesserFirst() {
		assertThatMin(1, 2, is(equalTo(1)));
		assertThatMin(2, 3, is(equalTo(2)));
		assertThatMin(3, 4, is(equalTo(3)));
		assertThatMin(1, null, is(equalTo(1)));
	}

	@Test
	public void testGreaterFirst() {
		assertThatMin(4, 3, is(equalTo(3)));
		assertThatMin(3, 2, is(equalTo(2)));
		assertThatMin(2, 1, is(equalTo(1)));
		assertThatMin(null, 1, is(equalTo(1)));
	}

}
