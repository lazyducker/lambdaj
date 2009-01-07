package ch.lambdaj.function.match;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import ch.lambdaj.util.IntrospectionUtil;

public class PropertyMatcher extends BaseMatcher {
	
	private String propertyName;
	private Matcher propertyMatcher;
	
	public PropertyMatcher(String propertyName, Matcher propertyMatcher) {
		this.propertyName = propertyName; 
		this.propertyMatcher = propertyMatcher;
	}

	public boolean matches(Object item) {
		Object value = IntrospectionUtil.getPropertyValue(item, propertyName);
		return propertyMatcher.matches(value);
	}

	public void describeTo(Description description) {
	}
}
