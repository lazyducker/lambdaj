package ch.lambdaj.function.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class StartStringMatcher extends BaseMatcher<String> {
	
	private String start;
	
	public StartStringMatcher(String start) {
		this.start = start;
	}
	
	public boolean matches(Object string) {
		return string.toString().startsWith(start);
	}

	public void describeTo(Description description) {
	}
}
