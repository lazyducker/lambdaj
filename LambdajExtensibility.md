# Introduction #

Lambdaj comes with lots of features that should help to make your code more readable and in many cases shorter than its original imperative version. Conciseness anyway it's just a side effect in using the DSL provided by lambdaj and in my opinion not the most remarkable one. The main purpose in using lambdaj should be to improve the readability of the code, decrease its cyclomatic complexity and promote reusability and maintainability. That's why lambdaj has been thought to be easily extensible allowing to deal with the specific needs of your domain model that of course cannot be directly covered out-of-the-box by the library. Let's see in more details how you can do that.

# Converting objects by implementing the Converter interface #

To convert objects in a different kind of objects is something very common when manipulating a given domain model. Lambdaj already offers this feature in a very basic way as in the following example:

```
List<Double> speeds = extract(cars, on(Car.class).getSpeed());
```

Here a list of cars is converted in a list of doubles by extracting the value of the speed property from each car. In other word you are implicitly defining a function that converts a Car in a Double and applying this function to all the cars in your list. It means that you could achieve the same result by defining an your own Converter:

```
class Car2SpeedConverter implements Converter<Car, Double> {
    public Double convert(Car car) {
        return car.getSpeed();
    }
}
```

and apply it to all the cars through the convert method:

```
List<Double> speeds = convert(cars, new Car2SpeedConverter());
```

In this the Converter (that as stated converts a Car in a Double) is extremely trivial. But you could feel the need to write your own converter for your specific needs. For example (even if I don't like Data Transfer Object) you could have the need to transform a list of your business domain objects in the corresponding DTOs. In this case could be useful to put the business logic that converts the your domain object in a DTO in a Converter in order to allow you to reuse the same logic in other part of your application.

# Filtering with your conditions by extending a Matcher #

As it happens with converters, lambdaj, through its DSL, gives the possibility to create in a very readable way some simple matcher. For example the following statement:

```
List<Sale> salesOfAFerrari = select(cars, having(on(Car.class).getBrand(), equalTo("Ferrari")));
```

defines a Matcher that filters all the cars having "Ferrari" as brand. Once again you could achieve the same result by defining your own matcher:

```
class BrandMatcher extends LambdaJMatcher<Car> {
    private final String brand;
    public BrandMatcher(String brand) {
        this.brand = brand;
    }

    public boolean matches(Object car) {
        return ((Car)car).getBrand().equals(brand);
    }
}
```

and than use it in the select statement as:

```
List<Sale> salesOfAFerrari = select(cars, new BrandMatcher("Ferrari"));
```

The LambdaJMatcher is just an extension of the Hamcrest's one that provide a small fluent interface allowing to combine more conditions through its or() and and() methods. Anyway you can also filter in lambdaj using the traditional Hamcrest's matchers if you prefer.

# Writing your own aggregation rules #

Lambdaj already provides the most common aggregation rules like sum, strings concatenation and search for min and max values in a list. For example to find the total costs of all the cars in your list you could write something like:

```
double totalCost = sum(cars, on(Car.class).getCost());
```

Anyway it could happen that you need an aggregation algorithm not already provided by lambdaj. For example let's suppose that for some reason you need to multiply those costs instead of sun them. In this case, since lambdaj doesn't provide an aggregator for that, you should write your own as it follows:

```
class Product extends PairAggregator<Double>() {
    protected Double emptyItem() { return 1.0; }
    protected Double aggregate(Double first, Double second) { return first * second; }
}
```

Here we have defined an Aggregator of Double by saying that its empty item (the idempotent value for the operation we are defining) is 1.0 and the rule to aggregate 2 items is multiplying them. After we can use this aggregator to find the product of the costs of our cars as it follows:

```
double productOfCost = aggregate(cars, new Product(), on(Car.class).getCost());
```

# Putting it all together #

The following example has been taken from the talk "Funky Java, Objective Scala" given by Dick Wall during the last JavaOne. In one of the project on which he works, he needed to implement a quite complex algorithm to calculate the Genetic Composite Index, i.e. the probability that a person could get a certain disease given its DNA sequence. In more details to calculate this value the algorithm works as follows:

  1. Given the SnpDetail, obtain the testRsId, and filter out any SnpDetails that do not have that testRsId in the key of the call map
  1. Following that filter (or perhaps as a second part of that filter) obtain the call from the map for that testRsId, and test to see if it is "fine" (e.g. a valid result). There is a method call isFine on the call that returns a boolean true or false
  1. By this stage, we should have a filtered list of SnpDetails that are satisfied by valid calls in the callMap
  1. Next, get the value of the call (it is a string) and use the methods getGenotypeForCall and getOddsRatioForGenotype on the snpDetail method to turn the call result into a double - representing an odds ratio (a sort of proportion of risk of having some condition)
  1. The result of this should be that a list of SnpDetails combined with the calls from the CallMap that satisfy them, should result in a list of doubles (or Doubles, it doesn't really matter - I am going for clarity, not performance in these examples).
  1. These doubles are then reduced using a product operator, so the whole thing is a map/reduce that results in a single number representing a risk score

The implementation of this algorithm looked as it follows:

```
public double getGciScore(final Condition condition, final Map<String, CallInfo> callMap) {
    double gci = 1.0;   // start with the identity multiplier
    
    for (SnpDetail snpDetail : conditions.snpDetails) {
        if (callMap.containsKey(snpDetails.testRdId)) {
            CallInfo callInfo = callMap.get(snpDetail.testRsId);
            if (callInfo.isFine()) {
                Genotype geno = snpDetail.getGenotypeForCall(callInfo.call);
                gci = gci * snpDetail.getOddsRatioForGenotype(geno);
            }
        }
    }
    return gci;
}
```

What's wrong with this approach? As Dick underlined, imperative code doesn't fit well with this kind of algorithms and most importantly doesn't allow to scale in complexity. Indeed in those cases you could easily have to deal with massively nested loops, multiple conditions and monster methods.

By leveraging the features provided by lambdaj it is possible to rewrite this algorithm in a completely functional way as:

```
public double getGciScore(final Condition condition, final Map<String, CallInfo> callMap) {
    Matcher<SnpDetail> satisfiedSnpDetailsFilter = new LambdaJMatcher<SnpDetail>() {
        public boolean matches(Object object) {
            SnpDetail snpDetail = (SnpDetail)object;
            return (callMap.containsKey(snpDetail.testRsId) && callMap.get(snpDetail.testRsId).isFine());
        }
    };

    Converter<SnpDetail, Double> snpDetailToGciScoreFn = new Converter<SnpDetail, Double>() {
        public Double convert(SnpDetail snpDetail) {
            return snpDetail.getOddsRatioForGenotype(snpDetail.getGenotypeForCall(callMap.get(snpDetail.testRsId).call));
        }
    };

    Aggregator<Double> product = new PairAggregator<Double>() {
        protected Double emptyItem() { return 1.0; }
        protected Double aggregate(Double first, Double second) { return first * second; }
    };

    double gci = aggregate(convert(select(condition.snpDetails, satisfiedSnpDetailsFilter), snpDetailToGciScoreFn), product);
    return gci;
}
```

Despite its apparent verbosity this second version has some valuable advantages:
  * allows to decompose the problem in smaller and then more manageable parts;
  * decreases the cyclomatic complexity of the algorithm;
  * promotes code reuse: you cannot reuse a for cycle or an if condition but you can easily reuse a component (a function) like a Converter or a Matcher;
  * improves the testability.

In the end it worth to note that by using the lambdaj fluent interface collections available since lambdaj 2.3 you could rewrite the last statement in an hopefully more readable way as:

```
double gci = with(condition.snpDetails)
             .retain(satisfiedSnpDetailsFilter)
             .convert(snpDetailToGciScoreFn)
             .aggregate(product);
```