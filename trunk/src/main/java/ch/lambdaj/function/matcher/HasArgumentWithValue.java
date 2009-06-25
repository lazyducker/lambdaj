package ch.lambdaj.function.matcher;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;

import org.hamcrest.*;

import ch.lambdaj.function.argument.*;

public class HasArgumentWithValue<T> extends LambdaJMatcher<T> {
	
    private final Argument<T> argument;
    private final Matcher<? extends Object> value;

    public HasArgumentWithValue(Argument<T> argument, Matcher<? extends Object> value) {
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
    public static HasArgumentWithValue<Boolean> having(Boolean argument) {
    	return having(argument, booleanMatcher);
    }
    
    @Factory
    public static <T> HasArgumentWithValue<T> having(T argument, Matcher<? extends Object> value) {
    	return new HasArgumentWithValue<T>(actualArgument(argument), value);
    }

    private static BooleanMatcher booleanMatcher = new BooleanMatcher();
    private static class BooleanMatcher extends BaseMatcher<Boolean> {
		public boolean matches(Object item) {
			return ((Boolean)item).booleanValue();
		}
		public void describeTo(Description description) { }
    }
}
