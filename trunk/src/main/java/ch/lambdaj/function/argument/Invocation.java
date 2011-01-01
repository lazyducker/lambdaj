// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.argument;

import java.lang.ref.*;
import java.lang.reflect.*;
import java.util.*;

import ch.lambdaj.util.*;

/**
 * Registers a method invocation
 *
 * @author Mario Fusco
 * @author Frode Carlsen
 */
final class Invocation {

    private final Class<?> invokedClass;
    private final Method invokedMethod;
    private String invokedPropertyName;
    private List<ParameterReference<Object>> weakArgs;
    private transient int hashCode;
    Invocation previousInvocation;

    Invocation(Class<?> invokedClass, Method invokedMethod, Object[] args) {
        this.invokedClass = invokedClass;
        this.invokedMethod = invokedMethod;
        invokedMethod.setAccessible(true);
        if (args != null && args.length > 0) {
            weakArgs = new LinkedList<ParameterReference<Object>>();
            for (int i = 0; i < args.length; i++) {
                weakArgs.add(new ParameterReference<Object>(args[i], !invokedMethod.getParameterTypes()[i].isPrimitive()));
            }
        }
    }

    private Object[] getConcreteArgs() {
        if (weakArgs == null) return new Object[0];
        Object[] args = new Object[weakArgs.size()];
        int i = 0;
        for (ParameterReference<Object> weakArg : weakArgs) {
            args[i++] = weakArg.get();
        }
        return args;
    }

    Class<?> getInvokedClass() {
        return invokedClass;
    }

    Method getInvokedMethod() {
        return invokedMethod;
    }

    Class<?> getReturnType() {
        return invokedMethod.getReturnType();
    }

    String getInvokedPropertyName() {
        if (invokedPropertyName == null) invokedPropertyName = IntrospectionUtil.getPropertyName(invokedMethod);
        return invokedPropertyName;
    }

    Object invokeOn(Object object) throws InvocationException {
        try {
            return object == null ? null : invokedMethod.invoke(object, getConcreteArgs());
        } catch (Exception e) {
            throw new InvocationException(e, invokedMethod, object);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(invokedMethod.toString());
        if (weakArgs != null && weakArgs.size() > 0) {
            sb.append(" with args ");
            boolean first = true;
            for (ParameterReference<?> arg : weakArgs) {
                sb.append(first ? "" : ", ").append(arg.get());
                first = false;
            }
        }
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        if (hashCode != 0) return hashCode;
        hashCode = 13 * invokedClass.hashCode() + 17 * invokedMethod.hashCode();
        if (weakArgs != null) hashCode += 19 + weakArgs.size();
        if (previousInvocation != null) hashCode += 23 * previousInvocation.hashCode();
        return hashCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof Invocation)) return false;
        Invocation otherInvocation = (Invocation) object;
        return areNullSafeEquals(invokedClass, otherInvocation.getInvokedClass()) &&
                areNullSafeEquals(invokedMethod, otherInvocation.getInvokedMethod()) &&
                areNullSafeEquals(previousInvocation, otherInvocation.previousInvocation) &&
                areNullSafeEquals(weakArgs, otherInvocation.weakArgs);
    }

    static boolean areNullSafeEquals(Object first, Object second) {
        return (first == null && second == null) || (first != null && second != null && first.equals(second));
    }

    private static final class ParameterReference<T> {
        private final boolean garbageCollectable;
        private WeakReference<T> weakRef;
        private T strongRef;

        private ParameterReference(T referent, boolean garbageCollectable) {
            this.garbageCollectable = garbageCollectable;
            if (garbageCollectable) weakRef = new WeakReference<T>(referent);
            else strongRef = referent;
        }

        private T get() {
            return garbageCollectable ? weakRef.get() : strongRef;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ParameterReference)) return false;
            ParameterReference<T> otherRef = (ParameterReference<T>) obj;
            return areNullSafeEquals(get(), otherRef.get());
        }
    }
}
