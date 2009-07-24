package ch.lambdaj.group;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;
import ch.lambdaj.function.argument.*;

public class ArgumentGroupCondition extends AbstractGroupCondition<Argument<?>> {
	
	private final Argument<?> groupBy;
	private final String groupName;

	ArgumentGroupCondition(Object argument) {
		groupBy = actualArgument(argument);
		groupName = groupBy.getInkvokedPropertyName();
	}
	
	public String getGroupName() {
		return groupName;
	}

	public String getGroupValue(Object item) {
		return asNotNullString(groupBy.evaluate(item));
	}

	@Override
	public ArgumentGroupCondition as(String alias) {
		return (ArgumentGroupCondition)super.as(alias);
	}

	public ArgumentGroupCondition head(Object argument) {
		Argument<?> actualArgument = actualArgument(argument);
		additionalProperties.put(actualArgument.getInkvokedPropertyName(), actualArgument);
		return this;
	}

	public ArgumentGroupCondition head(Object argument, String alias) {
		additionalProperties.put(alias, actualArgument(argument));
		return this;
	}

	public String getAdditionalPropertyValue(String name, Object item) {
		return asNotNullString(additionalProperties.get(name).evaluate(item));
	}
}
