// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.proxy;

import java.lang.reflect.*;

/**
 * An utility class of static factory methods that provide facilities to create proxies
 * @author Mario Fusco
 * @author Sebastian Jancke
 */
@SuppressWarnings("unchecked")
public final class ProxyUtil {
	
	private ProxyUtil() { }
	
    // ////////////////////////////////////////////////////////////////////////
    // /// Generic Proxy
    // ////////////////////////////////////////////////////////////////////////

    /**
     * Check if the given class is nor final neither a primitive one
     * @param clazz The class to be checked
     * @return True if the class is proxable, false otherwise
     */
    public static boolean isProxable(Class<?> clazz) {
        return !clazz.isPrimitive() && !Modifier.isFinal(clazz.getModifiers()) && !clazz.isAnonymousClass();
    }

    /**
     * Creates a dynamic proxy
     * @param interceptor The interceptor that manages the invocations to the created proxy
     * @param clazz The class to be proxied
     * @param failSafe If true return null if it is not possible to proxy the request class, otherwise throws an UnproxableClassException
     * @param implementedInterface The interfaces that has to be implemented by the new proxy
     * @return The newly created proxy
     */
	public static <T> T createProxy(InvocationInterceptor interceptor, Class<T> clazz, boolean failSafe, Class<?> ... implementedInterface) {
        if (clazz.isPrimitive()) return manageUnproxableClass(clazz, failSafe);
		if (clazz.isInterface()) return (T)createNativeJavaProxy(clazz.getClassLoader(), interceptor, concatClasses(new Class<?>[] { clazz }, implementedInterface));
        if (!Modifier.isFinal(clazz.getModifiers()) && !clazz.isAnonymousClass()) return ClassImposterizer.INSTANCE.imposterise(interceptor, clazz, implementedInterface);
        if (Proxy.isProxyClass(clazz)) return (T)createNativeJavaProxy(clazz.getClassLoader(), interceptor, concatClasses(implementedInterface, clazz.getInterfaces()));
        return manageUnproxableClass(clazz, failSafe);
	}

    private static <T> T manageUnproxableClass(Class<T> clazz, boolean failSafe) {
        if (failSafe) return null;
        throw new UnproxableClassException(clazz);
    }

    /**
     * Creates a dynamic proxy to be used as argument
     * @param interceptor The interceptor that manages the invocations to the created proxy
     * @param clazz The class to be proxied
     * @return The newly created proxy
     */
    public static <T> T createArgumentProxy(InvocationInterceptor interceptor, Class<T> clazz) {
        return clazz.isInterface() ?
                (T)createNativeJavaProxy(clazz.getClassLoader(), interceptor, clazz) :
                ClassImposterizer.INSTANCE.imposterise(interceptor, clazz);
    }

    // ////////////////////////////////////////////////////////////////////////
    // /// Void Proxy
    // ////////////////////////////////////////////////////////////////////////

    /**
     * Creates a proxy of the given class that just ignores any invocation on it
     * @param clazz The class to be proxied
     * @return The newly created proxy
     */
	public static <T> T createVoidProxy(Class<T> clazz) {
		return createProxy(InvocationInterceptor.VOID, clazz, false, InvocationInterceptor.VoidInterceptor.class);
	}
	
    // ////////////////////////////////////////////////////////////////////////
    // /// Iterable Proxy
    // ////////////////////////////////////////////////////////////////////////
    
    /**
     * Creates a proxy of the given class that also decorates it with Iterable interface
     * @param interceptor The object that will intercept all the invocations to the returned proxy
     * @param clazz The class to be proxied
     * @return The newly created proxy
     */
	public static <T> T createIterableProxy(InvocationInterceptor interceptor, Class<T> clazz) {
        if (clazz.isPrimitive()) return null;
        return createProxy(interceptor, normalizeProxiedClass(clazz), false, Iterable.class);
	}

    private static <T> Class<T> normalizeProxiedClass(Class<T> clazz) {
        if (clazz == String.class) return (Class<T>)CharSequence.class;
        return clazz;
    }

    // ////////////////////////////////////////////////////////////////////////
    // /// Private
    // ////////////////////////////////////////////////////////////////////////
    
    private static Object createNativeJavaProxy(ClassLoader classLoader, InvocationHandler interceptor, Class<?> ... interfaces) {
        return Proxy.newProxyInstance(classLoader, interfaces, interceptor);
    }
	
	private static Class<?>[] concatClasses(Class<?>[] first, Class<?>[] second) {
        if (first == null || first.length == 0) return second;
        if (second == null || second.length == 0) return first;
        Class<?>[] concatClasses = new Class[first.length + second.length];
        System.arraycopy(first, 0, concatClasses, 0, first.length);
        System.arraycopy(second, 0, concatClasses, first.length, second.length);
        return concatClasses;
	}
}
