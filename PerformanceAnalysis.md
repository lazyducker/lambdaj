This table show the minimum, maximum and average duration in milliseconds of 20 runs of 100,000 iterations of the examples below. These examples are referred to a very simple data model made of some sales of cars between two persons, a seller and a buyer. They show a comparison of how the same results can be achieved in the usual iterative java style and by using lambdaj. These figures are referred to the last 2.3.2 release

<table border='1'>
<blockquote><tr>
<blockquote><td></td>
<td align='center'><strong>iterative</strong></td>
<td align='center'><strong>lambdaj</strong></td>
<td></td>
</blockquote></tr>
<tr>
<blockquote><td width='240px'><strong>Test name</strong></td>
<td width='80px' align='center'>min</td>
<td width='80px' align='center'>max</td>
<td width='80px' align='center'>avg</td>
<td width='80px' align='center'>min</td>
<td width='80px' align='center'>max</td>
<td width='80px' align='center'>avg</td>
<td width='80px' align='center'><strong>ratio</strong></td>
</blockquote></tr>
<tr>
<blockquote><td><a href='http://code.google.com/p/lambdaj/wiki/PerformanceAnalysis?ts=1281993389&updated=PerformanceAnalysis#Print_all_brands'>Print all brands</a></td>
<td align='right'>265</td>
<td align='right'>312</td>
<td align='right'>283</td>
<td align='right'>1,310</td>
<td align='right'>1,591</td>
<td align='right'>1,377</td>
<td align='right'><strong>4.866</strong></td>
</blockquote></tr>
<tr>
<blockquote><td><a href='http://code.google.com/p/lambdaj/wiki/PerformanceAnalysis?ts=1281993389&updated=PerformanceAnalysis#Select_all_sales_of_a_Ferrari'>Select all sales of a Ferrari</a></td>
<td align='right'>281</td>
<td align='right'>437</td>
<td align='right'>366</td>
<td align='right'>1,528</td>
<td align='right'>1,607</td>
<td align='right'>1,566</td>
<td align='right'><strong>4.279</strong></td>
</blockquote></tr>
<tr>
<blockquote><td><a href='http://code.google.com/p/lambdaj/wiki/PerformanceAnalysis?ts=1281993389&updated=PerformanceAnalysis#Find_buys_of_youngest_person'>Find buys of youngest person</a></td>
<td align='right'>5,585</td>
<td align='right'>5,975</td>
<td align='right'>5,938</td>
<td align='right'>6,895</td>
<td align='right'>6,989</td>
<td align='right'>6,936</td>
<td align='right'><strong>1.168</strong></td>
</blockquote></tr>
<tr>
<blockquote><td><a href='http://code.google.com/p/lambdaj/wiki/PerformanceAnalysis?ts=1281993389&updated=PerformanceAnalysis#Find_most_costly_sale'>Find most costly sale</a></td>
<td align='right'>218</td>
<td align='right'>234</td>
<td align='right'>227</td>
<td align='right'>655</td>
<td align='right'>702</td>
<td align='right'>670</td>
<td align='right'><strong>2.952</strong></td>
</blockquote></tr>
<tr>
<blockquote><td><a href='http://code.google.com/p/lambdaj/wiki/PerformanceAnalysis?ts=1281993389&updated=PerformanceAnalysis#Sum_costs_where_both_are_males'>Sum costs where both are males</a></td>
<td align='right'>358</td>
<td align='right'>452</td>
<td align='right'>375</td>
<td align='right'>2,199</td>
<td align='right'>2,637</td>
<td align='right'>2,247</td>
<td align='right'><strong>5.592</strong></td>
</blockquote></tr>
<tr>
<blockquote><td><a href='http://code.google.com/p/lambdaj/wiki/PerformanceAnalysis?ts=1281993389&updated=PerformanceAnalysis#Find_age_of_youngest_who_bought_for_more_than_50000'>Age of youngest who bought for > 50,000</a></td>
<td align='right'>5,257</td>
<td align='right'>5,319</td>
<td align='right'>5,292</td>
<td align='right'>9,625</td>
<td align='right'>9,750</td>
<td align='right'>9,696</td>
<td align='right'><strong>1.832</strong></td>
</blockquote></tr>
<tr>
<blockquote><td><a href='http://code.google.com/p/lambdaj/wiki/PerformanceAnalysis?ts=1281993389&updated=PerformanceAnalysis#Sort_sales_by_cost'>Sort sales by cost</a></td>
<td align='right'>1,388</td>
<td align='right'>1,482</td>
<td align='right'>1,448</td>
<td align='right'>3,213</td>
<td align='right'>3,245</td>
<td align='right'>3,231</td>
<td align='right'><strong>2.231</strong></td>
</blockquote></tr>
<tr>
<blockquote><td><a href='http://code.google.com/p/lambdaj/wiki/PerformanceAnalysis?ts=1281993389&updated=PerformanceAnalysis#Extract_cars_original_cost'>Extract cars original cost</a></td>
<td align='right'>140</td>
<td align='right'>156</td>
<td align='right'>141</td>
<td align='right'>234</td>
<td align='right'>249</td>
<td align='right'>236</td>
<td align='right'><strong>1.674</strong></td>
</blockquote></tr>
<tr>
<blockquote><td><a href='http://code.google.com/p/lambdaj/wiki/PerformanceAnalysis?ts=1281993389&updated=PerformanceAnalysis#Index_cars_by_brand'>Index cars by brand</a></td>
<td align='right'>172</td>
<td align='right'>203</td>
<td align='right'>186</td>
<td align='right'>327</td>
<td align='right'>343</td>
<td align='right'>336</td>
<td align='right'><strong>1.806</strong></td>
</blockquote></tr>
<tr>
<blockquote><td><a href='http://code.google.com/p/lambdaj/wiki/PerformanceAnalysis?ts=1281993389&updated=PerformanceAnalysis#Group_sales_by_buyers_and_sellers'>Group sales by buyers and sellers</a></td>
<td align='right'>9,469</td>
<td align='right'>9,766</td>
<td align='right'>9,507</td>
<td align='right'>12,698</td>
<td align='right'>12,838</td>
<td align='right'>12,753</td>
<td align='right'><strong>1.341</strong></td>
</blockquote></tr>
<tr>
<blockquote><td><a href='http://code.google.com/p/lambdaj/wiki/PerformanceAnalysis?ts=1281993389&updated=PerformanceAnalysis#Find_most_bought_car'>Find most bought car</a></td>
<td align='right'>3,744</td>
<td align='right'>3,884</td>
<td align='right'>3,846</td>
<td align='right'>4,181</td>
<td align='right'>4,259</td>
<td align='right'>4,211</td>
<td align='right'><strong>1.095</strong></td>
</blockquote></tr>
<tr>
<blockquote><td align='right'><h2>Average ratio = 2.658</h2></td>
</blockquote></tr>
</table></blockquote>

## Print all brands ##
It concatenates, in a comma separated String, all the cars' brands
### iterative version: ###
```
StringBuilder sb = new StringBuilder();
for (Car car : db.getCars()) 
    sb.append(car.getBrand()).append(", ");
String brands = sb.toString().substring(0, sb.length()-2);
```
### lambdaj version: ###
```
String brands = joinFrom(db.getCars()).getBrand();
```

## Select all sales of a Ferrari ##
It filters all the sales of a car having the brand equals to "Ferrari"
### iterative version: ###
```
List<Sale> salesOfAFerrari = new ArrayList<Sale>();
for (Sale sale : sales) {
    if (sale.getCar().getBrand().equals("Ferrari")) 
        salesOfAFerrari.add(sale);
}
```
### lambdaj version: ###
```
List<Sale> salesOfAFerrari = select(sales,
    having(on(Sale.class).getCar().getBrand(),equalTo("Ferrari")));
```

## Find buys of youngest person ##
It finds the youngest person and then filters all the sales where that person was the buyer
### iterative version: ###
```
Person youngest = null;
for (Person person : persons)
    if (youngest == null || person.getAge() < youngest.getAge())
        youngest = person;
List<Sale> buys = new ArrayList<Sale>();
for (Sale sale : sales)
    if (sale.getBuyer().equals(youngest)) 
        buys.add(sale);
```
### lambdaj version: ###
```
List<Sale> sales = select(sales,having(on(Sale.class).getBuyer(),
    equalTo(selectMin(persons, on(Person.class).getAge()))));
```

## Find most costly sale ##
### iterative version: ###
```
double maxCost = 0.0;
for (Sale sale : sales) {
    double cost = sale.getCost();
    if (cost > maxCost) maxCost = cost;
}
```
### lambdaj version: ###
```
Sol. 1 -> double maxCost = max(sales, on(Sale.class).getCost());
Sol. 2 -> double maxCost = maxFrom(sales).getCost();
```

## Sum costs where both are males ##
It sums up the values of all the sales where both the buyer and the seller are males
### iterative version: ###
```
double sum = 0.0;
for (Sale sale : sales) {
    if (sale.getBuyer().isMale() && sale.getSeller().isMale())
        sum += sale.getCost();
}
```
### lambdaj version: ###
```
double sum = sumFrom(select(sales, 
    having(on(Sale.class).getBuyer().isMale())
    .and( having(on(Sale.class).getSeller().isMale())))).getCost();
```

## Find age of youngest who bought for more than 50000 ##
### iterative version: ###
```
int age = Integer.MAX_VALUE;
for (Sale sale : sales) {
    if (sale.getCost() > 50000.00) {
        int buyerAge = sale.getBuyer().getAge();
        if (buyerAge < age) age = buyerAge;
    }
}
```
### lambdaj version: ###
```
int age = min(forEach(select(sales, having(on(Sale.class).getCost(),
    greaterThan(50000.00)))).getBuyer(), on(Person.class).getAge());
```

## Sort sales by cost ##
### iterative version: ###
```
List<Sale> sortedSales = new ArrayList<Sale>(sales);
Collections.sort(sortedSales, new Comparator<Sale>() {
    public int compare(Sale s1, Sale s2) {
        return Double.valueOf(s1.getCost()).compareTo(s2.getCost());
    }
});
```
### lambdaj version: ###
```
List<Sale> sortedSales = sort(sales, on(Sale.class).getCost());
```

## Extract cars original cost ##
### iterative version: ###
```
List<Double> costs = new ArrayList<Double>();
for (Car car : cars) costs.add(car.getOriginalValue());
```
### lambdaj version: ###
```
List<Double> costs = extract(cars, on(Car.class).getOriginalValue());
```

## Index cars by brand ##
### iterative version: ###
```
Map<String, Car> carsByBrand = new HashMap<String, Car>();
for (Car car : db.getCars()) 
    carsByBrand.put(car.getBrand(), car);
```
### lambdaj version: ###
```
Map<String, Car> carsByBrand = index(cars, on(Car.class).getBrand());
```

## Group sales by buyers and sellers ##
### iterative version: ###
```
Map<Person,Map<Person,Sale>> map = new HashMap<Person,Map<Person,Sale>>();
for (Sale sale : sales) {
    Person buyer = sale.getBuyer();
    Map<Person, Sale> buyerMap = map.get(buyer);
    if (buyerMap == null) {
        buyerMap = new HashMap<Person, Sale>();
        map.put(buyer, buyerMap);
    }
    buyerMap.put(sale.getSeller(), sale);
}
Person youngest = null;
Person oldest = null;
for (Person person : persons) {
    if (youngest == null || person.getAge() < youngest.getAge())
        youngest = person;
    if (oldest == null || person.getAge() > oldest.getAge()) 
        oldest = person;
}
Sale saleFromYoungestToOldest = map.get(youngest).get(oldest);
```
### lambdaj version: ###
```
Group<Sale> group = group(sales, by(on(Sale.class).getBuyer()),by(on(Sale.class).getSeller()));
Person youngest = selectMin(persons, on(Person.class).getAge());
Person oldest = selectMax(persons, on(Person.class).getAge());
Sale sale = group.findGroup(youngest).find(oldest).get(0);
```

## Find most bought car ##
It finds the car that has been bought more times
### iterative version: ###
```
Map<Car, Integer> carsBought = new HashMap<Car, Integer>();
for (Sale sale : sales) {
    Car car = sale.getCar();
    Integer boughtTimes = carsBought.get(car);
    carsBought.put(car, boughtTimes == null ? 1 : boughtTimes+1);
}

Car mostBoughtCarIterative = null;
int boughtTimesIterative = 0;
for (Entry<Car, Integer> entry : carsBought.entrySet()) {
    if (entry.getValue() > boughtTimesIterative) {
        mostBoughtCarIterative = entry.getKey();
        boughtTimesIterative = entry.getValue();
    }
}
```
### lambdaj version: ###
```
Group<Sale> group = selectMax(
    group(sales, by(on(Sale.class).getCar())).subgroups(), on(Group.class).getSize());
Car mostBoughtCar = group.findAll().get(0).getCar();
int boughtTimes = group.getSize();
```