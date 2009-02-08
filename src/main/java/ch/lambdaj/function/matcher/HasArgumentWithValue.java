package ch.lambdaj.function.matcher;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;

import org.hamcrest.*;

import ch.lambdaj.function.argument.*;

public class HasArgumentWithValue<T> extends BaseMatcher<T> {
	
    private final Argument argument;
    private final Matcher<? extends Object> value;

    public HasArgumentWithValue(Argument argument, Matcher<? extends Object> value) {
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
    public static <T> Matcher<T> having(Object argument, Matcher<? extends Object> value) {
    	return new HasArgumentWithValue<T>(actualArgument(argument), value);
    }

}
