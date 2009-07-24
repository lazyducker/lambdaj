package ch.lambdaj.function.matcher;

import org.hamcrest.*;

public class AndMatcher<T> extends LambdaJMatcher<T> {
	
	private final Matcher<T>[] matchers;

	public AndMatcher(Matcher<T>... matchers) {
		this.matchers = matchers;
	}
	
	public boolean matches(Object item) {
		for (Matcher<T> matcher : matchers) if (!matcher.matches(item)) return false;
		return true;
	}

	public void describeTo(Description description) { }

    @Factory
    public static <T> AndMatcher<T> and(Matcher<T>... matchers) {
    	return new AndMatcher<T>(matchers);
    }
}
