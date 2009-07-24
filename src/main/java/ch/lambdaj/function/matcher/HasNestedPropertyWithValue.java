package ch.lambdaj.function.matcher;

import static ch.lambdaj.util.IntrospectionUtil.*;

import org.hamcrest.*;

public class HasNestedPropertyWithValue<T> extends LambdaJMatcher<T> {

    private final String propertyName;
    private final Matcher<?> value;

    public HasNestedPropertyWithValue(String propertyName, Matcher<?> value) {
        this.propertyName = propertyName;
        this.value = value;
    }

	public boolean matches(Object item) {
        try {
            return value.matches(getPropertyValue(item, propertyName));
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
    public static <T> Matcher<T> hasNestedProperty(String propertyName, Matcher<?> value) {
        return new HasNestedPropertyWithValue<T>(propertyName, value);
    }
}
