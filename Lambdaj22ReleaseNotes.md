The following new features and improvements have been added with the release 2.2:

**1.** A method to **convert the values of a given Map through a lambdaj Converter** (suggested by Robert Blumen):

```
Map<K,W> convertMap(Map<K,V> map, Converter<V,W> converter)
```

allows to convert the values of the given Map by applying to each of them the conversion defined by the given converter.

**2.** A method that **count the occurencies of each object in an Iterable**:

```
Map<A,Integer> count(Iterable<A>)
```

returning a Map having as keys the objects in the Iterable and as values the Integers corresponding to the number of their occurrences.

**3.** Also the **object on which the closure is invoked can be an unbuond variable**.

By passing to the of() method a class instead of an object you're saying that the object on which the closure will be invoked will be passed during the closure invocation itself. For example if you define a closure as it follows:

```
Closure2<Person, Integer> ageSetter = closure(Person.class, Integer.class); {
    of(Person.class).setAge(var(Integer.class));
}
```

you can then invoke it in this way:

```
ageSetter.apply(person, 35);
```

**4.** The possibility to **define a closure also without using a ThreadLocal**.

Somebody found confusing (or code smell) to bound a closure to a ThreadLocal before to define it. Now you can create a closure without any ThreadLocal as it follows:

```
Closure2<Person, Integer> ageSetter = new Closure2<Person, Integer>() {{
    of(Person.class).setAge(var(Integer.class));
}};
```

**5.** The possibility to **define a closure on a final class or that invokes a static method**.

For example the following closure will cause to invoke the static method Integer.parseInt(String string):

```
Closure1<String> intParser = closure(String.class).of(Integer.class, "parseInt", var(String.class));
```

this one returns a substring of the string "mario" by calling the method substring(int start, int end)

```
Closure2<Integer, Integer> substringOfMyName = closure(Integer.class, Integer.class)
    .of("mario", "substring", var(Integer.class), var(Integer.class));
```

and this last one converts a given string in upper case:

```
Closure1<String> toUpperCase = closure(String.class).of(String.class, "toUpperCase");
```

The method to be invoked by the closure is defined by its name and not by registering the method invocation on the proxy returned by the of() method. Of course it is possible to define a closure in this way also when the invoked method is not static and doesn't belong to a final class, even if in this way the strong typed invocation is lost.

**6.** The possibility to **define a closure that invokes a constructor**.

By using the constant Closure.CONSTRUCTOR as method name in the closure definition form provided by the former point it is possible to define a closure that invokes a costructor. For example if you have a class Person with a constructor Person(String name, int age) you can define a closure that invokes it:

```
Closure2<String, Integer> personCreator = closure()
    .of(Person.class, Closure.CONSTRUCTOR, var(String.class), var(Integer.class));
```

and then create a Person by invoking apply() on this closure as it follows:

```
Person me = (Person)personCreator.apply("Mario", 36);
```

**7.** A **[Switcher](http://code.google.com/p/lambdaj/wiki/Switcher)** that greatly enriches the feature of the native java switch in order to provide a powerful and easy to read tool to implement strategy and factory patterns.