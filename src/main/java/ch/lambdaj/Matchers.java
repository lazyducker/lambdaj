package ch.lambdaj;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import ch.lambdaj.function.matcher.PropertyMatcher;
import ch.lambdaj.function.matcher.StartStringMatcher;

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
