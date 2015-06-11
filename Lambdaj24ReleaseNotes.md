The following new features and improvements have been added with the release 2.4:

**1. Allow to define a custom strategy to create placeholder for final classes.**

As reported by [this issue](http://code.google.com/p/lambdaj/issues/detail?id=70): "the use of `AgumentsFactory.createArgumentPlaceholderForUnknownClass` assumes if a constructor with a string or numeric value is located, that the equals - relation will return false" Unfortunately this is not always true as for the case `org.joda.time.LocalDate`. This improvement allows to overcome this limitation by allowing to register a custom argument creator factory for unknown final class as in the following example:

```
    public class LocalDateArgumentCreator implements FinalClassArgumentCreator<LocalDate> {
        private final long MSECS_IN_DAY = 1000L * 60L * 60L * 24L;
        public LocalDate createArgumentPlaceHolder(int seed) {
            return new LocalDate((long)seed * MSECS_IN_DAY);
        }
    }
```

having defined this factory you can register it as it follows:

```
Lambda.registerFinalClassArgumentCreator(LocalDate.class, new LocalDateArgumentCreator());
```

**2. More precise exceptions (re)throw.**

When a method invoked through a lambdaj proxy throws an Exception it is no longer wrapped inside an `InvocationTargetException`. Now if the original Exception is a Runtime one it is thrown as it is, otherwise it's wrapped inside a `RuntimeExcption` and then rethrown.


**3. Sort options**

2 sort options, DESCENDING and IGNORE\_CASE are now available for the lambdaj sorting features, so for example you can write:

```
sort(persons, on(Person.class).getAge(), DESCENDING);
```

or

```
sort(persons, on(Person.class).getFirstName(), IGNORE_CASE);
```


Of course you can also combine them:

```
sort(persons, on(Person.class).getFirstName(), IGNORE_CASE + DESCENDING);
```

and use them in the lambdaj's fluent interface collections. More options will be possible made available in the future as per lambda's users requests.

**4. Arguments JITting**

Now the evaluation of lambdaj's arguments (the references created with the on() method) can be made as fast as the invocation of a compiled Java method. Indeed it is optionally possible to JIT them (i.e. precompile them in plain Java classes) using ASM. This compilation process is performed in background and in my experience can make the evaluation of lambdaj's expressions using the on() construct 10 to 20% faster. This optimization is switched off by default but can be easily enabled just by invoking:

```
Lambda.enableJitting();
```

**5. Bugs fixing**

The following bugs/issues have been fixed:

  * [Sum over collection returns wrong value when done again](http://code.google.com/p/lambdaj/issues/detail?id=74)
  * [java.lang.ClassCastException has orrcued if list is empty for sum function](http://code.google.com/p/lambdaj/issues/detail?id=76)
  * [Dependency: use hamcrest-library instead of hamcrest-all in pom.xml](http://code.google.com/p/lambdaj/issues/detail?id=79)
  * [Unwrap InvocationTargetException for Runtime Exceptions](http://code.google.com/p/lambdaj/issues/detail?id=80)
  * [Lambda should properly collect objects that are iterables](http://code.google.com/p/lambdaj/issues/detail?id=83)