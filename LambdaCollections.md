lambdaj's API are designed to be easily combined in a single complex statement as in the following example:

```
List<Person> buyersSortedByAges = sort(
    extract(
        select(sales, having(on(Sale.class).getValue(), greaterThan(50000)))
    ), on(Sale.class).getBuyer() 
), on(Person.class).getAge());
```

Here a list of Sales is filtered in order to select only the sales with a value greater than 50000, then from each of these sale is extracted its buyer and in the end the resulting Persons are sorted based on their ages. This single statement looks quite powerful, especially if you compare what you should do to achieve the same result in plain java:

```
List<Person> buyersSortedByAges = new ArrayList<Person>();
for(Sale sale : sales) {
    if (sale.getValue() > 50000) {
        buyersSortedByAges.add(sale.getBuyer());
    }
}
Collections.sort(buyersSortedByAges, new Comparator<Person>() {
    public int compare(Person p1, Person p2) {
        return p1.getAge() - p2.getAge();
    }
});
```

Anyway the lambdaj version could be a bit difficult to be read due to its nested form. That's why the release 2.3 introduced the so called [LambdaCollections](http://lambdaj.googlecode.com/svn/trunk/html/apidocs/ch/lambdaj/collection/LambdaCollections.html) that allows to use the same features in a more readable way:

```
List<Person> buyersSortedByAges = with(sales)
    .retain(having(on(Sale.class).getValue(), greaterThan(50000)))
    .extract(on(Sale.class).getBuyer())
    .sort(on(Person.class).getAge());
```

These special collections implements a [fluent interface](http://en.wikipedia.org/wiki/Fluent_interface) providing a more handy way to employ the lambdaj features. In more details the with() static method wraps a plain list (set, map or array) with the corresponding lambda collection.

Note that the [LambdaCollections](http://lambdaj.googlecode.com/svn/trunk/html/apidocs/ch/lambdaj/collection/LambdaCollections.html) actually implement the corresponding java interface (i.e. [LambdaList](http://lambdaj.googlecode.com/svn/trunk/html/apidocs/ch/lambdaj/collection/LambdaList.html) implements java.util.List) so you can use them in all other API as usual. This is done by simply delegating to the original wrapped collection for the methods belonging to the base interface, so you don't have to pay any performance penalty when invoking these methods.

Thus the LambdaCollections seamlessly enrich the API of the traditional [Java Collection Framework](http://java.sun.com/docs/books/tutorial/collections/index.html) with a fluent interface that allow to leverage the lambdaj's feature in a more comfortable way. The invocations of the methods of this fluent interface also change the state of the original wrapped collection. This implementation choice has been driven by 2 reasons:

  1. While to leave the original collection unchanged is closer to the functional programming principles, it didn't make too much sense to enforce an immutable API on an object that is however widely mutable by invoking the methods of the collection framework API.
  1. By never changing the instance of the wrapped collection assures that its characteristic are always reflected even by the wrapping lambdaj counterpart. It means, for example, that if you are wrapping a [SortedSet](http://java.sun.com/javase/6/docs/api/java/util/SortedSet.html) with a [LambdaSet](http://lambdaj.googlecode.com/svn/trunk/html/apidocs/ch/lambdaj/collection/LambdaSet.html), the items in this last Set are always guaranteed of staying sorted with the same criteria defined in the original Set.

Of course if you need to leave unchanged the original collection (i.e. when it is a list of JPA entities and you don't want to accidentally cascade delete entities from the
database) it is enough to clone it before to use the other fluent methods as it follows:

```
with(sales).clone().remove(...) ...
```

A special thank to Gianfranco Tognana who suggested the main ideas and developed part of the implementation of the LambdaCollections.