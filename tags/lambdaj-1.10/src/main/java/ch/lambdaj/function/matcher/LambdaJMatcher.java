package ch.lambdaj.function.matcher;

import org.hamcrest.*;

public abstract class LambdaJMatcher<T> extends BaseMatcher<T> {

	@SuppressWarnings("unchecked")
	public LambdaJMatcher<T> and(Matcher<T> matcher) {
		return AndMatcher.and(this, matcher);
	}

	@SuppressWarnings("unchecked")
	public LambdaJMatcher<T> or(Matcher<T> matcher) {
		return OrMatcher.or(this, matcher);
	}
}
