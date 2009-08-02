// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.group;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;
import ch.lambdaj.function.argument.*;

/**
 *
 * @author Mario Fusco
 */
public class ArgumentGroupCondition<T> extends AbstractGroupCondition<Argument<?>> {
	
	private final Argument<T> groupBy;
	private final String groupName;

	ArgumentGroupCondition(T argument) {
		groupBy = actualArgument(argument);
		groupName = groupBy.getInkvokedPropertyName();
	}
	
	public String getGroupName() {
		return groupName;
	}

    public Object getGroupValue(Object item) {
        return groupBy.evaluate(item);
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
