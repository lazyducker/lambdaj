lambdaj has a known limitation in both of its main features due to 2 different issues of the java language: lack of reified generics and impossibility to proxy a final class.

In more details the first problem doesn't allow lambdaj to infer the actual type that should be returned by the `forEach()` method when a null or empty collection is passed to it. That means that the following piece of code:

```
List<Person> persons = new ArrayList<Person>();
forEach(persons).setLastName("Fusco");
```

will cause lambdaj to throw an exception because it is not able to decide which actual Class should be proxied. To overcome this problem, when you aren't sure that a given collection has at least one item in it, it is advised to use the `forEach()` overloaded method that accepts a Class as second parameter as it follows:

```
forEach(persons, Person.class).setLastName("Fusco");
```

The second issue caused that when the `on()` method meets a final class it cannot proxy the returned value. It means that for example the following statement will not work:

```
List<Person> sortedByNamePersons = sort(persons, on(Person.class).getName().toLowerCase());
```

Even in this case lambdaj will throw an exception complaining that the requested argument cannot be resolved. To fix this last problem it is necessary to find some workaround in order to "help" lambdaj to work as you expect, probably by adding the method `getLowerCaseName()` to the Person class.