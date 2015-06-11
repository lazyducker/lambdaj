<table><tr>
<td valign='top'>

<h3>lambdaj news:</h3>

<ul><li><b>The slides of the <a href='http://www.eventreg.com/cc250/sessionDetail.jsp?SID=314002'>talk about lambdaj</a> I gave at javaone 2010 are viewable below</b>
</li><li><b>lambdaj 2.3.2 is 45% faster than release 2.3. A detailed <a href='http://code.google.com/p/lambdaj/wiki/PerformanceAnalysis'>performance analysis</a> is available</b>
</li><li><b>lambdaj 2.3 has been released. It adds <a href='http://code.google.com/p/lambdaj/wiki/LambdaCollections'>fluent interface collections</a></b>
</li><li><b>lambdaj 2.x brings (almost) real <a href='http://code.google.com/p/lambdaj/wiki/Closures'>closure</a> to java</b>
</li><li><b>lambdaj 2.x has been downloaded 5000 times (non counting maven users)</b></li></ul>

<table width='100%'><tr>
<td valign='top'>
<a href='http://twitter.com/mariofusco' title='follow me on twitter'><img src='http://lambdaj.googlecode.com/svn/trunk/html/img/twitter.png' alt='' border='0' /></a>
</td><td>
<a href='http://groups.google.com/group/lambdaj' title='join the lambdaj user group'><img src='http://lambdaj.googlecode.com/svn/trunk/html/img/mailbox.gif' alt='join the lambdaj user group' border='0' /></a>
</td><td>
<a href='http://www.java.net/'><img src='http://today.java.net/images/javanet_button_170.gif' alt='java.net Member Button: 170px wide' border='0' /></a>
</td><td>
</td></tr></table>

</td>
<td width='230px' align='right' valign='top'>
<img src='http://lambdaj.googlecode.com/svn/trunk/html/img/lambdaj.png' />
</td>
</tr></table>

&lt;wiki:gadget url="http://lambdaj.googlecode.com/svn/trunk/html/slidesharegadget.xml" width="850" height="652" /&gt;

<p />
# What is lambdaj for? #
How many times have you read or written the same two or three lines of code that frequently seem to go together, and even though they operate on different objects, feel like the same thing? And how often these repetitions involve some sort of collections iteration or more generically manipulation? These repetitions in the code is something that developers eventually learn to filter out and ignore when reading code, once they figure out where the interesting parts are placed. But even if the developers get used to it, it slows them down. Code like that is clearly written for computers to execute, not for developers to read.

lambdaj is a library that makes easier to address this issue by allowing to manipulate collections in a pseudo-functional and statically typed way. In our experience to iterate over collection, especially in nested loops, is often error prone and makes the code less readable. The purpose of this library is to alleviate these problems employing some functional programming techniques but without losing the static typing of java. We impose this last constraint to make refactoring easier and safer and allow the compiler to do its job.

## Access collections without explicit loops ##

The main purpose of lambdaj is to partially eliminate the burden to write (often nested and poorly readable) loops while iterating over collections. In particular it allows to iterate collections in order to:
  * filter its items on a given condition
  * convert each item with a given rule
  * extract a given property from each item
  * sort the items on the values of one of their property
  * group or index the items on the value of one or more properties
  * invoke a method on each item
  * sum (or more generally aggregate) the items or the values of one of their property
  * concatenate the string representation of the items or of the values of one of their property
without to write a single explicit loop.

## Make code more readable through DSL ##

[Domain-specific language](http://en.wikipedia.org/wiki/Domain-specific_language) are a quite new and rapidly growing trend in computer programming. The main benefit in writing code with a DSL style is to make it more readable and then more maintainable. With a few sugar methods lambdaj should give you the ability to express the business rules of your application in pseudo-english syntax.

# How does lambdaj work? #

There are 2 ideas at the base of lambdaj. The first one is to treat a collection of objects as it was a single object by allowing to propagate a single method invocation to all the objects in the collection as in the following example:

```
List<Person> personInFamily = asList(new Person("Domenico"), new Person("Mario"), new Person("Irma"));
forEach(personInFamily).setLastName("Fusco");
```

In this example all the persons in the list belongs to the same family so they all have the same last name. The [forEach](http://lambdaj.googlecode.com/svn/trunk/html/apidocs/ch/lambdaj/Lambda.html#forEach(java.lang.Iterable)) method actually returns a proxy object that implements both the Iterable interface and all the methods in each object in the given list. That allows to invoke a method of the Person object on the object returned by the forEach method as it was an instance of the Person class. When you do that, under the hood lambdaj propagates your invocation to all the objects in the collection.

The second idea on which lambdaj is built on is the possibility to have a pointer to a java method in a statically typed way by using the [on](http://lambdaj.googlecode.com/svn/trunk/html/apidocs/ch/lambdaj/Lambda.html#on(java.lang.Class)) method. That allows to easily and safely define on which argument a given lambdaj feature has to be applied. For example in the following statement we used the on construct in order to say the argument (their respective ages) on which a list of persons has to be sorted:

```
List<Person> sortedByAgePersons = sort(persons, on(Person.class).getAge());
```

Comparing this last statement with the piece of code necessary to achieve the same result in plain Java:

```
List<Person> sortedByAgePersons = new ArrayList<Person>(persons);
Collections.sort(sortedByAgePersons, new Comparator<Person>() {
	public int compare(Person p1, Person p2) {
	   return Integer.valueOf(p1.getAge()).compareTo(p2.getAge());
	}
});
```

makes evident how lambdaj could improve both the productivity while writing code and probably even more important its readability when you are called to maintain it.

# lambdaj features #

As stated lambdaj is designed to easily manipulate collections. Its features are intended to filter, convert, index and aggregate the items of a collection without explicitly iterate on it. Moreover the lambdaj API are designed to be easily concatenated in order to jointly use two or more features in a single statement. To investigate these features in more details read the [lambdaj features wiki page](http://code.google.com/p/lambdaj/wiki/LambdajFeatures).

Since the release 2.0 lambdaj also makes possible to define and use closures in its usual DSL style. Check out how it works on the [closures wiki page](http://code.google.com/p/lambdaj/wiki/Closures).


---

<table><tr><td>
YourKit is kindly supporting open source projects with its full-featured Java Profiler. </td></tr><tr><td>
YourKit, LLC is the creator of innovative and intelligent tools for profiling<br>
Java and .NET applications. </td></tr><tr><td> Take a look at YourKit's leading software products<br>
<a href='http://www.yourkit.com/java/profiler/index.jsp'>YourKit Java Profiler</a> and<br>
<a href='http://www.yourkit.com/.net/profiler/index.jsp'>YourKit .NET Profiler</a>.</td></tr></table>