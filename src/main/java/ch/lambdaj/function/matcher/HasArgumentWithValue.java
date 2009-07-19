package ch.lambdaj.function.matcher;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;

import org.hamcrest.*;

import ch.lambdaj.function.argument.*;

public class HasArgumentWithValue<T, A> extends LambdaJMatcher<T> {
	
    private final Argument<A> argument;
    private final Matcher<? extends Object> value;

    public HasArgumentWithValue(Argument<A> argument, Matcher<? extends Object> value) {
        this.argument = argument;
        this.value = value;
    }

	public boolean matches(Object item) {
		if (argument == null) return false;
        try {
            return value.matches(argument.evaluate(item));
        } catch (Exception e) {
            return false;
        } 
	}
	
    public void describeTo(Description description) {
        description.appendText("hasArgument(");
        description.appendValue(argument.getInkvokedPropertyName());
        description.appendText(", ");
        description.appendDescriptionOf(value);
        description.appendText(")");
    }

    @Factory
    public static <T> HasArgumentWithValue<T, Boolean> havingValue(Boolean argument) {
    	return havingValue(argument, booleanMatcher);
    }
    
    @Factory
    public static <T, A> HasArgumentWithValue<T, A> havingValue(A argument, Matcher<? extends Object> value) {
    	return new HasArgumentWithValue<T, A>(actualArgument(argument), value);
    }

    private static BooleanMatcher booleanMatcher = new BooleanMatcher();
    private static class BooleanMatcher extends BaseMatcher<Boolean> {
		public boolean matches(Object item) {
			return ((Boolean)item).booleanValue();
		}
		public void describeTo(Description description) { }
    }
}
