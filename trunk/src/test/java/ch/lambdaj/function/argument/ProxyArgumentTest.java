package ch.lambdaj.function.argument;

import static ch.lambdaj.function.argument.ProxyArgument.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

public class ProxyArgumentTest {

	@Test
	public void testGroupByInsuredName() {
		assertThat(createArgumentPlaceholder(String.class), instanceOf(String.class));
		assertThat(createArgumentPlaceholder(Integer.class), instanceOf(Integer.class));
		assertThat(createArgumentPlaceholder(Long.class), instanceOf(Long.class));
		assertThat(createArgumentPlaceholder(Float.class), instanceOf(Float.class));
		assertThat(createArgumentPlaceholder(Double.class), instanceOf(Double.class));
		assertThat(createArgumentPlaceholder(Short.class), instanceOf(Short.class));
		assertThat(createArgumentPlaceholder(Byte.class), instanceOf(Byte.class));
		assertThat(createArgumentPlaceholder(Boolean.class), instanceOf(Boolean.class));
		assertThat(createArgumentPlaceholder(Date.class), instanceOf(Date.class));
	}
}
