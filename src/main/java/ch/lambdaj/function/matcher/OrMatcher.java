package ch.lambdaj.function.matcher;

import org.hamcrest.*;

public class OrMatcher<T> extends LambdaJMatcher<T> {
	
	private final Matcher<T>[] matchers;

	public OrMatcher(Matcher<T>... matchers) {
		this.matchers = matchers;
	}
	
	public boolean matches(Object item) {
		for (Matcher<T> matcher : matchers) if (matcher.matches(item)) return true;
		return false;
	}

	public void describeTo(Description description) { }

    @Factory
    public static <T> OrMatcher<T> or(Matcher<T>... matchers) {
    	return new OrMatcher<T>(matchers);
    }
}