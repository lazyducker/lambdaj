package ch.lambdaj.function.matcher;

import org.hamcrest.*;

import ch.lambdaj.util.*;

public class HasNestedPropertyWithValue<T> extends BaseMatcher<T> {

    private final String propertyName;
    private final Matcher<? extends Object> value;

    public HasNestedPropertyWithValue(String propertyName, Matcher<? extends Object> value) {
        this.propertyName = propertyName;
        this.value = value;
    }

	public boolean matches(Object item) {
        try {
            return value.matches(IntrospectionUtil.getPropertyValue(item, propertyName));
        } catch (Exception e) {
            return false;
        } 
	}
	
    public void describeTo(Description description) {
        description.appendText("hasProperty(");
        description.appendValue(propertyName);
        description.appendText(", ");
        description.appendDescriptionOf(value);
        description.appendText(")");
    }

    @Factory
    public static <T> Matcher<T> hasNestedProperty(String propertyName, Matcher<? extends Object> value) {
        return new HasNestedPropertyWithValue<T>(propertyName, value);
    }
}
