# lambdaj features #

lambdaj is designed to easily manipulate collections. Its features are intended to filter, convert, index and aggregate the items of a collection without explicitly iterate on it. Moreover the lambdaj API are designed to be easily concatenated in order to jointly use two or more features in a single statement. All those features are provided as static methods in the class Lambda, so the best way to use them is just to add the following import:

```
import static ch.lambdaj.Lambda.*;
```

in the classes where you want to use it. Let's investigate these features in more details: ...

## Filtering on a condition ##

To [filter](http://lambdaj.googlecode.com/svn/trunk/html/apidocs/ch/lambdaj/Lambda.html#filter(org.hamcrest.Matcher,%20java.lang.Iterable)) the items of a collection on a given condition is a very common task and using lambdaj can be as easy as in the following example:

```
List<Integer> biggerThan3 = filter(greaterThan(3), asList(1, 2, 3, 4, 5));
```

The condition that defines how to filter the list is expressed as an hamcrest matcher. [Hamcrest](http://code.google.com/p/hamcrest/) is a library used by default even in Junit4 to describe in a DSL way the assertions that need to be verified during a unit test. In case you can find an hamcrest matcher that defines the condition you need, it is possible to define your own matchers as it follows:

```
Matcher<Integer> odd = new Predicate<Integer>() {
	public boolean apply(Integer item) {
		return item % 2 == 1;
	}
};
```

and then you can filter the odds number of a list of Integer in a way similar as before:

```
List<Integer> oddNumbers = filter(odd, asList(1, 2, 3, 4, 5));
```

Note that the class Predicate is a proprietary lambdaj implementation of an Hamcrest Matcher allowing type safety and the possibility to combine more of them in a fluent way through its or() and and() methods.

It is also possible to filter a list of objects based on the value of one their property. For example given the following list of Persons:

```
Person me = new Person("Mario", "Fusco", 35);
Person luca = new Person("Luca", "Marrocco", 29);
Person biagio = new Person("Biagio", "Beatrice", 39);
Person celestino = new Person("Celestino", "Bellone", 29);
List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
```

it is possible to filter the ones having more than 30 years applying the following filter:

```
List<Person> oldFriends = filter(having(on(Person.class).getAge(), greaterThan(30)), meAndMyFriends);
```

This example uses the hamcrest Matcher [having](http://lambdaj.googlecode.com/svn/trunk/html/apidocs/ch/lambdaj/function/matcher/HasArgumentWithValue.html#having(java.lang.Object,%20org.hamcrest.Matcher)) provided by lambdaj that extracts the value of property from each object in the list and compare it using another hamcrest Matcher.

The lambdaj [select](http://lambdaj.googlecode.com/svn/trunk/html/apidocs/ch/lambdaj/Lambda.html#select(java.lang.Iterable,%20org.hamcrest.Matcher)) method has the same semantics of the `filter` one, but it comes with a wider set of overloaded functions that allow to select the first item in a given collection that matches a given hamcrest Matcher, the unique item of that kind (it throws an exception if it finds more than one item that satisfy the Matcher) and a subset of non duplicated items with that characteristic.

## Defining object's property in a statically typed way ##

In the last example of the former paragraph, the property to be extracted from the list of objects is defined via another lambdaj construct called [on](http://lambdaj.googlecode.com/svn/trunk/html/apidocs/ch/lambdaj/Lambda.html#on(java.lang.Class)). This method is widely used throughout lambdaj in order to statically define a method that needs to be invoked on given class of objects. More formally the method `T on(Class<T> clazz)` returns a proxy object that mocks the given Class, registering all the subsequent invocations on the objects of that Class. Note that this construct works not only for a single method invocation but even for a sequence of calls as in the following example:

```
List<Person> oldFriends = filter(having(on(Person.class).getBestFriend().getAge(), greaterThan(30)), meAndMyFriends);
```

## Aggregating items or item's values ##

Another common task while working with collections is to aggregate their items in some way. One of the most common way to aggregate items (more specifically numbers) is by summing them as it follows:

```
int sum = sum(asList(1, 2, 3, 4, 5));
assertEquals(15, sum);
```

Actually this function is only a specialization of the more general [aggregate](http://lambdaj.googlecode.com/svn/trunk/html/apidocs/ch/lambdaj/Lambda.html#aggregate(java.lang.Object,%20ch.lambdaj.function.aggregate.Aggregator)) method defined as:

```
T aggregate(Object iterable, Aggregator<T> aggregator);
```

where an Aggregator is an implementation of the lambdaj's interface

```
public interface Aggregator<T> {
	T aggregate(java.lang.Iterable<? extends T> iterable);
}
```

that defines how to aggregate a collection of object in a single object of the same type. Could be also convenient to implement an Aggregator by extending the abstract implementation PairAggregator

```
public class PairAggregator<T> {
	protected abstract T emptyItem();
	protected abstract T aggregate(T first, T second);
```

that defines an aggregation operation through its idempotent item and how to 2 items must be aggregated. Given this definition the aggregate operation that sums Integer can be defined as:

```
public class Sum extends PairAggregator<Integer> {
	public Integer emptyItem() {
		return 0;
	}
	public Integer aggregate(Integer first, Integer second) {
		return first + second;
	}
}
```

Using the `on` statement it is also possible to sum (or more in general aggregate) the values of a particular property of a collection of objects. For example to totalize the ages of a list of Persons you can just write:

```
int totalAge = sum(meAndMyFriends, on(Person.class).getAge());
```

The same result can also be achieved in a slightly different ways by using the [sumFrom](http://lambdaj.googlecode.com/svn/trunk/html/apidocs/ch/lambdaj/Lambda.html#sumFrom(java.lang.Iterable)) method:

```
int totalAge = sumFrom(meAndMyFriends).getAge();
```

that in turns it is only another specialization of the [aggregateFrom](http://lambdaj.googlecode.com/svn/trunk/html/apidocs/ch/lambdaj/Lambda.html#aggregateFrom(java.lang.Iterable,%20ch.lambdaj.function.aggregate.Aggregator)) method. More formally the object returned by the `aggregateFrom` method is a lambda function defined as:

```
aggregateFrom : (aggregator, list) => lambda : (aggregator, convert : object => number) => number
```

while the 'sumFrom' method is the [curry](http://en.wikipedia.org/wiki/Currying) of the 'aggregateFrom' one where the aggregator function has been bound to the + (sum) operator:

```
curry(aggregateFrom) : aggregator => sum = sumFrom : list : => lambda : (convert : object => number) => number
```

In the end a further curry of the convert function (the definition of how an item must be converted in a number) is done by invoking on that returned object the method that returns the values of the property to be summed.

The same features are present to select the minimum and maximum item's value from a collection of Comparable objects. Moreover you can easily adapt the aggregation features of lambdaj by writing an implementation of the [Aggregator](http://lambdaj.googlecode.com/svn/trunk/html/apidocs/ch/lambdaj/function/aggregate/Aggregator.html) that fits your needs.

## Joining strings ##

String concatenation is a more specific, but very frequently used, type of object aggregation. Since it is a so common task, lambdaj offers some facilities to achieve it in many different flavors through its python-style [join](http://lambdaj.googlecode.com/svn/trunk/html/apidocs/ch/lambdaj/Lambda.html#join(java.lang.Object)) function. The simplest example of how to use this feature is:

```
List<String> strings = asList("first", "second", "third");
String result = join(strings);
assertEquals(result, "first, second, third");
```

As you can see passing a List of String to the join function it concatenates them using the default separator ", ". Of course it is also possible to choose a different separator like it follows:

```
List<String> strings = asList("first", "second", "third");
String result = join(strings, " - ");
assertEquals(result, "first - second - third");
```

A quite more sophisticated string concatenation feature is given by the [joinFrom](http://lambdaj.googlecode.com/svn/trunk/html/apidocs/ch/lambdaj/Lambda.html#joinFrom(java.lang.Iterable)) method. In a similar way as already seen for the `sumFrom` method, this method returns a lambda function formally defined as it follows:

```
joinFrom : (concat, iterable) => lambda : (convert : object => object) => string
```

so, in this case, the curry of the convert function defines how an item must be converted in a String. This is done by invoking on that returned object the method that returns the values of the property to be further concatenated in a String as in the following statement:

```
String friendNames = joinFrom(meAndMyFriends).getFirstName();
assertEquals(friendNames, "Mario, Luca, Biagio, Celestino");
```

that gives as result the concatenation of the first name of all the persons in the meAndMyFriends List. Even in this case there is an overload that allows to change the default separator.

## Converting, indexing and sorting items ##

lambdaj allows to convert the items of a List by applying on all of them the conversion function defined by implementing the one-method [Converter](http://lambdaj.googlecode.com/svn/trunk/html/apidocs/ch/lambdaj/function/convert/Converter.html) interface. For example the function that converts a String in an int representing its length can be defined as:

```
public class StringLengthConverter extends PropertyExtractor<String, Integer> {
	public int convert(String string) {
		return string.length();
	}
}
```

In this way it is possible to convert a List of String in a List of int holding the String's length by using the `convert` method as simply as:

```
List<Integer> lengths = convert(strings, new StringLengthConverter());
```

lambdaj comes with a set of some predefined converters like the `PropertyExtractor` one. This is a more generic version of the `StringLengthConverter` that allows to extract any given object's property by its name. It means the former conversion can be achieved in the same way by calling:

```
List<Integer> lengths = convert(strings, new PropertyExtractor("length"));
```

Another way to achieve the same result it is through the `on` method that allows to define how to convert a given object by extracting one of its property in a statically typed way. For example you can extract the ages of a List of Persons, or in other word to convert that List of in a List of int representing their age, as:

```
List<Integer> ages = extract(persons, on(Person.class).getAge());
```

Going a step further it is even possible to index a List of Object using as key the value resulting from a conversion. For example the following [index](http://lambdaj.googlecode.com/svn/trunk/html/apidocs/ch/lambdaj/Lambda.html#index(java.lang.Object,%20T)) invocation method returns a Map having the given List of Persons as values indexed on their corresponding first names as keys:

```
Map<String, Person> personsByName = index(persons, on(Person.class).getFirstName());
```

The `on` operator can also be used in conjunction with the [sort](http://lambdaj.googlecode.com/svn/trunk/html/apidocs/ch/lambdaj/Lambda.html#sort(java.lang.Object,%20A,%20java.util.Comparator)) method in order to sort a list of items on the value of one of their property. As it is easy to realize, the following statement sorts a List of Persons on their respective ages:

```
List<Person> sorted = sort(persons, on(Person.class).getAge());
```

## Grouping items ##

The former `index` feature is enough in many cases, but it has at least 2 big limitations: it doesn't manage conflicts if 2 or more objects should be indexed on the same key value and it doesn't easily allow to index the objects in a multilevel hierarchy.

To overcome these constraints lambdaj provides a feature to (optionally hierarchically) group objects on the value of their properties. As usual the preferred (statically typed) way in lambdaj to choose an object's property is via the `on` construct, so to group a List of Persons based on their ages it is sufficient to write:

```
Group<Person> group = group(meAndMyFriends, by(on(Person.class).getAge()));
```

The [Group](http://lambdaj.googlecode.com/svn/trunk/html/apidocs/ch/lambdaj/group/Group.html) returned by the former statement group the Persons by their age. It is then possible to find the subgroup of the 29 years old Persons as it follows:

```
Group<Person> group29aged = group.findGroup("29");
assertEquals(2, group29aged.getSize());
```

or even just to get the Collection of that Persons:

```
Collection<Person> persons29Aged = group.find("29");
assertTrue(persons29Aged.contains(luca));
assertTrue(persons29Aged.contains(celestino));
```

As stated it is also possible to group the same List in a hierarchy with as many level as you want. For example the following statement groups the usual List of Person in a 2 level hierarchy indexed by their age and then by their first name:

```
Group<Person> group = group(meAndMyFriends, by(on(Person.class).getAge()), by(on(Person.class).getFirstName()));
```

so it is possible to find the group of the 29 aged persons and its subgroup of the 29 aged persons named Luca as it follows:

```
Group<Person> group29aged = group.findGroup("29");
Group<Person> group29agedNamedLuca = group29aged.findGroup("Luca");
```

## Projecting object's property values ##

It is possible to project the property values of a given object on an object of a different class by invoking the constructor of this target class. This feature can be useful for example when you need to transform a list of beans in the corresponding list of data transfer objects. Supposing you have a Person bean as it follows:

```
class Person {
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getCompleteName() { return getFirstName() + " " + getLastName(); }
    public int getAge() { return age; }
}
```

and a PersonDto? on which you have to transfer the data of a Person:

```
class PersonDto {
    private final String name;
    private final int age;
    PersonDto(String name, int age) {
            this.name = name;
            this.age = age;
    }
}
```

you can convert a List of Person in the corresponding List of PersonDto? as it follows:

```
List<PersonDto> perosonDtos = project(persons, PersonDto.class, on(Person.class).getCompleteName(), on(Person.class).getAge()); 
```