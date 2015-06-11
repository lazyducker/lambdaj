The native java switch construct has been often criticized for its limited applicability, since it can operate only on ints, chars and (starting from Java 5) enums. The lambdaj [Switcher](http://lambdaj.googlecode.com/svn/trunk/html/apidocs/ch/lambdaj/function/closure/Switcher.html) greatly enriches its features in order to provide a powerful and easy to read tool to implement strategy and factory patterns.

Through a fluent interface you can add cases to the Switcher that in the most general case can be defined with the method:

```
Switcher<T> addCase(Matcher<?> matcher, Closure closure);
```

When the Switcher is called if the object passed to it matches a given Matcher the corresponding closure is invoked. There are a couple of overload that allows to use any object instead of a Matcher (in this case an equalTo(object) Matcher is used, meaning that the case is activated when the switcher is invoked with an object equals to the one that defines the case) and/or any object instead of the closure (that will be the object returned by the Switcher when the corresponding case matches). For example it is possible to define a Switcher as it follows

```
Switcher<Integer> switcher = new Switcher<Integer>()
    .addCase("+", closure().of(this, "add", var(Integer.class), var(Integer.class)))
    .addCase("-", closure().of(this, "sub", var(Integer.class), var(Integer.class)))
    .addCase("*", closure().of(this, "mul", var(Integer.class), var(Integer.class)))
    .addCase("/", closure().of(this, "div", var(Integer.class), var(Integer.class)))
    .setDefault(0);
```

so when the Swicther is invoked the following value are returned.

```
int result = switcher.exec("+", 3, 2) // result == 5
int result = switcher.exec("*", 3, 2) // result == 6
int result = switcher.exec("?", 3, 2) // result == 0
```

The first argument of the exec() method is the one used to evaluate the switch while all the following ones (if any) are passed to the closure that corresponds to the matching case. It is also possible to set a default value that is returned when the first argument doesn't match any other case. Even the defualt value can be a fixed one or a closure. To give another example, having built the following Switcher:

```
Switcher<String> switcher = new Switcher<String>()
    .addCase(new Person("me", 36), "me")
    .addCase(having(on(Person.class).getAge(), lessThan(30)), "young")
    .addCase(having(on(Person.class).getAge(), greaterThan(70)), "old")
    .setDefault("adult");
```

you will obtain the following results while executing it:

```
String result = switcher.exec(new Person("me", 36)); // result == "me";
String result = switcher.exec(new Person("sister", 31)); // result == "adult";
String result = switcher.exec(new Person("dad", 71)); // result == "old";
```

Finally, by using closures that invoke constructor it is easy to say how it is possible to implement a factory pattern in a declarative way:

```
Switcher<Person> factory = new Switcher<Person>()
    .addCase("me", closure().of(Person.class, Closure.CONSTRUCTOR, "Mario", var(Integer.class)))
    .addCase("sis", closure().of(Person.class, Closure.CONSTRUCTOR, "Irma", var(Integer.class)))
    .addCase("dad", closure().of(Person.class, Closure.CONSTRUCTOR, "Domenico", var(Integer.class)));
```

so by calling:

```
Person sis = factory.exec("sis", 31);
```

you will obtain a Parson named Irma and aged 31.

As last example let's investigate how the lambdaj Switcher allows to implement a strategy pattern. For this purpose suppose you have to switch between three sorting algorithm based on some characteristic of the list to be sorted. In particular let's assume we have an algorithm specialized for Strings:

```
public List<String> sortStrings(List<String> list) {
    // a sort algorithm suitable for Strings
}
```

another one that works well with small lists having no more than 100 items:

```
public List<T> sortSmallList(List<T> list) {
    // a sort algorithm suitable for no more than 100 items
}
```

and more general purpose one:

```
public List<String> sort(List<String> list) {
    // a generic sort algorithm
}
```

Given these 3 sorting methods it is possible to create a strategy that chooses the most suitable of them in the following declarative way:

```
Switcher<List<T>> sortStrategy = new Switcher<List<T>>()
    .addCase(having(on(List.class).get(0), instanceOf(String.class))), 
        new Closure() {{ of(this).sortStrings(var(List.class)); }})
    .addCase(having(on(List.class).size(), lessThan(100))), 
        new Closure() {{ of(this).sortSmallList(var(List.class)); }})
    .setDefault(new Closure() {{ of(this).sort(var(List.class)); }});
```

and sort a list using the best available algorithm by invoking the Switcher:

```
List<T> sortedList = sortStrategy.exec(list, list);
```

Note that in this last case the list must be passed twice to the Switcher: once to decide, through the hamcrest Matchers, which sorting method should be used and once to be passed to the closure that will actually do the sort.