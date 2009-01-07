package ch.lambdaj;

import org.hamcrest.*;

import ch.lambdaj.function.match.*;

/** @author Luca Marrocco */
public class Matchers {
	@Factory
	public static PropertyMatcher where(String propertyName, Matcher propertyMatcher) {
		return new PropertyMatcher(propertyName, propertyMatcher);
	}

	@Factory
	public static StartStringMatcher startsWith(String start) {
		return new StartStringMatcher(start);
	}
}
